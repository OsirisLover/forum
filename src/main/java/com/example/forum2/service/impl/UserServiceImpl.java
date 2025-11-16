package com.example.forum2.service.impl;


import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.dao.UserMapper;
import com.example.forum2.explication.ApplicationException;
import com.example.forum2.model.User;
import com.example.forum2.service.IUserService;
import com.example.forum2.utils.MD5Utils;
import com.example.forum2.utils.StringUtils;
import com.example.forum2.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void createNormal(User user) {
        //判断参数是否为空，提高程序的健壮性
        if(user==null){
            log.warn(AppResult.failed(ResultCode.ERROR_IS_NULL).toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }

        //判断用户是否已经存在
        User user1 = userMapper.selectByUsername(user.getUsername());
        if(user1!=null){
            log.warn(ResultCode.FAILED_USER_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_EXISTS));
        }

        //设置默认值
        user.setGender((byte) 2);
        user.setPhoneNum("");
        user.setArticleCount(0);
        user.setIsAdmin((byte) 0);
        user.setState((byte) 0);
        user.setDeleteState((byte) 0);
        Date date=new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);

        int row = userMapper.insertSelective(user);
        if(row!=1){
            log.warn(ResultCode.FAILED_CREATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_CREATE));
        }
        log.info("新增用户成功");


    }

    @Override
    public User login(String username, String password) {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            log.warn(ResultCode.FAILED_PARAMS_VALIDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        User user = userMapper.selectByUsername(username);
        if(user==null){
            //用户不存在
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }

        boolean checkPassword = MD5Utils.compareToCiphertext(password, user.getSalt(), user.getPassword());

        if(!checkPassword){
            //密码错误
            log.warn(ResultCode.FAILED_LOGIN.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_LOGIN));
        }
           return user;
    }

    @Override
    public User selectById(Long id) {

        if(id==null){
            log.warn(AppResult.failed(ResultCode.ERROR_IS_NULL).toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        User user = userMapper.selectByPrimaryKey(id);

        return user;
    }

    @Override
    public void modifyPassword(Long id, String newPassword, String oldPassword) {
// 1. 根据用户Id查询用户信息
        User user = userMapper.selectByPrimaryKey(id);
        // 2. 校验用户Id是否有存在
        if (user == null) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UNAUTHORIZED));
        }

        // 3. 校验原密码是否正确
        String oldEncryptPassword = MD5Utils.md5AndSalt(oldPassword, user.getSalt());
        if (!oldEncryptPassword.equalsIgnoreCase(user.getPassword())) {
            // 记录日志
            log.info(ResultCode.FAILED_PARAMS_VALIDATE.toString() + "username = " + user.getUsername() + ", password = " + oldPassword);
            // 密码不正确抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE));
        }
        // 4. 重新生成扰动字符串
        String salt = UUIDUtils.UUID_32();
        // 5. 计算新密码
        String encryptPassword = MD5Utils.md5AndSalt(newPassword, salt);
        // 6. 创建用于更新的User对象
        User modifyUser = new User();
        // 设置用户Id
        modifyUser.setId(user.getId());
        // 设置新密码
        modifyUser.setPassword(encryptPassword);
        // 设置扰动字符串
        modifyUser.setSalt(salt);
        // 设置更新时间
        modifyUser.setUpdateTime(new Date());

        // 7. 更新操作
        int row = userMapper.updateByPrimaryKeySelective(modifyUser);
        if (row != 1) {
            // 记录日志
            log.info(ResultCode.FAILED.toString() + "修改密码失败. userId = " + user.getId());
            // 抛出异常
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED));
        }
        // 打印日志
        log.info("用户密码修改成功. userId = " + user.getId());
    }


    @Override
    public void modifyInfo(Long id, Byte gender, String nickname, String phoneNum, String email, String remark) {
        // 非空校验，id为null 或 其他的参数全部为null
        if (id == null || (gender == null
                && StringUtils.isEmpty(nickname)
                && StringUtils.isEmpty(phoneNum)
                && StringUtils.isEmpty(email)
                && StringUtils.isEmpty(remark))) {
            log.warn(ResultCode.ERROR_IS_NULL.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.ERROR_IS_NULL));
        }
        // 校验用户状态
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null || user.getDeleteState() == 1) {
            log.warn(ResultCode.FAILED_USER_NOT_EXISTS.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_USER_NOT_EXISTS));
        }

        // 构造一个修改对象
        User updateUser = new User();
        // 设置Id
        updateUser.setId(id);
        // 设置昵称
        if (!StringUtils.isEmpty(nickname)) {
            updateUser.setNickname(nickname);
        }
        // 设置电话
        if (!StringUtils.isEmpty(phoneNum)) {
            updateUser.setPhoneNum(phoneNum);
        }
        // 设置邮箱
        if (!StringUtils.isEmpty(email)) {
            updateUser.setEmail(email);
        }
        // 个人简介
        if (!StringUtils.isEmpty(remark)) {
            updateUser.setRemark(remark);
        }
        // 性别
        if (gender != null && gender >= 0 && gender <= 2) {
            updateUser.setGender(gender);
        }

        // 调用DAO
        int row = userMapper.updateByPrimaryKeySelective(updateUser);
        if (row != 1) {
            log.warn(ResultCode.FAILED_UPDATE.toString());
            throw new ApplicationException(AppResult.failed(ResultCode.FAILED_UPDATE));
        }
    }
}


package com.example.forum2.controller;
import com.example.forum2.common.AppResult;
import com.example.forum2.common.ResultCode;
import com.example.forum2.config.AppConfig;
import com.example.forum2.model.User;
import com.example.forum2.service.IUserService;
import com.example.forum2.utils.MD5Utils;
import com.example.forum2.utils.StringUtils;
import com.example.forum2.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")

@Slf4j
public class UserController {

    @Resource
    private IUserService iUserService;

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public AppResult register(@ApiParam(value = "用户名") @RequestParam(value = "username") @NonNull String username,
                              @ApiParam(value = "昵称") @RequestParam(value = "nickname") @NonNull String nickname,
                              @ApiParam(value = "密码") @RequestParam(value = "password") @NonNull String password,
                              @ApiParam(value = "确认密码") @RequestParam(value = "passwordRepeat") @NonNull String passwordRepeat) {


        //判断密码
        if (!password.equals(passwordRepeat)) {
            log.warn(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString());
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        //获取随机扰动字符
        String salt = UUIDUtils.UUID_32();

        //生成密文
        String ciphertext = MD5Utils.md5AndSalt(password, salt);

        //初始化用户对象
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);

        user.setSalt(salt);
        user.setPassword(ciphertext);

        iUserService.createNormal(user);
        log.info("ID : " + user.getId() + " 注册成功");
        return AppResult.success();
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public AppResult<User> login(HttpServletRequest request,
                                 @ApiParam(value = "用户名") @RequestParam(value = "username") @NonNull String username,
                                 @ApiParam(value = "密码") @RequestParam(value = "password") @NonNull String password) {
        User loginUser = iUserService.login(username, password);

        HttpSession session = request.getSession(true);  //如果有会话，则返回，如果没有则新建一个会话
        session.setAttribute(AppConfig.USER_SESSION,loginUser);


        return AppResult.success(loginUser);
    }

    @ApiOperation("用户信息")
    @GetMapping("/info")
    public AppResult<User> info(HttpServletRequest request,
                                @ApiParam(value = "用户Id") @RequestParam(value = "id",required = false) Long id) {

        User user=null;
        if(id==null){
            HttpSession session = request.getSession(false);
             user = (User) session.getAttribute(AppConfig.USER_SESSION);
        }else{
            user=iUserService.selectById(id);
        }
        return AppResult.success(user);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @ApiOperation("用户退出")
    @GetMapping("/logout")
    public AppResult<User> login(HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        if(session!=null){
            //销毁session
            session.invalidate();
        }
        return AppResult.success();
    }

    /**
     * 修改用户信息
     * @param id 用户id
     * @param gender 性别
     * @param nickname 昵称
     * @param phoneNum 电话号码
     * @param email 邮箱地址
     * @param remark 个人简介
     */
    @ApiOperation("修改用户信息")
    @PostMapping("/modifyInfo")
    public AppResult modifyInfo (HttpServletRequest request,
                                 @ApiParam("用户Id") @RequestParam("id") @NonNull Long id,
                                 @ApiParam("性别") @RequestParam(value = "gender", required = false) Byte gender,
                                 @ApiParam("昵称") @RequestParam(value = "nickname", required = false) String nickname,
                                 @ApiParam("电话号码") @RequestParam(value = "phoneNum", required = false) String phoneNum,
                                 @ApiParam("邮箱") @RequestParam(value = "email", required = false) String email,
                                 @ApiParam("个人简介") @RequestParam(value = "remark", required = false) String remark) {
        // 校验
        if (gender == null
                && StringUtils.isEmpty(nickname)
                && StringUtils.isEmpty(phoneNum)
                && StringUtils.isEmpty(email)
                && StringUtils.isEmpty(remark)) {
            // 参数同时为空时返回错误信息
            return AppResult.failed(ResultCode.FAILED_PARAMS_VALIDATE.toString());
        }
        // 校验传入的Id是否为当前登录用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getId() != id) {
            // 返回错误
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED.toString());
        }
        // 调用service
        iUserService.modifyInfo(id, gender, nickname, phoneNum, email, remark);
        // 重新获取用户信息，更新session
        user = iUserService.selectById(id);
        session.setAttribute(AppConfig.USER_SESSION, user);
        // 返回结果
        return AppResult.success();
    }


    /**
     * 修改用户密码
     *
     * @param id 用户 id
     * @param oldPassword 密码
     * @param newPassword 新密码
     * @param passwordRepeat 重复密码
     * @return
     */
    @ApiOperation("修改用户密码")
    @PostMapping("/modifyPwd")
    public AppResult modifyPassword (HttpServletRequest request,
                                     @ApiParam(value = "用户Id") @RequestParam(value = "id") @NonNull Long id,
                                     @ApiParam(value = "原密码") @RequestParam(value = "oldPassword")  @NonNull String oldPassword,
                                     @ApiParam(value = "新密码") @RequestParam(value = "newPassword")  @NonNull String newPassword,
                                     @ApiParam(value = "重复新密码") @RequestParam(value = "passwordRepeat")  @NonNull String passwordRepeat) {
        // 1. 获取Session中的用户信息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        // 2. 校验传入的用户Id是否为当前登录用户
        if (user == null || user.getId() != id) {
            // 记录日志
            log.info(ResultCode.FAILED_UNAUTHORIZED.toString() + "user id = " + id);
            // 校验不通过返回未授权
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        // 3. 校验新密码与重复密码是否相等
        if (!newPassword.equals(passwordRepeat)) {
            // 记录日志
            log.info(ResultCode.FAILED_TWO_PWD_NOT_SAME.toString() + "user id = " + id);
            // 返回错误信息
            return AppResult.failed(ResultCode.FAILED_TWO_PWD_NOT_SAME);
        }

        // 4. 调用Service处理
        iUserService.modifyPassword(user.getId(), newPassword, oldPassword);
        // 7. 记录日志
        log.info("用户密码修改成功: userId = " + user.getId());
        // 8. 返回成功
        return AppResult.success();
    }

}

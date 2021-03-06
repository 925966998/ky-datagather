package com.ky.redwood.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.redwood.entity.SysUserEntity;
import com.ky.redwood.logUtil.Log;
import com.ky.redwood.mapper.SysUserMapper;
import com.ky.redwood.mybatis.RestResult;
import com.ky.redwood.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/ky-redwood")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LoginService loginService;
    @Autowired
    SysUserMapper sysUserMapper;

    @Log(description = "登录操作", module = "系统登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpSession session) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        logger.info("The LoginController login method params are {},{}", userName, password);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", userName);
        jsonObject.put("password", password);
        RestResult restResult = (RestResult) loginService.login(jsonObject);
        if (restResult.getCode() == 10000) {
            session.setAttribute("user", restResult.getData());
            session.setMaxInactiveInterval(60 * 60 * 12);
        }
        return restResult;
    }


    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public Object loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new RestResult();
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.POST)
    public RestResult reset(@PathVariable String id) {
        String psw = "123456";
        String md5psw = DigestUtils.md5DigestAsHex(psw.getBytes());
        SysUserEntity sysUserEntity = sysUserMapper._get(id);
        if (sysUserEntity != null) {
            sysUserEntity.setPassword(md5psw);
            sysUserMapper._updateEntity(sysUserEntity);
            return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "重置成功默认密码:123456,请登录后立即修改密码");
        } else {
            return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "用户名不存在");
        }
    }
}

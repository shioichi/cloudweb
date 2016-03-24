package com.cpj.controller;

import com.cpj.openstack.Authenticate;
import com.cpj.openstack.KeyStone;
import com.cpj.service.UserService;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by chenpengjiang on 2016/3/7.
 */
@Controller
@SessionAttributes({"os_admin","os_internal"})
public class LoginController {
    @Resource
    UserService userService;
    @Resource
    Authenticate authenticate;
    @Resource
    KeyStone keystone;

    public final static String hostIp = "10.0.0.11";
    @RequestMapping("tologin.do")
    public String tologin(){
        return "login/login";
    }
    @RequestMapping("/login.do")
    public ModelAndView login(@RequestParam("userName")String userName, @RequestParam("password")String password, ModelMap model){
        int loginflag = userService.logincheck(userName,password);
        ModelAndView modelAndView = new ModelAndView();
        if(loginflag==0){
            modelAndView.addObject("errmsg","该用户不存在！");
            modelAndView.setViewName("login/login");
        }else if(loginflag==1){
            modelAndView.addObject("errmsg","密码错误！");
            modelAndView.setViewName("login/login");
        }else if(loginflag==200||loginflag==201){
            modelAndView.setViewName("index/index");
            if(loginflag==200){
                OSClient os_admin = authenticate.logauthold(hostIp,userName,"admin",password,Facing.ADMIN);
                model.addAttribute("os_admin",os_admin);
            }else if(loginflag==201){
                OSClient os_internal = authenticate.logauthold(hostIp,userName,"demo",password,Facing.INTERNAL);
                model.addAttribute("os_internal",os_internal);
            }

        }else{
            modelAndView.addObject("errmsg","请重新登陆！");
            modelAndView.setViewName("login/login");
        }

        return modelAndView;

    }
    @RequestMapping("logout.do")
    public String logout(SessionStatus sessionStatus){
        sessionStatus.setComplete();
        return "login/login";
    }

}

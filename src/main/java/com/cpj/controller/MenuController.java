package com.cpj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by chenpengjiang on 2016/3/23.
 */
@Controller
@SessionAttributes({"os_admin","os_internal"})
@RequestMapping("general")
public class MenuController {
    @RequestMapping("menu")
    public ModelAndView index(@RequestParam("module")String module) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(module+"/"+module);
        return mv;

        }

}

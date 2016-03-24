package com.cpj.controller;

import com.cpj.openstack.Compute;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Server;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenpengjiang on 2016/3/23.
 */
@Controller
@SessionAttributes({"os_admin","os_internal"})
@RequestMapping("Instance")
public class InstanceController {
    @Resource
    Compute compute;

    public static void writeJosn(HttpServletResponse response, String jsonStr) {

        try {

            response.setContentType("text/html");
            response.setHeader("Pragma", "No-cache");// 设置页面不缓存
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = null;
            out = response.getWriter();
            out.print(jsonStr);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object arg0) {
        try {
            return objectMapper.writeValueAsString(arg0);
        } catch (Exception e) {
            throw new RuntimeException(e.toString(), e);
        }
    }

    @ResponseBody
    @RequestMapping("getcurInstance")
    public void getCurInstance(@ModelAttribute("os_internal")OSClient os,HttpServletResponse response){
        List<? extends Server> servers = compute.Servergetall(os,true);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data", servers);
        String json = writeValueAsString(map);
        writeJosn(response, json);
    }
}

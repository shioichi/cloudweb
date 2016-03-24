package com.cpj.openstack;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Service;

/**
 * Created by chenpengjiang on 2016/3/8.
 */
@Service
public class Authenticate {
    /**
     *  V3登陆
     * @param serverip 服务期地址
     * @param username 登陆用户名
     * @param password  密码
     * @param tenant    所属project
     * @param var       登陆类型
     * @return
     */
    public OSClient logauth(String serverip,String username,String password,String tenant,Facing var){
        OSClient os = OSFactory.builderV3()
                .endpoint("http://"+serverip+":5000/v3")
                .credentials(username,password,Identifier.byName("default"))
                .scopeToProject(Identifier.byName(tenant), Identifier.byName("default"))
                .authenticate()
                .perspective(var);
        return os;
    }

    /**
     *  V2登陆
     * @param serverip 服务器地址
     * @param username 登陆用户名
     * @param password 密码
     * @param var      登陆类型
     * @return
     */
    public OSClient logauthold(String serverip,String username,String tenantname,String password,Facing var){
        OSClient os = OSFactory.builder()
                .endpoint("http://"+serverip+":5000/v2.0")
                .credentials(username,password)
                .tenantName(tenantname)
                .authenticate()
                .perspective(var);
        return os;
    }

}

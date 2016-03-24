package com.cpj.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.identity.RoleService;
import org.openstack4j.api.identity.ServiceManagerService;
import org.openstack4j.api.identity.TenantService;
import org.openstack4j.api.identity.UserService;
import org.openstack4j.model.compute.ActionResponse;
import org.openstack4j.model.identity.*;

import java.util.List;

/**
 * Created by chenpengjiang on 2016/3/8.
 */
@org.springframework.stereotype.Service
public class KeyStone {
    /*curd project*/

    /**
     * 获取project统一操作
     * @param os
     * @return
     */
    private TenantService getTenants(OSClient os) {
        return os.identity().tenants();
    }
    private UserService getUsers(OSClient os){
        return os.identity().users();
    }
    private RoleService getRoles(OSClient os){
        return os.identity().roles();
    }
    private ServiceManagerService getServices(OSClient os){
        return os.identity().services();
    }

    /**
     * 增加一个新的project
     * @param os
     * @param projectname
     * @param projectdes
     * @return
     */
    public Tenant  projectadd(OSClient os,String projectname,String projectdes){

        try {
            Tenant tenant = getTenants(os)
                    .create(new Builders().tenant().name(projectname).description(projectdes).build());
            return tenant;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除一个project，成功返回200，出现异常返回400
     * @param os
     * @param projectid
     * @return
     */
    public int projectdelete(OSClient os,String projectid){
        try {
            ActionResponse flag = getTenants(os).delete(projectid);
            return flag.getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return 400;
        }
    }

    /**
     * 更新一个project，返回tenant
     * @param os
     * @param projectid
     * @param projectname
     * @param projectdes
     * @return
     */
    public Tenant projectupdate(OSClient os,String projectid,String projectname,String projectdes){
        Tenant tenant = getTenants(os).get(projectid);
        if(projectname==null||projectname.equals("")){
            projectname = tenant.getName();
        }
        if(projectdes==null){
            projectdes=tenant.getDescription();
        }
        if(tenant!=null){
            try {
                tenant = getTenants(os).update(tenant.toBuilder().name(projectname).description(projectdes).build());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return tenant;
    }

    /**
     * 查询该用户下的所有project，如果是admin身份则查出所有的peoject，否则查出自己所属的project
     * @param os
     * @return
     */
    public List<? extends Tenant> Projectgetall(OSClient os){
        List<? extends Tenant> tenants = null;
        try {
            tenants = getTenants(os).list();
            return tenants;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 通过ID得到Project
     * @param os
     * @param projectId
     * @return
     */
    public Tenant projectgetbyId(OSClient os,String projectId){
        Tenant tenant = null;
        try {
            tenant = getTenants(os).get(projectId);
            return tenant;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过PROJECTNAME得到Project
     * @param os
     * @param projectName
     * @return
     */
    public Tenant projectgetbyname(OSClient os,String projectName){
        Tenant tenant = null;
        try {
            tenant = getTenants(os).getByName(projectName);
            return tenant;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

   /*curd user*/

    /**
     * 增加一个新用户
     * @param os 认证
     * @param name  新建用户名
     * @param password  新建密码
     * @param email 电子邮件
     * @param tenantid  projectid
     * @param roleid    角色id
     * @return 200成功
     */
    public int useradd(OSClient os,String name,String password,String email,String tenantid,String roleid){
        //封装用户对象
        User user = Builders.user()
                .name(name)
                .password(password)
                .email(email)
                .tenantId(tenantid).build();
        int flag = 0;
        try {
            //增加用户
            getUsers(os).create(user);
            //绑定角色给用户
            flag = getRoles(os).addUserRole(tenantid,user.getId(),roleid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 根据ID删除用户
     * @param os
     * @param userId
     * @return
     */
    public int userdel(OSClient os,String userId){
        int flag = 0;
        try {
           flag =  getUsers(os).delete(userId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据ID修改用户信息
     * @param os  认证信息
     * @param userId  用户ID
     * @param name 修改后用户名
     * @param email 修改后的邮件
     * @param tenantId  工程ID
     * @return 返回修改后的对象
     */
    public User userupdate(OSClient os,String userId,String name,String email,String tenantId){
        User user = getUsers(os).get(userId);
        if(name==null||name.equals("")){
            name = user.getName();
        }
        if(email==null){
            email = user.getEmail();
        }
        if(tenantId==null||tenantId.equals("")){
            tenantId = user.getTenantId();
        }
        if(user!=null) {
            try {
                user = getUsers(os).update(user.toBuilder().name(name).email(email).tenantId(tenantId).build());
            } catch (Exception e) {
                e.printStackTrace();
                user = null;
            }
        }
        return user;
    }

    /**
     * 查询所有用户的信息，要求是管理员角色，否则会返回空列表
     * @param os
     * @return
     */
    public List<? extends User> usersfindall(OSClient os){
        List<? extends User> users = null;
        try {
            users = getUsers(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 根据工程ID查询下面的用户
     * @param os
     * @param tenantid 工程ID
     * @return
     */
    public List<? extends User> usersgetbyproject(OSClient os,String tenantid){
        List<? extends User> users = null;
        try {
            users = getUsers(os).listTenantUsers(tenantid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 查询出所有的角色，需管理员权限认证，否则返回空
     * @param os
     * @return
     */
    public List<? extends Role> rolesgetall(OSClient os){
        List<? extends Role> roles = null;
        try {
            roles = getRoles(os).list();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 根据用户id查询用户的角色
     * @param os
     * @param userid
     * @return
     */
    public List<? extends Role> rolesbyuserid(OSClient os,String userid){
        List<? extends Role> roles = null;
        try {
            roles = getUsers(os).listRoles(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 根据用户id查询用户具体信息
     * @param os
     * @param userid
     * @return
     */
    public User userbyid(OSClient os,String userid){
        User user = null;
        try {
            user = getUsers(os).get(userid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

   /* curd service and endpoints*/

    /**
     * 查询所有的服务，需要管理员权限认证
     * @param os
     * @return
     */
    public List<? extends Service> servicesget(OSClient os){
        List<? extends  Service> services = null;
        try {
            services = getServices(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }

    /**
     * 查询所有的endpoints，需要管理员权限认证
     * @param os
     * @return
     */
    public List<? extends ServiceEndpoint> endpointget(OSClient os){
        List<? extends ServiceEndpoint> serviceEndpoints = null;
        try {
            serviceEndpoints = getServices(os).listEndpoints();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceEndpoints;
    }


}

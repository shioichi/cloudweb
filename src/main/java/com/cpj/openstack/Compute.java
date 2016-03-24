package com.cpj.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.*;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.network.NetFloatingIP;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by chenpengjiang on 2016/3/13.
 */
@Service
public class Compute {
    private FlavorService getFlavor(OSClient os) {
        return os.compute().flavors();
    }
    private ServerService getServers(OSClient os){return os.compute().servers();}
    private ComputeFloatingIPService getFloatingIps(OSClient os){return os.compute().floatingIps();}
    private KeypairService getKeypairs(OSClient os){return os.compute().keypairs();}
    private ComputeSecurityGroupService getSecGroups(OSClient os){return os.compute().securityGroups();}
    /*flavor curd*/

    /**
     * 新增一个flavor
     * @param os 认证
     * @param name 名称
     * @param ram 内存大小，int
     * @param cpus cpu数量int
     * @param disk 磁盘大小 int
     * @param swap 交换空间大小（M） int
     * @return
     */
    public Flavor flavorcreate(OSClient os,String name,int ram,int cpus,int disk,int swap){
        Flavor flavor = Builders.flavor()
                                .name(name)
                                .ram(ram)
                                .vcpus(cpus)
                                .disk(disk)
                                .swap(swap)
                                .build();
        flavor = getFlavor(os).create(flavor);
        return flavor;
    }

    /**
     * 根据ID删除flavor
     * @param os
     * @param flavorid
     * @return
     */
    public int flavordelete(OSClient os,String flavorid){
        int flag = 0;
        try {
            flag = getFlavor(os).delete(flavorid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询出所有的flavor，需要管理员权限
     * @param os
     * @return
     */
    public List<? extends Flavor> flavorall(OSClient os){
        List<? extends Flavor> flavors = null;
        try {
            flavors = getFlavor(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flavors;
    }

    /**
     * 查询出对应id的flavor
     * @param os
     * @param flavorid
     * @return
     */
    public Flavor flavorbyid(OSClient os,String flavorid){
        Flavor flavor = null;
        try {
            flavor = getFlavor(os).get(flavorid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flavor;
    }

    /*image curd nova内的操作并不完全，使用glance内的api*/

    /*server 操作*/

    /**
     * 新建并启动一个主机
     * @param os 认证
     * @param name  主机名称
     * @param flavorid 类型id
     * @param imageid 镜像id
     * @param netlist 网络列表
     * @return 服务信息
     */
    public Server bootserver(OSClient os, String name, String flavorid, String imageid,List<String> netlist){
        ServerCreate sc = Builders.server().name(name)
                .flavor(flavorid)
                .image(imageid)
                .networks(netlist)
                .build();
        Server server = null;
        try {
            server = getServers(os).boot(sc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return server;
    }

    /**
     * 操作虚拟机实例
     * @param os 认证
     * @param serverid 实例id
     * @param action 操作
     * @return 200成功 其他失败
     */
    public int Serveroperate(OSClient os, String serverid,Action action){
        int flag = 0;
        try {
            flag = getServers(os).action(serverid,action).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;

    }

    /**
     * 重启虚拟机
     * @param os 认证
     * @param serverid 实例id
     * @param type 重启类型，软重启、硬重启
     * @return 200成功
     */
    public int Serverextendoperate(OSClient os,String serverid,RebootType type){
        int flag = 0;
        try {
            flag = getServers(os).reboot(serverid,type).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改虚拟机配置，并确定
     * @param os 认证
     * @param serverid 实例id
     * @param flavorid 配置id
     * @return 200成功
     */
    public int Serverflavorchange(OSClient os,String serverid,String flavorid){
        int flag = 0;
        try {
            flag = getServers(os).resize(serverid,flavorid).getCode();
            flag = getServers(os).confirmResize(serverid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 创建实例的快照
     * @param os
     * @param serverid 实例id
     * @param snapshotname 快照名称
     * @return 快照id
     */
    public String snapshotcreate(OSClient os,String serverid,String snapshotname){
        String imageId = null;
        try {
            imageId = getServers(os).createSnapshot(serverid,snapshotname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageId;
    }

    /*floatingips*/

    /**
     * 获取可用的IP资源池的名称
     * @param os
     * @return 序列
     */
    public List<String> Poolnamesget(OSClient os){
        List<String> pools = null;
        try {
            pools = getFloatingIps(os).getPoolNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pools;
    }

    /**
     * 获取所有的floating ip 地址
     * @param os
     * @return
     */
    public List<? extends FloatingIP> Floatipsget(OSClient os){
        List<? extends FloatingIP> ips = null;
        try {
            ips = getFloatingIps(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ips;
    }

    /**
     * 绑定浮动ip到实例上
     * @param os
     * @param serverId
     * @param ipId
     * @return 成功返回200
     */
    public int floatingtoserver(OSClient os,String serverId,String ipId){
        int flag = 0;
        try {
            NetFloatingIP netFloatingIP = os.networking().floatingip().get(ipId);
            Server server = getServers(os).get(serverId);
            flag = getFloatingIps(os).addFloatingIP(server,netFloatingIP.getFloatingIpAddress()).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 移除floatingip
     * @param os
     * @param serverid
     * @param ipId
     * @return
     */
    public int floatingremove(OSClient os,String serverid,String ipId){
        int flag = 0;
        try {
            NetFloatingIP netFloatingIP = os.networking().floatingip().get(ipId);
            Server server = getServers(os).get(serverid);
            flag = getFloatingIps(os).removeFloatingIP(server,netFloatingIP.getFloatingIpAddress()).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /*metadata*/

    /**
     * 获取实例的metadata,返回map
     * @param os
     * @param serverid
     * @return
     */
    public Map<String,String> Metadataget(OSClient os,String serverid){
        Map<String,String> md = null;
        try {
            md = getServers(os).getMetadata(serverid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  md;
    }

    /**
     * 更新或替换实例的metadata，返回map
     * @param os
     * @param serverid
     * @param map
     * @return
     */
    public Map<String,String> Metadataupdate(OSClient os,String serverid,Map<String,String> map){
        Map<String,String> md = null;
        try {
            md = getServers(os).updateMetadata(serverid,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md;
    }

    /**
     * 删除实例的metadata
     * @param os
     * @param serverid
     * @param key
     * @return
     */
    public int Metadataremove(OSClient os,String serverid,String key){
        int flag = 0;
        try {
            flag = getServers(os).deleteMetadataItem(serverid,key).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /*VNC和控制台*/

    /**
     * 获取实例的控制台的输出，参数为输出的行数
     * @param os
     * @param serverid
     * @param linenum
     * @return
     */
    public String Consoleget(OSClient os,String serverid,int linenum){
        String consoleOut = null;
        try {
            consoleOut = getServers(os).getConsoleOutput(serverid,linenum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return consoleOut;
    }

    /**
     * 获取实例的VNC、XVNC控制台
     * @param os
     * @param serverid
     * @param type
     * @return
     */
    public VNCConsole vncget(OSClient os, String serverid,VNCConsole.Type type){
        VNCConsole console = null;
        try {
            console = getServers(os).getVNCConsole(serverid,type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return console;
    }

    /*获取实例占用资源信息*/

    /**
     * 根据实例id,获得实例资源使用情况
     * @param serverid
     * @return
     */
    public Map<String,? extends Number> ServerResGet(OSClient os,String serverid){
        Map<String,? extends Number> diangstics = null;
        try {
            diangstics = getServers(os).diagnostics(serverid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diangstics;
    }

    /*查询实例*/

    /**
     * 查询该用户下的所有实例
     * @param os
     * @param flag 如果false只显示少量信息
     * @return
     */
    public List<? extends Server> Servergetall(OSClient os,Boolean flag){
        List<? extends Server> servers = null;
        try {
            servers = getServers(os).list(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servers;
    }

    /**
     * 根据ID得到实例
     * @param os
     * @param serverid
     * @return
     */
    public Server Servergetbyid(OSClient os,String serverid){
        Server server = null;
        try {
            server = getServers(os).get(serverid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return server;
    }

    /**
     * 根据ID删除实例
     * @param os
     * @param serverid
     * @return
     */
    public int Serverdel(OSClient os,String serverid){
        int flag = 0;
        try {
            flag = getServers(os).delete(serverid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  flag;
    }

    /**
     * Keypairs操作
     */
    /**
     * 查询出所有keypairs
     * @param os
     * @return
     */
    public List<? extends Keypair> Keypairgetall(OSClient os){
        List<? extends Keypair> kps = null;
        try {
            kps = getKeypairs(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kps;
    }

    /**
     * 根据keyname查询
     * @param os
     * @param keyname
     * @return
     */
    public Keypair Keypairgetone(OSClient os,String keyname){
        Keypair kp = null;
        try {
            kp = getKeypairs(os).get(keyname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kp;
    }

    /**
     * 创建新的keypair
     * @param os
     * @param keyname
     * @return
     */
    public Keypair Keypaircreate(OSClient os,String keyname){
        Keypair kp = null;
        try {
            kp = getKeypairs(os).create(keyname,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kp;
    }

    /**
     * 根据keyname删除keypairs
     * @param os
     * @param keyname
     * @return
     */
    public int Keypairdelete(OSClient os,String keyname){
        int flag = 0;
        try {
            flag = getKeypairs(os).delete(keyname).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
/*
    Security Groups安全组
*/

    /**
     * 查询工程下的所有安全组
     * @param os
     * @return
     */
    public List<? extends SecGroupExtension> SecGroupGetAll(OSClient os){
        List<? extends SecGroupExtension> sg = null;
        try {
            sg = getSecGroups(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  sg;
    }

    /**
     * 根据serverid查询当前server的安全组策略
     * @param os
     * @param serverid
     * @return
     */
    public List<? extends SecGroupExtension> SecGroupGetServer(OSClient os,String serverid){
        List<? extends SecGroupExtension> sg = null;
        try {
            sg = getSecGroups(os).listServerGroups(serverid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  sg;
    }

    /**
     * 根据策略组id查询策略组
     * @param os
     * @param groupid
     * @return
     */
    public SecGroupExtension SecGroupGetOne(OSClient os,String groupid){
        SecGroupExtension group = null;
        try {
            group = getSecGroups(os).get(groupid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group;
    }

    /**
     * 创建新的安全组
     * @param os
     * @param name
     * @param des
     * @return
     */
    public SecGroupExtension SecGroupCreate(OSClient os,String name,String des){
        SecGroupExtension group = null;
        try {
            group = getSecGroups(os).create(name,des);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group;
    }

    /**
     * 更新安全组名称或描述
     * @param os
     * @param Secgourpid
     * @param name
     * @param des
     * @return
     */
    public SecGroupExtension SecGroupUpdate(OSClient os,String Secgourpid,String name,String des){
        if(name.equals("")||name==null){
            name = getSecGroups(os).get(Secgourpid).getName();
        }
        SecGroupExtension group = null;
        try {
            group = getSecGroups(os).update(Secgourpid,name,des);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return group;
    }

    /**
     * 为安全组增加一条策略
     * @param os
     * @param groupid
     * @param ipProtocol 类型
     * @param ipport  ip加掩码
     * @param start    端口号
     * @param end     端口号
     * @return
     */
    public SecGroupExtension.Rule SecRuleCreate(OSClient os,String groupid,IPProtocol ipProtocol,String ipport,int start,int end){
        SecGroupExtension.Rule rule = null;
        try {
            rule = getSecGroups(os).createRule(Builders.secGroupRule()
                                                                        .parentGroupId(groupid)
                                                                         .protocol(ipProtocol)
                                                                          .cidr(ipport)
                                                                            .range(start,end)
                                                                              .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rule;
    }

    /**
     * 删除一个安全组内的策略
     * @param os
     * @param ruleid
     * @return
     */
    public int Ruledelete(OSClient os,String ruleid){
        int flag = 0;
        try {
            flag = getSecGroups(os).deleteRule(ruleid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }

}



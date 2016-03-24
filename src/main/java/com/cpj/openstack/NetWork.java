package com.cpj.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.networking.NetworkService;
import org.openstack4j.api.networking.PortService;
import org.openstack4j.api.networking.RouterService;
import org.openstack4j.api.networking.SubnetService;
import org.openstack4j.model.network.*;

import java.util.List;

/**
 * Created by chenpengjiang on 2016/3/20.
 */
public class NetWork {
    private NetworkService getNetworking(OSClient os){return os.networking().network();}
    private SubnetService getSubnet(OSClient os){return os.networking().subnet();}
    private PortService getPort(OSClient os){return os.networking().port();}
    private RouterService getRouter(OSClient os){return os.networking().router();}
    /**
     * 创建网络
     * @param os
     * @param name
     * @param tenantid
     * @return
     */
    public Network NetworkCreate(OSClient os,String name,String tenantid){
        Network network = null;
        try {
            network = getNetworking(os).create(Builders.network().name(name).tenantId(tenantid).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }

    /**
     * 查询当前租户下的所有网络
     * @param os
     * @return
     */
    public List<? extends Network> NetWorkALL(OSClient os){
        List<? extends Network> networks = null;
        try {
            networks = getNetworking(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networks;
    }

    /**
     * 根据ID查询网络
     * @param os
     * @param networkid
     * @return
     */
    public Network NetWorkOne(OSClient os,String networkid){
        Network network = null;
        try {
            network = getNetworking(os).get(networkid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return network;
    }

    /**
     * 删除网络，返回200成功
     * @param os
     * @param networkid
     * @return
     */
    public int NetWorkDelete(OSClient os,String networkid){
        int flag = 0;
        try {
            flag = getNetworking(os).delete(networkid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  flag;
    }

    /**
     * 为网络新建一个子网
     * @param os
     * @param name
     * @param networkid
     * @param tenantid
     * @param ipbegin
     * @param ipend
     * @param type
     * @param cidr
     * @return
     */
    public Subnet SubnetCreate(OSClient os, String name, String networkid, String tenantid, String ipbegin, String ipend, IPVersionType type,String cidr){
        Subnet subnet = null;
        try {
            subnet = getSubnet(os).create(Builders.subnet().name(name)
            .networkId(networkid)
            .tenantId(tenantid)
            .addPool(ipbegin,ipend)
            .ipVersion(type)
            .cidr(cidr)
            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subnet;
    }

    /**
     * 查询当前所有可用的子网
     * @param os
     * @return
     */
    public List<? extends Subnet> Subnetgetall(OSClient os){
        List<? extends Subnet> subnets = null;
        try {
            subnets = getSubnet(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subnets;
    }

    /**
     *根据ID查询子网信息
     * @param os
     * @param subnetId
     * @return
     */
    public Subnet Subnetgetone(OSClient os,String subnetId){
        Subnet subnet = null;
        try {
            subnet = getSubnet(os).get(subnetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subnet;

    }

    /**
     * 根据ID删除子网
     * @param os
     * @param subnetId
     * @return
     */
    public int SubnetDelete(OSClient os,String subnetId){
        int flag = 0;
        try {
            flag = getSubnet(os).delete(subnetId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }

    /**
     * 创建一个新的端口
     * @param os
     * @param name
     * @param networkid
     * @param fixedip
     * @param subnetid
     * @return
     */
    public Port PortCreate(OSClient os,String name,String networkid,String fixedip,String subnetid){
        Port port = null;
        try {
            port = getPort(os).create(Builders.port()
                                            .name(name)
            .networkId(networkid)
            .fixedIp(fixedip,subnetid)
            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * 查询所有的端口
     * @param os
     * @return
     */
    public List<? extends Port> PortGetAll(OSClient os){
        List<? extends Port> ports = null;
        try {
            ports = getPort(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ports;
    }

    /**
     * 查询单个id
     * @param os
     * @param portId
     * @return
     */
    public Port PortGetOne(OSClient os,String portId){
        Port port = null;
        try {
            port = getPort(os).get(portId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return port;
    }

    /**
     * 根据ID删除端口
     * @param os
     * @param portId
     * @return
     */
    public int PortDelete(OSClient os,String portId){
       int flag = 0;
        try {
            flag = getPort(os).delete(portId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

   /* 路由*/

    /**
     * 新建一个虚拟路由器
     * @param os
     * @param name
     * @param flag 是否是管理员状态
     * @param networkId 外部网络IP
     * @param destination
     * @param nexthop
     * @return
     */
    public Router RouterCreate(OSClient os,String name,boolean flag,String networkId,String destination, String nexthop){

        Router router = null;
        try {
            router = getRouter(os).create(Builders.router()
            .name(name)
            .adminStateUp(flag)
            .externalGateway(networkId)
            .route(destination,nexthop)
            .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return router;
    }

    /**
     * 更改路由器的管理员状态
     * @param os
     * @param routerId
     * @param flag
     * @return
     */
     public Router RouterAdminToggle(OSClient os,String routerId,boolean flag){
         Router router = null;
         try {
             router = getRouter(os).toggleAdminStateUp(routerId,flag);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return router;
     }

    /**
     * 为路由器增加路由端口
     * @param os
     * @param routerId
     * @param type
     * @param subnetId
     * @return
     */
    public RouterInterface RouterAttaching(OSClient os,String routerId,AttachInterfaceType type,String subnetId){
        RouterInterface iface = null;
        try {
            iface = getRouter(os).attachInterface(routerId,type,subnetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iface;
    }

    /**
     * 取消路由
     * @param os
     * @param routerId
     * @param subnetId
     * @return
     */
    public RouterInterface RouterDetach(OSClient os,String routerId,String subnetId){
        RouterInterface iface = null;
        try {
            iface = getRouter(os).detachInterface(routerId,subnetId,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iface;
    }

    /**
     * 删除路由
     * @param os
     * @param routerid
     * @return
     */
    public int RouterDelete(OSClient os,String routerid){
        int flag = 0;
        try {
            flag = getRouter(os).delete(routerid).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询出所有的路由
     * @param os
     * @return
     */
    public List<? extends Router> RouterGetAll(OSClient os){
        List<? extends  Router> routers = null;
        try {
            routers = getRouter(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routers;
    }

    /**
     * 查询单个路由
     * @param os
     * @param RouterId
     * @return
     */
    public Router RouterGetOne(OSClient os,String RouterId){
        Router router = null;
        try {
            router = getRouter(os).get(RouterId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return router;

    }

}


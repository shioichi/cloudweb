package com.cpj.openstack;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.storage.ObjectStorageContainerService;
import org.openstack4j.api.storage.ObjectStorageObjectService;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.storage.object.SwiftContainer;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.model.storage.object.options.ContainerListOptions;
import org.openstack4j.model.storage.object.options.ObjectListOptions;
import org.openstack4j.model.storage.object.options.ObjectPutOptions;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by chenpengjiang on 2016/3/21.
 */
public class Swift {
    private ObjectStorageContainerService getContainer(OSClient os){return os.objectStorage().containers();}
    private ObjectStorageObjectService getObject(OSClient os){return os.objectStorage().objects();}

    /**
     * 查询所有的容器
     * @param os
     * @return
     */
    public List<? extends SwiftContainer> ContainersGetAll(OSClient os){
        List<? extends SwiftContainer> containers = null;
        try {
            containers = getContainer(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return containers;
    }

    /**
     * 条件查询
     * @param os
     * @param startWith 首字母
     * @param limit 数量
     * @return
     */
    public List<? extends SwiftContainer> ContainersGetAdvanced(OSClient os,String startWith,int limit){
        List<? extends SwiftContainer> containers = null;
        try {
            containers = getContainer(os).list(ContainerListOptions.create()
                    .startsWith(startWith)
                    .limit(limit)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return containers;
    }

    /**
     * 创建一个容器
     * @param os
     * @param name 容器名
     * @return
     */
    public int ContainerCreate(OSClient os,String name){
        int flag = 0;
        try {
            flag = getContainer(os).create(name).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除一个容器，容器内不得有文件，否则删除失败
     * @param os
     * @param containername
     * @return
     */
    public int ContainerDelete( OSClient os,String containername){
        int flag = 0;
        try {
            flag = getContainer(os).delete(containername).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 创建多级目录，在容器下
     * @param os
     * @param containername
     * @param path
     * @return
     */
    public String ContainerCreateNext(OSClient os,String containername,String path){
        String  flag = null;
        try {
            flag = getContainer(os).createPath(containername,path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /*MetaData*/

    /**
     * 取当前容器的元数据，返回map
     * @param os
     * @param Containername
     * @return
     */
    public Map<String,String> ContainerDataGet(OSClient os,String Containername){
        Map<String,String> md = null;
        try {
            md = getContainer(os).getMetadata(Containername);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md;
    }

    /**
     * 新增或更新元数据
     * @param os
     * @param containername
     * @param map
     * @return
     */
    public boolean ContainerDataSaveorUpdate(OSClient os,String containername,Map<String,String> map){
        boolean flag = false;
        try {
            flag = getContainer(os).updateMetadata(containername,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除或更新元数据
     * @param os
     * @param containername
     * @param map
     * @return
     */
    public boolean ContainerDataDelete(OSClient os,String containername,Map<String,String> map){
        boolean flag = false;
        try {
            flag = getContainer(os).deleteMetadata(containername,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /*Object*/

    /**
     * 查询容器下的所有对象
     * @param os
     * @param containerName
     * @return
     */
    public List<? extends SwiftObject> ObjectGetAll(OSClient os,String containerName){
        List<? extends SwiftObject> objects = null;
        try {
            objects = getObject(os).list(containerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * 查询路径下的object
     * @param os
     * @param containerName
     * @param path
     * @return
     */
    public List<? extends SwiftObject> ObjectGetAdvanced(OSClient os,String containerName,String path){
        List<? extends SwiftObject> objects = null;
        try {
            objects = getObject(os).list(containerName, ObjectListOptions.create().path(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    /**
     * 上传文件到容器内
     * @param os
     * @param containername
     * @param objectname
     * @param filepath 文件路径
     * @param savepath 保存路径
     * @param map
     * @return
     */
    public String Objectupload(OSClient os,String containername,String objectname,String filepath,String savepath,Map<String,String> map){
        Payload<File> payload = Payloads.create(new File(filepath));
        String etag = null;
        try {
            etag = getObject(os).put(containername,objectname,payload, ObjectPutOptions.create().path(savepath).metadata(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return etag;
    }

    /**
     * 删除文件
     * @param os
     * @param container
     * @param objectName
     * @return
     */
    public int ObjectDelete(OSClient os,String container,String objectName){
        int flag = 0;
        try {
            flag = getObject(os).delete(container,objectName).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查看文件的信息
     * @param os
     * @param containername
     * @param objectname
     * @return
     */
    public Map<String,String> ObjectGetData(OSClient os,String containername,String objectname){
        Map<String,String> md = null;
        try {
            md = getObject(os).getMetadata(containername,objectname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md;
    }

}

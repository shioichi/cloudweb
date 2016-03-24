package com.cpj.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;

import java.io.File;
import java.util.List;

/**
 * Created by chenpengjiang on 2016/3/21.
 */
public class Images {
    private ImageService getImage(OSClient os){return os.images();}

    /**
     * 创建并上传一个新的镜像
     * @param os
     * @param name
     * @param filepath
     * @param ispublic
     * @param imagetype
     * @param disktype
     * @return
     */
    public Image ImageCreate(OSClient os, String name, String filepath, boolean ispublic, ContainerFormat imagetype, DiskFormat disktype){
        Image image = null;
        try {
            Payload<File> payload = Payloads.create(new File(filepath));
            image = getImage(os).create(Builders.image()
            .name(name)
            .isPublic(ispublic)
            .containerFormat(imagetype)
            .diskFormat(disktype)
            .build(),payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 根据imageid删除
     * @param os
     * @param imageId
     * @return
     */
    public int ImageDelete(OSClient os,String imageId){
        int flag = 0;
        try {
            flag = getImage(os).delete(imageId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 得到全部的镜像
     * @param os
     * @return
     */
    public List<? extends Image> ImageGetAll(OSClient os){
        List<? extends Image> images = null;
        try {
            images = getImage(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    /**
     * 得到一个image详细
     * @param os
     * @param imageId
     * @return
     */
    public Image ImageGetOne(OSClient os,String imageId){
        Image image = null;
        try {
            image = getImage(os).get(imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 返回所有可以使用该镜像的tenant名单
     * @param os
     * @param imageId
     * @return
     */
    public List<? extends ImageMember> ImageGetMember(OSClient os,String imageId){
        List<? extends ImageMember> members= null;
        try {
            members = getImage(os).listMembers(imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }

    /**
     * 为一个镜像添加权限
     * @param os
     * @param imageId
     * @param tenantId
     * @return
     */
    public boolean ImageAddMember(OSClient os,String imageId,String tenantId){
        boolean flag = false;
        try {
            flag = getImage(os).addMember(imageId,tenantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 为当前镜像删除一个权限
     * @param os
     * @param imageId
     * @param tentanId
     * @return
     */
    public boolean ImageDelMember(OSClient os,String imageId,String tentanId){
        boolean flag = false;
        try {
            flag = getImage(os).removeMember(imageId,tentanId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}


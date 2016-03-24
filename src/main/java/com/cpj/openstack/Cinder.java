package com.cpj.openstack;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.storage.BlockVolumeService;
import org.openstack4j.api.storage.BlockVolumeSnapshotService;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;

import java.util.List;

/**
 * Created by chenpengjiang on 2016/3/21.
 */
public class Cinder {
    private BlockVolumeService getBlock(OSClient os){return os.blockStorage().volumes();}
    private BlockVolumeSnapshotService getSnapshot(OSClient os){return os.blockStorage().snapshots();}

    /**
     * 查询所有硬盘的类型
     * @param os
     * @return
     */
    public List<? extends VolumeType> BlockAllTypes(OSClient os){
        List<? extends VolumeType> types = null;
        try {
            types = getBlock(os).listVolumeTypes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }

    /**
     * 查询当前工程下所有可用的硬盘
     * @param os
     * @return
     */
    public List<? extends Volume> BlockGetAll(OSClient os){
        List<? extends Volume> volumes = null;
        try {
            volumes = getBlock(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  volumes;
    }

    /**
     * 查询特定的硬盘的信息
     * @param os
     * @param volumeId
     * @return
     */
    public Volume BlockGetOne(OSClient os,String volumeId){
        Volume volume = null;
        try {
            volume = getBlock(os).get(volumeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volume;
    }

    /**
     * 新建一个硬盘
     * @param os
     * @param name
     * @param dec
     * @param size
     * @return
     */
    public Volume BlockCreate(OSClient os,String name,String dec,int size){
        Volume volume = null;
        try {
            volume = getBlock(os).create(Builders.volume()
                    .name(name)
                    .description(dec)
                    .size(size)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volume;
    }

    /**
     * 创建一个启动盘，将镜像放置在cinder上启动，可提高启动速度
     * @param os
     * @param name
     * @param dec
     * @param imageId
     * @param bootable
     * @return
     */
    public Volume BlockBootCreate(OSClient os,String name,String dec,String imageId,boolean bootable){
        Volume volume = null;
        try {
            volume = getBlock(os).create(Builders.volume()
                    .name(name)
                    .description(dec)
                    .imageRef(imageId)
                    .bootable(bootable)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return volume;
    }

    /**
     * 更新硬盘信息
     * @param os
     * @param volumeId
     * @param name
     * @param des
     * @return
     */
    public int BlockUpdate(OSClient os,String volumeId,String name,String des){
        int flag = 0;
        if(name.equals("")||name==null){
            name = getBlock(os).get(volumeId).getName();
        }
        try {
            flag = getBlock(os).update(volumeId,name,des).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除银盘
     * @param os
     * @param volumeId
     * @return
     */
    public int BlockDelete(OSClient os,String volumeId){
        int flag = 0;
        try {
            flag = getBlock(os).delete(volumeId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有快照
     * @param os
     * @return
     */
    public List<? extends VolumeSnapshot> SnapshotGetAll(OSClient os){
        List<? extends VolumeSnapshot> snapshots = null;
        try {
            snapshots = getSnapshot(os).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snapshots;
    }

    /**
     * 查询某个快照
     * @param os
     * @param snapshotId
     * @return
     */
    public VolumeSnapshot SnapshotGetOne(OSClient os,String snapshotId){
        VolumeSnapshot snapshot = null;
        try {
            snapshot = getSnapshot(os).get(snapshotId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snapshot;
    }

    /**
     * 创建硬盘的快照
     * @param os
     * @param name
     * @param des
     * @param volumeId
     * @return
     */
    public VolumeSnapshot SnapshotCreate(OSClient os,String name,String des,String volumeId){
        VolumeSnapshot snapshot = null;
        try {
            snapshot = getSnapshot(os).create(
                   Builders.volumeSnapshot()
                    .name(name)
                    .description(des)
                    .volume(volumeId)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snapshot;
    }

    /**
     * 删除一个快照
     * @param snapshotId
     * @return
     */
    public int SnapshotDelete(OSClient os,String snapshotId){
        int flag = 0;
        try {
            flag = getSnapshot(os).delete(snapshotId).getCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  flag;
    }
}

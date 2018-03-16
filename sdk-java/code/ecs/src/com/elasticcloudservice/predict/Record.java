package com.elasticcloudservice.predict;

/**
 * 历史虚拟机申请记录对象
 */
public class Record {
    String fid;
    String flavorName;
    String createTime;

    public Record(String fid, String flavorName, String createTime) {
        this.fid = fid;
        this.flavorName = flavorName;
        this.createTime = createTime;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFlavorName() {
        return flavorName;
    }

    public void setFlavorName(String flavorName) {
        this.flavorName = flavorName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

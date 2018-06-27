package com.xietong.demo.eaas.facade.dto;

import com.xietong.demo.eaas.domain.SessionUser;

/**
 * Created by Administrator on 2016/6/22.
 */
public class SessionUserDTO {
    private String id;
    private String nickName;//昵称别名
    private String email;//电子邮件
    private String mobileNo;//手机号
    private String avatarUrl;//头像镜像地址  ／＊作为静态文件存储＊／
    private Boolean isAnonymous=false;//是否为匿名用户标志
    SessionUser.Status status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public SessionUser.Status getStatus() {
        return status;
    }

    public void setStatus(SessionUser.Status status) {
        this.status = status;
    }
}

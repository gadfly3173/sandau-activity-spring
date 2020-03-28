package vip.gadfly.sandauactivity.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@DynamicUpdate
public class UserInfo {

    @Id
    private String id;

    private String openid;

    private String nickname;

    private String accessLevel;

    private Long createTime;

    private Long updateTime;

    private String avatarUrl;

    private String stuNum;

    private String name;

    private Long phone;

    @OneToMany(mappedBy = "userInfo",cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Activity> activity;

    public UserInfo() {

    }

    public UserInfo(String id, String openid, String nickname, String avatarUrl, Long createTime) {
        this.id = id;
        this.openid = openid;
        this.nickname = nickname;
        this.createTime = createTime;
        this.avatarUrl = avatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}

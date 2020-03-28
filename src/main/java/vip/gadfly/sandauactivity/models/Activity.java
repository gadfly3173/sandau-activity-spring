package vip.gadfly.sandauactivity.models;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Activity {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @NotNull
    private String title;

    @NotNull
    private String brief;

    @Lob
    @NotNull
    @Column(columnDefinition="MEDIUMTEXT")
    private String content;

    private String img;

    private Integer registered = 0;

    @NotNull
    private Integer maxMembers;

    @NotNull
    private String location;

    @NotNull
    private Long regStartTime;

    @NotNull
    private Long regEndTime;

    @NotNull
    private Long actStartTime;

    @NotNull
    private Long actEndTime;

    private Long createTime;

    private Long updateTime;

    @Version
    private int version;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name = "category_id")
    private Categories categories;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    public Activity() {

    }

    public Activity(String title, String brief, String content, String img,
                    Integer maxMembers, Long createTime, Categories categories, UserInfo userInfo) {
        this.title = title;
        this.brief = brief;
        this.content = content;
        this.img = img;
        this.registered = 0;
        this.maxMembers = maxMembers;
        this.createTime = createTime;
        this.categories = categories;
        this.userInfo = userInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRegistered() {
        return registered;
    }

    public void setRegistered(Integer registered) {
        this.registered = registered;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(Long regStartTime) {
        this.regStartTime = regStartTime;
    }

    public Long getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(Long regEndTime) {
        this.regEndTime = regEndTime;
    }

    public Long getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(Long actStartTime) {
        this.actStartTime = actStartTime;
    }

    public Long getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Long actEndTime) {
        this.actEndTime = actEndTime;
    }

    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

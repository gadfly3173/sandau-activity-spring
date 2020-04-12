package vip.gadfly.sandauactivity.models;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DynamicUpdate
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SignUp {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    @NotNull
    private String contact;

    @NotNull
    private String status = "SIGN";

    @NotNull
    private Long createTime;

    private Long updateTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;


    public SignUp() {
    }

    public SignUp(String contact, Long createTime, Activity activity, UserInfo userInfo) {
        this.contact = contact;
        this.createTime = createTime;
        this.activity = activity;
        this.userInfo = userInfo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

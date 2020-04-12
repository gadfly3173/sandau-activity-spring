package vip.gadfly.sandauactivity.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String category;

    private Long createTime;

    private Long updateTime;

    public Categories() {

    }

    public Categories(Integer id) {
        this.id = id;
    }

    public Categories(String category, Long createTime) {
        this.category = category;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}

package com.forsrc.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.forsrc.lucene.IndexWhenPublishedInterceptor;
import com.forsrc.utils.JsonSerializerDate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.search.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.Id;
import javax.xml.bind.annotation.*;
import java.util.Date;


@Indexed(index = "MyStudy.User", interceptor = IndexWhenPublishedInterceptor.class)
@Analyzer(impl = IKAnalyzer.class, definition = "cn")
//@Analyzer(impl = SmartChineseAnalyzer.class)

@org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler", "password"})
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler", "password"})
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
@DynamicUpdate(true)
@DynamicInsert(true)
@SelectBeforeUpdate(true)
public class User implements java.io.Serializable {


    // Fields
    @DocumentId
    @Id
    //@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @XmlElement(name = "id")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonSerialize(using = JsonSerializerTimestamp.class)
    @JsonSerialize(using = JsonSerializerDate.class)
    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Date createOn;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = JsonSerializerDate.class)
    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Date updateOn;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    private String username;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    private String email;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    @XmlTransient
    private String password;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Integer status; // 0: delete; 1: OK; 2: NG

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Boolean isAdmin;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private String image;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Integer version;

    // Constructors

    /**
     * default constructor
     */
    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public void setAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
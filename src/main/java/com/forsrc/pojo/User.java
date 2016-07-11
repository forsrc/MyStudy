package com.forsrc.pojo;

import com.forsrc.lucene.IndexWhenPublishedInterceptor;
import org.hibernate.search.annotations.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Indexed(index = "MyStudy.User", interceptor = IndexWhenPublishedInterceptor.class)
@Analyzer(impl = IKAnalyzer.class, definition = "cn")
//@Analyzer(impl = SmartChineseAnalyzer.class)

@org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler"})
@XmlRootElement(name = "User")
public class User implements java.io.Serializable {


    // Fields    
    @DocumentId
    @Id
    //@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    private Long id;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Date updateOn;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Date createOn;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    private String username;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    private String email;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.NO)
    private String password;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private int status; // 0: delete; 1: OK; 2: NG

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private boolean isAdmin;

    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private String image;

    // Constructors

    /**
     * default constructor
     */
    public User() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
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
}
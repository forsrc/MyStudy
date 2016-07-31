package com.forsrc.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.forsrc.lucene.IndexWhenPublishedInterceptor;
import com.forsrc.utils.JsonSerializerDate;
import com.forsrc.utils.XmlAdapterDate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.search.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.persistence.Id;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;


/**
 * The type User.
 */
@Indexed(index = "MyStudy.User", interceptor = IndexWhenPublishedInterceptor.class)
@Analyzer(impl = IKAnalyzer.class, definition = "cn")
//@Analyzer(impl = SmartChineseAnalyzer.class)

@org.codehaus.jackson.annotate.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler", "password"})
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "fieldHandler", "password"})
@XmlRootElement(name = "User")
@XmlAccessorType(XmlAccessType.FIELD)
@DynamicUpdate(true)
@DynamicInsert(true)
@SelectBeforeUpdate(false)
public class User implements java.io.Serializable {


    // Fields
    @DocumentId
    @Id
    //@Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    @SortableField
    @XmlElement(name = "id")
    private Long id;

    @XmlJavaTypeAdapter(XmlAdapterDate.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonSerialize(using = JsonSerializerTimestamp.class)
    @JsonSerialize(using = JsonSerializerDate.class)
    @Field(index = Index.NO, analyze = Analyze.NO, store = Store.YES)
    private Date createOn;


    @XmlJavaTypeAdapter(XmlAdapterDate.class)
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

    /**
     * Instantiates a new User.
     *
     * @param id the id
     */
    public User(Long id) {
        this.id = id;
    }

    // Property accessors

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public Boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Gets update on.
     *
     * @return the update on
     */
    public Date getUpdateOn() {
        return updateOn;
    }

    /**
     * Sets update on.
     *
     * @param updateOn the update on
     */
    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    /**
     * Sets admin.
     *
     * @param admin the admin
     */
    public void setAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    /**
     * Gets is admin.
     *
     * @return the is admin
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets is admin.
     *
     * @param admin the admin
     */
    public void setIsAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets create on.
     *
     * @return the create on
     */
    public Date getCreateOn() {
        return createOn;
    }

    /**
     * Sets create on.
     *
     * @param createOn the create on
     */
    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(int version) {
        this.version = version;
    }
}
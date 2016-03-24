package com.cpj.pojo;

import com.cpj.pojo.base.GeneralPojo;

import javax.persistence.*;

/**
 * Created by chenpengjiang on 2016/3/7.
 */
@Entity
@Table(name = "login", schema = "logintest")
public class User extends GeneralPojo {
    private static final long serialVersionUID = 100000000L;
    private String userId;
    private String userName;
    private String password;
    private int identity;

    @Id
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    @Basic
    @Column(name = "identity")
    public int getIdentity() {return identity;}

    public void setIdentity(int identity) {this.identity = identity;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}

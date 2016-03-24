//package com.cpj.dao.impl;
//
//import com.cpj.dao.UserDao;
//import com.cpj.pojo.User;
//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * Created by chenpengjiang on 2016/3/7.
// */
//@Repository
//public class UserDaoImpl implements UserDao {
//    @Resource
//    private SessionFactory sessionFactory;
//
//    public Session getSession(){
//        return sessionFactory.getCurrentSession();
//    }
//    public User findByUserName(String userName){
//        User user = null;
//        Query query = getSession().createQuery("from User as u where u.userName=?");
//        query.setString(0, userName);
//        List<User> users = query.list();
//        if (users.size() > 0) {
//            return users.get(0);
//        }
//        return user;
//    }
//}

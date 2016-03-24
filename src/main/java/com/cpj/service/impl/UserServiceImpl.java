package com.cpj.service.impl;

import com.cpj.dao.IBaseDao;
import com.cpj.pojo.User;
import com.cpj.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by chenpengjiang on 2016/3/7.
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    IBaseDao<User> iBaseDao;
//    public User findByUserName(String userName){
//        return userDao.findByUserName(userName);
//    }
    public User findByUserName2(String userName){
        return iBaseDao.getByHQL("from User as u where u.userName = ?",userName);
    }

    /**
     * 判断用户名和密码
     * @param userName
     * @param password
     * @return 200通过 0用户不存在 1密码错误
     */
    public int logincheck(String userName,String password){
        int flag;
        User user = iBaseDao.getByHQL("from User as u where u.userName = ?",userName);
        if(user==null){
            flag = 0;
        }else if(user.getPassword().equals(password)){
            flag = 200 + user.getIdentity();
        }else{
            flag = 1;
        }
        return flag;
    }

}

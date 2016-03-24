package com.cpj.service;

/**
 * Created by chenpengjiang on 2016/3/7.
 */
public interface UserService<User> {
  //  public User findByUserName(String userName);
    public User findByUserName2(String userName);

  /**
   * 判断用户登陆信息
   * @param userName
   * @param password
   * @return 200成功 0 用户不存在 1 密码错误
     */
    public int logincheck(String userName,String password);
}

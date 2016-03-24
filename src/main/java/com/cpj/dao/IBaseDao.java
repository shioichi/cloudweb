package com.cpj.dao;

import com.cpj.pojo.base.PageResults;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用dao接口操作
 * Created by chenpengjiang on 2016/3/8.
 */
public interface IBaseDao<T extends Serializable> {
    /**
     * 增加或更新实体
     * @param entity 实体对象
     */
    public abstract void saveOrUpdate(T entity);

    /**
     * 增加或更新实体
     *
     * @param entity
     *            实体对象
     * @return entity
     */
    public abstract T saveOrUpdateReturnObj(T entity);

    /**
     * 对于指定类型，按主键取记录
     * @param id 主键值
     * @param clazz 指定类型
     * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
     */
    public abstract T get(long id, Class<?> clazz);

    /**
     *
     * @param id 主键值
     * @param clazz 指定类型
     * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
     */
    public abstract T load(long id, Class<?> clazz);


    /**
     * <contains>
     * @param t 实体
     * @return 是否包含
     */
    public abstract boolean contains(T t);

    /**
     * 返回所有记录
     * @param clazz
     * @return
     */
    public abstract List<T> findAll(Class<?> clazz,  Map<String, Object> params) throws Exception;

    /**
     * 删除指定的实体
     * @param clazz 指定类型
     */
    public abstract boolean delete(Class<?> clazz, String ids);
    /**
     * 执行Hql语句
     * @param hqlString hql
     * @param values 不定参数数组
     */
    public abstract void excuteHql(String hqlString, Object... values);

    /**
     * 执行Sql语句
     * @param sqlString sql
     * @param values 不定参数数组
     */
    public abstract void excuteSql(String sqlString, Object... values);

    /**
     * 根据HQL语句查找唯一实体
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     */
    public abstract T getByHQL(String hqlString, Object... values);

    /**
     * 根据SQL语句查找唯一实体
     * @param sqlString SQL语句
     * @param values 不定参数的Object数组
     * @return 查询实体
     */
    public abstract T getBySQL(String sqlString, Object... values);

    /**
     * 根据HQL语句，得到对应的list
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     */
    public abstract List<T> getListByHQL(String hqlString, Object... values);

    /**
     * 根据SQL语句，得到对应的list
     * @param sqlString HQL语句
     * @param values 不定参数的Object数组
     * @return 查询多个实体的List集合
     */
    public abstract List<T> getListBySQL(String sqlString, Object... values);

    /**
     * 根据SQL语句，返回map
     * @param sqlString
     * @param values
     * @return
     */
    public abstract List<Map<String,Object>> getListBySQLMap(String sqlString, Object... values);


    /**
     * 根据HQL得到记录数
     * @param hql HQL语句
     * @param values 不定参数的Object数组
     * @return 记录总数
     */
    public abstract Long countByHql(String hql, Object... values);

    /**
     * 分页查询
     * @param entity
     * @param pageNo
     * @param pageSize
     * @param map    查询条件
     * @return
     * @throws Exception
     */
    public abstract PageResults<T> findPageByFetched(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> params)  throws Exception;

    /**
     * hql语句
     * @param hqlString
     * @param values
     * @return
     */
    public abstract int updateByHQL(String hqlString, Object[] values);

    /**
     * sql语句
     * @param sqlString
     * @param values
     */
    public abstract void operateBySQL(String sqlString, Object[] values);
}

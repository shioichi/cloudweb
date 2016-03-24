package com.cpj.dao.impl;

import com.cpj.dao.IBaseDao;
import com.cpj.pojo.base.PageResults;
import com.sun.deploy.util.GeneralUtil;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * Created by chenpengjiang on 2016/3/8.
 */
@SuppressWarnings("unchecked")
@Repository(value="userDao")
public class BaseDaoImpl<T extends Serializable> implements IBaseDao<T> {
    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private SessionFactory sessionFactory;

    public Session getSession()
    {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     *  增加或更新实体
     * @param entity 实体
     */
    public void saveOrUpdate(T entity) {
        Session session = getSession();
        session.saveOrUpdate(entity);
        session.flush();
    }

    /**
     * 增加或更新实体并返回对象
     * @param entity  实体
     * @return
     */
    public T  saveOrUpdateReturnObj(T entity) {
        Session session = getSession();
        session.saveOrUpdate(entity);
        session.flush();
        return entity;
    }

    /**
     * 是否包含
     * @param t
     * @return
     */
    public boolean contains(T t) {
        return this.getSession().contains(t);
    }

    /**
     * 带参数的更新
     * @param hqlString hql
     * @param values 不定参数数组
     */
    public void excuteHql(String hqlString, Object... values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();

    }

    /**
     * 带参数的更新
     * @param sqlString sql
     * @param values 不定参数数组
     */
    public void excuteSql(String sqlString, Object... values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
    }

    /**
     *查询结果唯一的hql查询
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return
     */
    public T getByHQL(String hqlString, Object... values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    /**
     * 查询结果唯一的sql查询
     * @param sqlString SQL语句
     * @param values 不定参数的Object数组
     * @return
     */
    public T getBySQL(String sqlString, Object... values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (T) query.uniqueResult();
    }

    /**
     * 返回一个序列的hql查询
     * @param hqlString HQL语句
     * @param values 不定参数的Object数组
     * @return
     */
    public List<T> getListByHQL(String hqlString, Object... values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    /**
     * sql语句，返回一个集合
     * @param sqlString sqL语句
     * @param values 不定参数的Object数组
     * @return
     */
    public List<T> getListBySQL(String sqlString, Object... values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }
    /**
     * sql语句，返回一个map集合
     * @param sqlString sqL语句
     * @param values 不定参数的Object数组
     * @return
     */
    public List<Map<String, Object>> getListBySQLMap(String sqlString, Object... values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }

        // 结果集返回map
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    /**
     * 查询数量
     * @param hql HQL语句 "select count(tb) from table tb"
     * @param values 不定参数的Object数组
     * @return
     */
    public Long countByHql(String hql, Object... values) {
        Query query = this.getSession().createQuery(hql);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        return (Long) query.uniqueResult();
    }

    /**
     * 不要用这个方法，上面有同样功能的
     * @param hqlString
     * @param values
     * @return
     */
    public int updateByHQL(String hqlString, Object[] values) {
        Query query = this.getSession().createQuery(hqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
        return 1;
    }

    /**
     * sql对数据库操作，同样不推荐使用
     * @param sqlString
     * @param values
     */
    public void operateBySQL(String sqlString, Object[] values) {
        Query query = this.getSession().createSQLQuery(sqlString);
        if (values != null)
        {
            for (int i = 0; i < values.length; i++)
            {
                query.setParameter(i, values[i]);
            }
        }
        query.executeUpdate();
    }

    /**
     * 分页条件查询
     * @param clazz
     * @param pageNo
     * @param pageSize
     * @param params
     * @return
     * @throws Exception
     */
    public PageResults<T> findPageByFetched(Class<?> clazz, int pageNo, int pageSize, Map<String, Object> params) throws Exception {
        PageResults<T> retValue = new PageResults<T>();
        String hql = "from " + clazz.getSimpleName();
        String countHql = "select count(*) from " + clazz.getSimpleName();
        // 获取查询条件
//        String condition = GeneralUtil.getHqlCondition(clazz, params);
        String condition = "";
        Query query = this.getSession().createQuery(hql + condition);
        Long count = countByHql(countHql + condition);

        retValue.setTotalCount(count.intValue());
        retValue.setPageSize(pageSize);
        retValue.resetPageNo();
        int currentPage = pageNo > 1 ? pageNo : 1;
        retValue.setCurrentPage(currentPage);

        List<T> itemList = query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
        retValue.setResults(itemList);

        return retValue;
    }

    /**
     * 根据id删除对象
     * @param clazz 指定类型
     * @param ids
     * @return
     */
    public boolean delete(Class<?> clazz, String ids) {
        String hql = "delete from " + clazz.getSimpleName() + " as entity where id in (" + ids + ")";
        log.debug("删除实体hql：" + hql);
        Query query = this.getSession().createQuery(hql);
        try {
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     *查询出所有的记录
     * @param clazz
     * @param params
     * @return
     * @throws Exception
     */
    public List<T> findAll(Class<?> clazz, Map<String, Object> params) throws Exception {
        String hql = "from " + clazz.getSimpleName();
        // 获取查询条件
//        String condition = GeneralUtil.getHqlCondition(clazz, params);
        String condition = "";
        Query query = this.getSession().createQuery(hql + condition);
        return query.list();
    }

    /**
     * 查询某一条数据，load方法认为该数据在数据库中一定存在，可以放心的使用代理来延迟加载，如果在使用过程中发现了问题，只能抛异常
     * @param id 主键值
     * @param clazz 指定类型
     * @return
     */
    public T load(long id, Class<?> clazz) {
        return (T) getSession().load(clazz, id);
    }
    /**
     * 查询某一条数据,get方法，hibernate一定要获取到真实的数据，否则返回null。
     * @param id 主键值
     * @param clazz 指定类型
     * @return
     */
    public T get(long id, Class clazz) {
        return (T) getSession().get(clazz, id);
    }
}

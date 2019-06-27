package com.bosssoft.gp.demo.service.api.redis;


import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author smile7up
 * @createDate 2019-06-27
 * @Description redis接口
 *
 */
public interface IRedisService {

    /**
     * 根据Key值批量删除Values
     *
     * @param keys
     * @return
     */
    Long removeValuesByBatch(final String... keys);

    /**
     * 批量删除Values
     *
     * @param pattern
     * @return
     */
    Long removeValuesByPattern(final String pattern);


    /**
     * 根据Key值删除Value
     *
     * @param key
     * @return
     */
    Long removeValuesBySingle(final String key);


    /**
     * 判断Key是否存在
     *
     * @param key
     * @return
     */
    boolean isExists(final String key);

    /**
     * 清空所有key
     *
     * @return
     * @
     */
    Boolean flushAll();

    /**
     * 存入Redis
     *
     * @param key 唯一键
     * @param t   对象
     * @
     */
    <T> void setObj(String key, T t);

    /**
     * 根据redis的key获取相关的值
     *
     * @param key
     * @param c
     * @param <T>
     * @return
     * @
     */
    <T> T getObj(String key, Class<T> c);

    /**
     * 给redis 赋值List
     *
     * @param key
     * @param i
     * @param <T>
     * @
     */
    <T> void setList(String key, List<T> i);

    /**
     * 给redis 取List
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @
     */
    <T> List<T> getList(String key, Class<T> t);

    /**
     * 根据Key值读取缓存
     *
     * @param key
     * @return
     * @
     */
    Object get(String key);

    /**
     * 根据Key存放缓存
     *
     * @param key
     * @param value
     * @param expireTime
     * @return
     * @
     */
    boolean set(String key, Object value, Long expireTime);

    /**
     * 根据Key存放缓存
     *
     * @param key
     * @param value
     * @return
     * @
     */
    boolean set(String key, Object value);

    /**
     * 根据Key值获取数据
     *
     * @param key
     * @return
     * @
     */
    byte[] get(byte[] key);

    /**
     * 增加数据方法
     *
     * @param key
     * @param value
     * @return
     * @
     */
    Boolean set(byte[] key, byte[] value);

    /**
     * 增加数据方法
     *
     * @param key
     * @param value
     * @param expire
     * @return
     * @
     */
    Boolean set(byte[] key, byte[] value, Long expire);

    /**
     * 删除数据
     *
     * @param key
     * @return
     */
    Boolean del(byte[] key);

    /**
     * 删除数据
     *
     * @param key
     * @return
     */
    Boolean del(String key);

    /**
     * 根据通配符进行查询
     *
     * @param pattern
     * @return
     */
    Set<byte[]> keys(String pattern);

    /**
     * 将list 放入redis中
     *
     * @param key   list 的key
     * @param value
     */
    void lPushList(String key, String value);


    /**
     * 查询列表结构
     *
     * @param key   主键
     * @param start 起始
     * @param end   结束
     * @return
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 删除list中的数据
     *
     * @param key   list的key
     * @param value list中的值
     */
    void lRemList(String key, String value);

    /**
     * 设置key 的过期时间
     *
     * @param key           主键
     * @param expireSeconds 过期时间
     */
    void expireKey(String key, long expireSeconds);

    /**
     * as
     *
     * @param key 主键
     * @param timeUnit 时间格式
     * @return
     */
    Long getEffectiveDuration(String key, TimeUnit timeUnit);
}

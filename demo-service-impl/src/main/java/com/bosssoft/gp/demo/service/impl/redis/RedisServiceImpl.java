package com.bosssoft.gp.demo.service.impl.redis;

import com.bosssoft.gp.demo.service.api.redis.IRedisService;
import com.bosssoft.gp.framework.exception.system.GpOtherExceptionCause;
import com.bosssoft.gp.framework.exception.system.GpSystemException;
import com.bosssoft.gp.framework.util.serialization.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author cjh
 * @description redis操作实现类
 * @createDate 2019-02-22
 */
@Service
public class RedisServiceImpl implements IRedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Long removeValuesByBatch(String... keys) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long result = 0;
            for (int i = 0; i < keys.length; i++) {
                try {
                    result = connection.del(keys[i].getBytes("UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
                }
            }
            return result;
        });
    }

    @Override
    public Long removeValuesByPattern(String pattern) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long result = 0;
            Set<byte[]> keys = null;
            try {
                keys = connection.keys(pattern.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
            for (byte[] key : keys) {
                result += connection.del(key);
            }
            return result;
        });
    }

    @Override
    public Long removeValuesBySingle(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            try {
                return connection.del(key.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });
    }

    @Override
    public boolean isExists(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                return connection.exists(key.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });
    }

    @Override
    public Boolean flushAll() {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.flushAll();
            return true;
        });
    }

    @Override
    public <T> void setObj(String key, T t) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                connection.set(key.getBytes("UTF-8"), SerializationUtil.serialize(t));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
            return null;
        });
    }

    @Override
    public <T> T getObj(String key, Class<T> c) {
        return redisTemplate.execute((RedisCallback<T>) connection -> {
            try {
                byte[] bytes = connection.get(key.getBytes("UTF-8"));
                return (T) SerializationUtil.deserialize(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });
    }

    @Override
    public <T> void setList(String key, List<T> i) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                logger.debug("插入List数据");
                connection.set(key.getBytes("UTF-8"), SerializationUtil.serialize(i));
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "插入列表失败");
            }
            return null;
        });
    }


    @Override
    public <T> List<T> getList(String key, Class<T> t) {
        return redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            try {
                byte[] bytes = connection.get(key.getBytes("UTF-8"));
                return (List<T>) SerializationUtil.deserialize(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });
    }

    @Override
    public Object get(String key) {
        return redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                byte[] bytes = connection.get(key.getBytes("UTF-8"));
                return SerializationUtil.deserialize(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });

    }

    @Override
    public boolean set(String key, Object value, Long expireTime) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                if (expireTime == null) {
                    connection.set(key.getBytes("UTF-8"), SerializationUtil.serialize(value));
                    return true;
                } else if (expireTime != null && expireTime > 0) {
                    Expiration expiration = Expiration.from(expireTime, TimeUnit.MILLISECONDS);
                    connection.set(key.getBytes("UTF-8"), SerializationUtil.serialize(value), expiration, null);
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "插入数据失败");
            }
        });
    }

    @Override
    public boolean set(String key, Object value) {
        return set(key, value, null);
    }

    @Override
    public byte[] get(byte[] key) {
        return redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    }

    @Override
    public Boolean set(byte[] key, byte[] value) {
        return set(key, value, null);
    }

    @Override
    public Boolean set(byte[] key, byte[] value, Long expire) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            if (expire == null) {
                connection.set(key, value);
                return true;
            }
            if (expire != null && expire > 0) {
                Expiration expiration = Expiration.from(expire, TimeUnit.MILLISECONDS);
                connection.set(key, value, expiration, null);
                return true;
            }
            return false;
        });
    }

    @Override
    public Boolean del(byte[] key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            if (key != null && key.length > 0) {
                connection.del(key);
                return true;
            }
            return false;
        });
    }

    @Override
    public Boolean del(String key) {
        try {
            byte[] keysByte = key.getBytes("UTF-8");
            if (del(keysByte)) {
                return true;
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "删除数据有误");
        }
    }

    @Override
    public Set<byte[]> keys(String pattern) {
        return redisTemplate.execute((RedisCallback<Set<byte[]>>) connection -> {
            try {
                return connection.keys(pattern.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据类型转换失败");
            }
        });
    }

    @Override
    public void lPushList(String key, String value) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                connection.lPush(key.getBytes("UTF-8"), value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据类型转换失败");
            }
            return null;
        });
    }


    @Override
    public List<String> lrange(String key, long start, long end) {
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            try {
                List<String> stringList = new LinkedList<>();
                List<byte[]> bytes = connection.lRange(key.getBytes("UTF-8"), start, end);
                for (byte[] content : bytes) {
                    stringList.add(new String(content));
                }
                return stringList;
            } catch (Exception e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据转换有误");
            }
        });
    }

    @Override
    public void lRemList(String key, String value) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                connection.lRem(key.getBytes("UTF-8"), 0, value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据类型转换失败");
            }
            return null;
        });
    }

    @Override
    public void expireKey(String key, long expireSeconds) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            try {
                connection.expire(key.getBytes("UTF-8"), expireSeconds);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据类型转换失败");
            }
            return null;
        });
    }

    @Override
    public Long getEffectiveDuration(String key, TimeUnit timeUnit) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            try {
                return connection.ttl(key.getBytes("UTF-8"), timeUnit);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new GpSystemException(GpOtherExceptionCause.PARAMETER_CONVERT_ERROR, "数据类型转换失败");
            }
        });
    }

}

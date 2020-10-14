package alphago.propertysale.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: propertysale
 * @description:
 * @author: XIAO HAN
 * @create: 2020-10-06 22:19
 **/
public class RedisUtil {

    /**
    * @Description: Retuen redisTemplate for Hash usage,
    * @Author: Xiaohan
    * @Date: 6/10/20
    */
    public static RedisTemplate getHashRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 设置序列化方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    public static RedisTemplate valueRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 设置序列化方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    public static RedisTemplate getRedis(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtil.getApplicationContext().getBean("redisTemplate");
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
}
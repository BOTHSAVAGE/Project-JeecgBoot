package org.jeecg.common.modules.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.GlobalConstants;

import org.jeecg.common.modules.redis.receiver.RedisReceiver;
import org.jeecg.common.modules.redis.writer.JeecgRedisCacheWriter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.*;

import javax.annotation.Resource;
import java.time.Duration;

import static java.util.Collections.singletonMap;

/**
 * @check bothsavage
 * @lastUpdateTime 2021年6月16日
 *
 * - 数据存储
 * 	- redisTemplate配置
 * 	- spring Cache配置
 * - 发布订阅
 * - 过期key监听
 *
*/
@Slf4j
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	@Resource
	private LettuceConnectionFactory lettuceConnectionFactory;

	/**
	 * @description 自定义的缓存key的生成策略 若想使用这个key
	 *              只需要讲注解上keyGenerator的值设置为keyGenerator即可</br>
	 * @return 自定义策略生成的key
	 */
	//@Override
	//@Bean
	//public KeyGenerator keyGenerator() {
	//	return new KeyGenerator() {
	//		@Override
	//		public Object generate(Object target, Method method, Object... params) {
	//			StringBuilder sb = new StringBuilder();
	//			sb.append(target.getClass().getName());
	//			sb.append(method.getDeclaringClass().getName());
	//			Arrays.stream(params).map(Object::toString).forEach(sb::append);
	//			return sb.toString();
	//		}
	//	};
	//}



	/**
	 * 序列化工具的配置
	 */
	private Jackson2JsonRedisSerializer jacksonSerializer() {
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
		return jackson2JsonRedisSerializer;
	}



	/**
	 * RedisTemplate配置
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

		//1.新建一个redisTemplate -> 配置生产它的客户端
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);

		//2.新建一个json序列化工具+string序列化工具
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =jacksonSerializer();
		RedisSerializer<?> stringSerializer = new StringRedisSerializer();

		//3.设置序列化规则
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

		//4.调用InitializingBean的属性设置完成方法（具体逻辑：配置序列化工具和脚本解析器等）
		redisTemplate.afterPropertiesSet();

		//5.返回配置好的redisTemplate
		return redisTemplate;

	}

	/**
	 * 缓存配置管理器
	 * @param factory
	 * @return
	 * todo 4.16
	 * CacheManager的作用就是在使用@cacheable和@cacheevit的时候使用指定缓存比如redis进行操作
	 */
	@Bean
	public CacheManager cacheManager(LettuceConnectionFactory factory) {

        // 1.缓存配置 -> 设置过期时间+序列化规则
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().
																			      entryTtl(Duration.ofHours(6)).
																				  serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())).
																				  serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		//2.配置cacheManager
		RedisCacheManager cacheManager = RedisCacheManager.builder(new JeecgRedisCacheWriter(factory, Duration.ofMillis(50L))).cacheDefaults(redisCacheConfiguration)
				.withInitialCacheConfigurations(singletonMap(CacheConstant.TEST_DEMO_CACHE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)).disableCachingNullValues()))
				.withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_RANKING, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
				.withInitialCacheConfigurations(singletonMap(CacheConstant.PLUGIN_MALL_PAGE_LIST, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).disableCachingNullValues()))
				.transactionAware().build();

		//3.返回CacheManager
		return cacheManager;

	}

	/**
	 * redis 监听配置
	 */
	@Bean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, RedisReceiver redisReceiver, MessageListenerAdapter commonListenerAdapter) {

		//1.创建一个容器+设置工厂
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);

		//2.在容器中添加通道(通用监听者适配器+具体的通道)
		container.addMessageListener(commonListenerAdapter, new ChannelTopic(GlobalConstants.REDIS_TOPIC_NAME));

		//3.返回
		return container;

	}


	/**
	 * 消息接收适配器
	 */
	@Bean
	MessageListenerAdapter commonListenerAdapter(RedisReceiver redisReceiver) {

		//1.传入自定义的redis接收器，设置接收处理逻辑为onMessage
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(redisReceiver, "onMessage");

		//2.设置json序列化
		messageListenerAdapter.setSerializer(jacksonSerializer());

		//3.返回消息适配器
		return messageListenerAdapter;

	}



}

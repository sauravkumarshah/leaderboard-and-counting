package com.tipsontech.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.tipsontech.demo.listener.LeaderboardUpdateListener;

@Configuration
public class RedisConfig {

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
			LeaderboardUpdateListener leaderboardUpdateListener) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(leaderboardUpdateListener, new ChannelTopic("leaderboardUpdates"));

		return container;
	}
}

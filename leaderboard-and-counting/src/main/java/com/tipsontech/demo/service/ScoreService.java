package com.tipsontech.demo.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tipsontech.demo.constants.Constants;

@Service
public class ScoreService implements IScore {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public ScoreService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void addScore(String player, int score) {
		redisTemplate.opsForZSet().incrementScore(Constants.LEADERBOARD_KEY, player, score);
		publishLeaderboardUpdate();
	}

	public Set<String> getLeaderboard() {
		return redisTemplate.opsForZSet().reverseRange(Constants.LEADERBOARD_KEY, 0, -1);
	}

	public Double getScore(String player) {
		return redisTemplate.opsForZSet().score(Constants.LEADERBOARD_KEY, player);
	}

	private void publishLeaderboardUpdate() {
		redisTemplate.convertAndSend(Constants.CHANNEL, getLeaderboard().toString());
	}
}

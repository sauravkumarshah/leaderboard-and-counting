package com.tipsontech.demo.service;

import java.util.Set;

public interface IScore {
	public void addScore(String player, int score);

	public Set<String> getLeaderboard();

	public Double getScore(String player);

}

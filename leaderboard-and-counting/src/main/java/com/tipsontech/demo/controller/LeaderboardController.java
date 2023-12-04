package com.tipsontech.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tipsontech.demo.service.IScore;

@RestController
@RequestMapping("/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {

	@Autowired
	private IScore scoreService;

	@PostMapping("/addScore")
	public void addScore(@RequestParam String player, @RequestParam int score) {
		scoreService.addScore(player, score);
	}

	@GetMapping("/getLeaderboard")
	public Set<String> getLeaderboard() {
		return scoreService.getLeaderboard();
	}

	@GetMapping("/getScore")
	public Double getScore(@RequestParam String player) {
		return scoreService.getScore(player);
	}

	@MessageMapping("/subscribeLeaderboard")
	@SendTo("/topic/leaderboardUpdates")
	public Set<String> subscribeLeaderboard() {
		return scoreService.getLeaderboard();
	}
}

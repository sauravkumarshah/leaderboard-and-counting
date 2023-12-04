package com.tipsontech.demo.listener;

import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardUpdateListener implements MessageListener {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String receivedMessage = new String(message.getBody());
		System.out.println("Received leaderboard update: " + receivedMessage);

		try {

			JSONArray jsonArray = new JSONArray(receivedMessage);

			List<Object> leaderboard = jsonArray.toList();

			// Notify clients via WebSocket
			messagingTemplate.convertAndSend("/topic/leaderboardUpdates", leaderboard);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

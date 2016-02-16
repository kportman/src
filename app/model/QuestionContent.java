package app.model;

import java.sql.Timestamp;

public class QuestionContent {
	private String time;
	private String topic;
	private String nickname;
	private int rating;
	private int votes;
	private String text;
	
	public QuestionContent(String time, String text, String nickname, String topic, int rating, int votes){
		this.time = time;
		this.topic = topic;
		this.nickname = nickname;
		this.rating = rating;
		this.votes = votes;
		this.text = text;
	}
	

}

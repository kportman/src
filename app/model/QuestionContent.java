package app.model;

import java.sql.Timestamp;

public class QuestionContent {
	private String time;
	private String topic;
	private String nickname;
	private int rating;
	private int votes;
	private String text;
	private int id;
	
	public QuestionContent(String time, String text, String topic, String nickname, int rating, int votes, int id){
		this.time = time;
		this.topic = topic;
		this.nickname = nickname;
		this.rating = rating;
		this.votes = votes;
		this.text = text;
		this.id = id;
	}
	

}

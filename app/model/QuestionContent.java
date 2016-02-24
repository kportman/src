package app.model;

import java.sql.Timestamp;

public class QuestionContent {
	private String time;
	private String topic;
	private String text;
	private Float rating;
	private String nickname;
	private Float votes;
	private int id;
	
	private String ans;
	private Float ansVotes;
	
	public QuestionContent(String time, String text, String topic, String nickname, Float rating, Float votes, int id){
		this.time = time;
		this.topic = topic;
		this.nickname = nickname;
		this.rating = rating;
		this.votes = votes;
		this.text = text;
		this.id = id;
	}

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}

	public Float getAnsVotes() {
		return ansVotes;
	}

	public void setAnsVotes(Float ansVotes) {
		this.ansVotes = ansVotes;
	}
	

}

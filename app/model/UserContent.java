package app.model;

import java.util.ArrayList;

public class UserContent {

	private String photo;
	private String nickname;
	private Float rating;
	private String desc;
	public ArrayList<TopicContent> expProfile;
	public ArrayList<QuestionContent> lastAskedQ;
	public ArrayList<QuestionContent> lastAnsQ;
	
	
	public UserContent(String photo, String nickname, Float rating, String desc){
		this.photo = photo;
		this.desc = desc;
		this.setNickname(nickname);
		this.rating = rating;
		
		expProfile = new ArrayList<TopicContent>();
		lastAnsQ = new ArrayList<QuestionContent>();
		lastAskedQ = new ArrayList<QuestionContent>();
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}

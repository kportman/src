package app.model;

public class AnswerContent {
	private int id;
	private String time;
	private String nickname;
	private Float votes;
	private String text;
	
	
	public AnswerContent(String time, String text, String nickname, Float votes, int id){
		this.time = time;
		this.nickname = nickname;
		this.votes = votes;
		this.text = text;
		this.id = id;
	}
	
}

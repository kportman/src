package app.model;

public class AnswerContent {
	private int id;
	private String time;
	private String nickname;
	private int votes;
	private String text;
	
	
	public AnswerContent(String time, String text, String nickname, int votes, int id){
		this.time = time;
		this.nickname = nickname;
		this.votes = votes;
		this.text = text;
		this.id = id;
	}
	
}

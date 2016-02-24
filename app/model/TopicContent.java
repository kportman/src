package app.model;

public class TopicContent {
	private String topic;
	private Float grade;
	
	public TopicContent(Object topic, Object grade){
		this.topic = (String) topic;
		this.grade = (Float) grade;
	}
}

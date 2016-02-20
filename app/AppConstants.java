package app;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * A simple place to hold global application constants
 */
public interface AppConstants {

	//public final String CUSTOMERS = "customers";
	//public final String CUSTOMERS_FILE = CUSTOMERS + ".json";
	public final String NAME = "name";
	//public final Type CUSTOMER_COLLECTION = new TypeToken<Collection<Customer>>() {}.getType();
	//derby constants
	public final String DB_NAME = "AppDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/AppDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	//sql statements//
	
	//create statements:
	
	public final String SELECT_TOPICS = "SELECT TOPIC, "
			+ "RATING "
			+ "FROM QUESTIONS "
			+ "ORDER BY RATING DESC";
	
	public final String VOTE_ANSWER_STMT = "UPDATE ANSWERS "
			+ "SET VOTES = VOTES + ? "
			+ "WHERE ID=?";
	
	public final String VOTE_ANS_UPDATE_QUES_STMT = "UPDATE QUESTIONS "
			+ "SET ANS_SUM = ANS_SUM + ?, "
			+ "RATING = (0.2*(VOTES)+0.8*(ANS_SUM+?/NUM_OF_ANSWERS)) "
			+ "WHERE ID=?";
	
	public final String VOTE_QUESTION_STMT = "UPDATE QUESTIONS "
			+ "SET VOTES = VOTES + ?, "
			+ "RATING = CASE WHEN NUM_OF_ANSWERS = 0 THEN 0.2*(VOTES+?) ELSE (0.2*(VOTES+?)+0.8*(ANS_SUM/NUM_OF_ANSWERS)) END "//divide by zero bug
			+ "WHERE ID=?";
		//divide by zero bug solution
		//CASE WHEN @Name is null THEN [Name] ELSE @Name END
	
	public final String UPDATE_Q_TABLE = "UPDATE QUESTIONS "
			+ "SET NUM_OF_ANSWERS = NUM_OF_ANSWERS + 1, "
			+ "RATING = (0.2*(VOTES)+0.8*(ANS_SUM/(NUM_OF_ANSWERS + 1))) "
			+ "WHERE ID=?";
	
	public final String SELECT_TOPIC_QUESTIONS_BY_OFFSET = "SELECT TIME_ASKED,"
			+ "QUESTION_TEXT,"
			+ "TOPIC,"
			+ "NICKNAME,"
			+ "RATING,"
			+ "VOTES, "
			+ "ID "
			+ "FROM QUESTIONS "
			+ "WHERE TOPIC=? "
			+ "ORDER BY RATING DESC "
			+ "OFFSET ? ROWS "
			+ "FETCH NEXT 20 ROWS ONLY";
	
	public final String SELECT_EXIST_QUESTIONS_BY_OFFSET = "SELECT TIME_ASKED,"
			+ "QUESTION_TEXT,"
			+ "TOPIC,"
			+ "NICKNAME,"
			+ "RATING,"
			+ "VOTES, "
			+ "ID "
			+ "FROM QUESTIONS "
			//+ "WHERE NUM_OF_ANSWERS=0 "
			+ "ORDER BY RATING DESC "
			+ "OFFSET ? ROWS "
			+ "FETCH NEXT 20 ROWS ONLY";
	
	public final String SELECT_NEW_QUESTIONS_BY_OFFSET = "SELECT TIME_ASKED,"
			+ "QUESTION_TEXT,"
			+ "TOPIC,"
			+ "NICKNAME,"
			+ "RATING,"
			+ "VOTES, "
			+ "ID "
			+ "FROM QUESTIONS "
			+ "WHERE NUM_OF_ANSWERS=0 "
			+ "ORDER BY TIME_ASKED DESC "
			+ "OFFSET ? ROWS "
			+ "FETCH NEXT 20 ROWS ONLY";
	
	public final String SELECT_ANSWERS = "SELECT TIME_ANSWERED,"
			+ "ANSWER_TEXT,"
			+ "NICKNAME,"
			+ "VOTES, "
			+ "ID "
			+ "FROM ANSWERS "
			+ "WHERE QID=? "
			+ "ORDER BY VOTES DESC";
	
	public final String CHECK_USER= "SELECT * "
			+ "FROM USERS "
			+ "WHERE USERNAME=? "
			+ "AND PASSWORD=?";
	
	public final String GET_USER= "SELECT * "
			+ "FROM USERS "
			+ "WHERE USERNAME=? ";
	
	public final String CREATE_USER_TABLE="CREATE TABLE USERS("
			+ "USERNAME VARCHAR(10) CHECK (USERNAME <> '') PRIMARY KEY,"
			+ "PASSWORD VARCHAR(8) NOT NULL CHECK (PASSWORD <> ''),"
			+ "NICKNAME VARCHAR(20) NOT NULL CHECK (NICKNAME <> ''),"
			+ "DESCRIPTION VARCHAR(2048),"
			+ "PHOTO_URL VARCHAR(20000),"
			+ "RATING INT DEFAULT 0,"
			+ "UNIQUE (NICKNAME))";
	
	public final String CREATE_QUESTIONS_TABLE="CREATE TABLE QUESTIONS("
			+ "ID INT PRIMARY KEY,"
			+ "RATING FLOAT DEFAULT 0,"
			+ "VOTES INT DEFAULT 0,"
			+ "QUESTION_TEXT VARCHAR(140) NOT NULL CHECK (QUESTION_TEXT <> ''),"
			+ "NICKNAME VARCHAR(20) CHECK (NICKNAME <> ''),"
			+ "TIME_ASKED TIMESTAMP NOT NULL,"
			+ "NUM_OF_ANSWERS INT DEFAULT 0,"
			+ "TOPIC VARCHAR(140),"
			+ "ANS_SUM INT DEFAULT 0,"
			+ "FOREIGN KEY	(NICKNAME) REFERENCES USERS(NICKNAME))";
	
	public final String CREATE_ANSWERS_TABLE="CREATE TABLE ANSWERS("
			+ "ID INT PRIMARY KEY,"
			+ "QID INT NOT NULL,"
			+ "VOTES INT DEFAULT 0,"
			+ "ANSWER_TEXT VARCHAR(140) NOT NULL CHECK (ANSWER_TEXT <> ''),"
			+ "NICKNAME VARCHAR(20) CHECK (NICKNAME <> ''),"
			+ "TIME_ANSWERED TIMESTAMP NOT NULL,"
			+ "FOREIGN KEY	(QID) REFERENCES QUESTIONS(ID))";
	
	//insert statements:
	
	public final String INSERT_USER_STMT = "INSERT INTO USERS VALUES(?,?,?,?,?,?)";
	public final String INSERT_QUESTION_STMT="INSERT INTO QUESTIONS VALUES(?,?,?,?,?,?,?,?,?)";
	public final String INSERT_ANSWER_STMT="INSERT INTO ANSWERS VALUES(?,?,?,?,?,?)";
	
	public final Boolean CREATE_DB = false;// Initialize to true only if data base is new, Otherwise - set to false
	
	public final String QUESTIONS_SEQUENCE = "SELECT NEXT VALUE FOR Q_SEQ";
	public final String Q_MAX_ID = "SELECT MAX(ID) FROM QUESTIONS";
	public final String A_MAX_ID = "SELECT MAX(ID) FROM ANSWERS";
		
}

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
	
	public final String SELECT_NEW_QUESTIONS_BY_OFFSET = "SELECT TIME_ASKED,"
			+ "QUESTION_TEXT,"
			+ "TOPIC,"
			+ "NICKNAME,"
			+ "RATING,"
			+ "VOTES "
			+ "FROM QUESTIONS "
			+ "WHERE NUM_OF_ANSWERS=0 "
			+ "ORDER BY TIME_ASKED DESC "
			+ "OFFSET ? ROWS "
			+ "FETCH NEXT 20 ROWS ONLY";
	
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
			+ "RATING INT DEFAULT 0,"
			+ "VOTES INT DEFAULT 0,"
			+ "QUESTION_TEXT VARCHAR(140) NOT NULL CHECK (QUESTION_TEXT <> ''),"
			+ "NICKNAME VARCHAR(20) CHECK (NICKNAME <> ''),"
			+ "TIME_ASKED TIMESTAMP NOT NULL,"
			+ "NUM_OF_ANSWERS INT DEFAULT 0,"
			+ "TOPIC VARCHAR(140),"
			+ "FOREIGN KEY	(NICKNAME) REFERENCES USERS(NICKNAME))";
	
	//insert statements:
	
	public final String INSERT_USER_STMT = "INSERT INTO USERS VALUES(?,?,?,?,?,?)";
	public final String INSERT_QUESTION_STMT="INSERT INTO QUESTIONS VALUES(?,?,?,?,?,?,?,?)";
	
	public final Boolean CREATE_DB = false;// Initialize to true only if data base is new, Otherwise - set to false
	
	public final String QUESTIONS_SEQUENCE = "SELECT NEXT VALUE FOR Q_SEQ";
	public final String MAX_ID = "SELECT MAX(ID) FROM QUESTIONS";
	
}

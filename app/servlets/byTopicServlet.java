package app.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;

import app.AppConstants;
import app.model.AnswerContent;

/**
 * Servlet implementation class byTopicServlet
 */
@WebServlet("/byTopicServlet")
public class byTopicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public byTopicServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                return -1 * (o1.getValue().compareTo(o2.getValue()));
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultSet rs =null;
		PreparedStatement pstmt=null;
		Connection conn = null;
		PrintWriter out=null;

		try {
			ArrayList <String> messageResult= new ArrayList <String>();
			Gson gson = new Gson();
			String jsonResult;
			response.setContentType("text/html");
			out = response.getWriter();	

			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			conn = ds.getConnection();

			//checkSession
			//HttpSession session=request.getSession(false); //Prevent access to SRV if user does not have an active session
			//if (session ==null) {
			//	return ;
			//}

			//String username = (String)session.getAttribute("username");
			//String onlyfollowed = request.getParameter("onlyfollowed");

			/*if (onlyfollowed != null && onlyfollowed.contains("true")) {
				pstmt = conn.prepareStatement(AppConstants.SELECT_TOP_MESSAGES_BY_POPULARITY_OF_USER_I_FOLLOW);
				pstmt.setString(1, username);
				pstmt.setString(2, username);
				pstmt.setInt(3, 10);
			} else {
				pstmt = conn.prepareStatement(AppConstants.SELECT_TOP_MESSAGES_BY_POPULARITY);
				pstmt.setString(1, username);
				pstmt.setString(2, username);
				pstmt.setInt(3, 10);
			}
			*/
			//int offset = Integer.parseInt(request.getParameter("offset"));
			if(request.getParameter("get").equals("allTopics"))
			{
				pstmt = conn.prepareStatement(AppConstants.SELECT_TOPICS);
			}
			rs = pstmt.executeQuery();
			
			
			Map<String, Float> topics = new HashMap<String, Float>();
			while(rs.next()){
				if(topics.containsKey(rs.getString(1)))
				{
					topics.put(rs.getString(1), topics.get(rs.getString(1)) + rs.getFloat(2));
				}else{
					topics.put(rs.getString(1), rs.getFloat(2));
				}
			}
			//sorting Map like Hashtable and HashMap by values in Java
			Map<String, Float> sorted = sortByValues(topics);
	        System.out.println("Sorted Map in Java by values: " + sorted);
			
	        Set<String> set = sorted.keySet();
	        System.out.println("Sorted set in Java by values: " + set);
	        
	        Integer counter = 0;
	        for(String s : set){
	        	System.out.println(s);
	        	if (counter < 20) {
		        	messageResult.add(s);
		        	counter++;
	        	} else {
	        		break;
	        	}
	        }
	        
	        jsonResult=gson.toJson(messageResult,ArrayList.class);
			out.print(jsonResult);
			

		} catch (IOException | SQLException | NamingException e) {
			getServletContext().log("Error ,general SQLException ", e);
		} finally {
			try { rs.close(); } catch (Exception e) { /* ignored */ }
			try { pstmt.close(); } catch (Exception e) { /* ignored */ }
			try { conn.close(); } catch (Exception e) { /* ignored */ }
			try { out.close(); } catch (Exception e) { /* ignored */ }


		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

package app.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import app.Utils;
import app.model.QuestionContent;
import app.model.TopicContent;
import app.model.UserContent;

/**
 * Servlet implementation class profileServlet
 */
@WebServlet("/profileServlet")
public class profileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public profileServlet() {
        super();
        // TODO Auto-generated constructor stub
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


			ArrayList <UserContent> messageResult= new ArrayList <UserContent>();
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
			
			//String type = request.getParameter("getQuestions");
			//int offset = Integer.parseInt(request.getParameter("offset"));
			
			pstmt = conn.prepareStatement(AppConstants.SELECT_USERS);
			//pstmt.setString(1, type);
			//pstmt.setInt(2, offset);
			
			
			rs = pstmt.executeQuery();
			
			//prepare users
			while(rs.next()){
				UserContent uC = new UserContent ( rs.getString(1), rs.getString(2), rs.getFloat(3), rs.getString(4));
				messageResult.add(uC);
			}
			rs.close();
			pstmt.close();
			
			long tsTime;
			DateFormat df;
			Date startDate;
			String createdDate;
			
			//prepare last asked
			for(UserContent u : messageResult){
				pstmt = conn.prepareStatement(AppConstants.SELECT_LAST_ASKED_Q);
				pstmt.setString(1, u.getNickname());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					tsTime = rs.getTimestamp(1).getTime();
					df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					startDate = new Date(tsTime);
					createdDate = df.format(startDate);
					
					QuestionContent qC = new QuestionContent ( createdDate, rs.getString(3), rs.getString(2), u.getNickname(), rs.getFloat(4),(float) 0,0);
					u.lastAskedQ.add(qC);
				}
				rs.close();
				pstmt.close();
			}
			
			
			//prepare last answered
			for(UserContent u : messageResult){
				pstmt = conn.prepareStatement(AppConstants.SELECT_LAST_ANS_Q);
				pstmt.setString(1, u.getNickname());
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					tsTime = rs.getTimestamp(1).getTime();
					df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					startDate = new Date(tsTime);
					createdDate = df.format(startDate);
					
					QuestionContent qC = new QuestionContent ( createdDate, rs.getString(3), rs.getString(2), u.getNickname(), rs.getFloat(4),(float) 0,0);
					qC.setAns(rs.getString(5));
					qC.setAnsVotes(rs.getFloat(6));
					u.lastAnsQ.add(qC);
				}
				rs.close();
				pstmt.close();
			}
			
			//prepare expertise profile
			for(UserContent u : messageResult){//not ready
				pstmt = conn.prepareStatement(AppConstants.SELECT_EXP_PROFILE);
				pstmt.setString(1, u.getNickname());
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
				Map<String, Float> sorted = Utils.sortByValues(topics);
		        System.out.println("Sorted Map in Java by values: " + sorted);
				
		        //Set<String> set = sorted.keySet();
		        //System.out.println("Sorted set in Java by values: " + set);
		        
		        Integer counter = 0;
		        Iterator<Entry<String, Float>> it = sorted.entrySet().iterator();
		        while (it.hasNext()) {//iterate over sorted map
		        	Map.Entry<String, Float> pair = (Map.Entry<String, Float>)it.next();
		        	System.out.println(pair.getKey() + " = " + pair.getValue());
		        	if (counter < 5) {
		        		TopicContent tC = new TopicContent ( pair.getKey(), pair.getValue());
						u.expProfile.add(tC);
			        	counter++;
		        	} else {
		        		break;
		        	}
		        	it.remove(); // avoids a ConcurrentModificationException
		        }
		        
				rs.close();
				pstmt.close();
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

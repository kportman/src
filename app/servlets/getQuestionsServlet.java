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
import app.model.QuestionContent;

/**
 * Servlet implementation class getQuestionsServlet
 */
@WebServlet("/getQuestionsServlet")
public class getQuestionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuestionsServlet() {
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


			ArrayList <QuestionContent> messageResult= new ArrayList <QuestionContent>();
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
			pstmt = conn.prepareStatement(AppConstants.SELECT_NEW_QUESTIONS_BY_OFFSET);
			pstmt.setInt(1, 0);
			rs = pstmt.executeQuery();
			
			long tsTime;
			DateFormat df;
			Date startDate;
			String createdDate;
			
			while(rs.next()){
				tsTime = rs.getTimestamp(1).getTime();
				df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				startDate = new Date(tsTime);
				createdDate = df.format(startDate);
				
				QuestionContent qC = new QuestionContent ( createdDate, rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),rs.getInt(6));
				messageResult.add(qC);

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

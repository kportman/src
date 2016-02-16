package app.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;

import app.AppConstants;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultSet rs =null;
		PreparedStatement pstmt=null;
		Connection conn = null;
		PrintWriter out = null;

		try{
			Gson gson = new Gson();
			String jsonResult=null;

			response.setContentType("text/html");
			out = response.getWriter();	

			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource) context.lookup(AppConstants.DB_DATASOURCE);
			conn = ds.getConnection();



			String username = request.getParameter("username");
			String password = request.getParameter("password");

			pstmt=conn.prepareStatement(AppConstants.CHECK_USER);
			pstmt.setString(1,username);
			pstmt.setString(2,password);
			rs = pstmt.executeQuery();

			if (!rs.next()){ //data not found at DB
				out.print("userInvalid");
				System.out.print(username);
				System.out.println(" failed to login");
			} else {//return user info
				System.out.print(username);
				System.out.println(" logged in succesfuly");
				rs.close();
				pstmt.close();
				HttpSession session=request.getSession();
				session.setAttribute("username",username);
				session.setMaxInactiveInterval(60*60);


				pstmt=conn.prepareStatement(AppConstants.GET_USER);
				pstmt.setString(1,username);
				rs = pstmt.executeQuery();
				rs.next();
				String usernick= rs.getString(3);
				String userdesc= rs.getString(4);
				String userpic= rs.getString(5);



				ArrayList<String> dataArray = new ArrayList<String>();
				dataArray.add(username);
				dataArray.add(userpic);
				dataArray.add(usernick);
				dataArray.add(userdesc);
				jsonResult=gson.toJson(dataArray,ArrayList.class);//changes here
				out.print(jsonResult);

			}
			conn.commit();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		} finally {
			try { 
				rs.close(); 
				pstmt.close();
				conn.close();
				out.close();
			} catch (Exception e){}
		}
	}
}

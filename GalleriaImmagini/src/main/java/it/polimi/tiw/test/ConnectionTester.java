package it.polimi.tiw.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConnectionTester extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String DB_URL = getServletContext().getInitParameter("dbUrl");
		final String USER = getServletContext().getInitParameter("dbUser");
		final String PASS_greg = getServletContext().getInitParameter("dbPasswordGreg");
		final String PASS_dani = getServletContext().getInitParameter("dbPasswordDani");
		final String DRIVER_STRING = getServletContext().getInitParameter("dbDriver");
		
		String result = "Connection worked";
		try {
			Class.forName(DRIVER_STRING); // Inizializzo il driver necessario per connettersi alla BDD
			DriverManager.getConnection(DB_URL, USER, PASS_dani); //Ottengo la connessione al db, usando il driver, attraverso il DriverManager
		} catch (Exception e) {
			result = "Connection failed";
			e.printStackTrace();
		}
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();
	}
}
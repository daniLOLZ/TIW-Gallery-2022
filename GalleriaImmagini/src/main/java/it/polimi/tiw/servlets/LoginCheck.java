package it.polimi.tiw.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utility.CheckerUtility;
import it.polimi.tiw.utility.ConnectionUtility;

//@WebServlet("/LoginCheck")
public class LoginCheck extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Connection connection;

    @Override
    public void init() throws ServletException {
    	
		connection = ConnectionUtility.getConnection(getServletContext());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO(connection);
        String path = getServletContext().getContextPath();
        
        //final WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        
        if(!(CheckerUtility.checkAvailability(username) ||
        	 CheckerUtility.checkAvailability(password))){
        
        	response.sendRedirect(path + "/?errorId=5"); //Send with error status = 5 (null/invalid inputs (login))
            return;
		}

        try {
			if(userDAO.checkCredentials(username, password) == null) {
				// User isn't present
				
				response.sendRedirect(path + "/?errorId=6"); //Send with error status = 6 (incorrect credentials)
				return;
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in database checking user credentials");
			return;
		}

        
        // Add session creation here
		HttpSession session = request.getSession(true);
		//It should always be new, since the session is just now starting after sign in
		if(session.isNew()){
			request.getSession().setAttribute("username", username);
		}

        response.sendRedirect(path + "/Home");
    }

    @Override
    public void destroy() {
    	try {
			ConnectionUtility.closeConnection(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}
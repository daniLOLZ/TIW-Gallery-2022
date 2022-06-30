package it.polimi.tiw.servlets;

import java.io.IOException;
import java.io.Writer;
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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.utility.ConnectionUtility;

//@WebServlet("/")
public class GoToLoginPage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	//DB connection not necessary in this page, can be removed
    private Connection connection;
	private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
    	
    	ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		// a template resolver is an object in charge of resolving templates and containing additional information
		// related to the template, like the template mode, if it can be cached and for how long. a servletcontext
		// resolver specifically computes the resource from which to resolve the template based on a ServletContext
		// object.
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
    	
		connection = ConnectionUtility.getConnection(getServletContext());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String htmlPath = "/login_page.html";
		ServletContext servletContext = getServletContext();
		final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		
		//Checking whether the user is already logged in, in that case send them to the home page
		HttpSession session = request.getSession(false);
        
        if(!(session == null || session.isNew() || session.getAttribute("username") == null)){
        	response.sendRedirect(getServletContext().getContextPath() + "/Home");
            return;
        }
		
		String errorMsgLogin = "";
		String errorMsgSignup = "";
		String readErrorId = request.getParameter("errorId");
		Integer errorId = -1;
		
		if(readErrorId == null) {
			errorId = -1;
		}
		
		try {
			errorId = Integer.parseInt(readErrorId);
		} catch (NumberFormatException e) {
			errorId = -1;
			//Bad query string, ignore the error id
		}
		
		switch (errorId) {
		case 1:
			errorMsgSignup = "Invalid inputs received";
			break;
		case 2:
			errorMsgSignup = "Bad email format";
			break;
		case 3:
			errorMsgSignup = "Username already taken";
			break;
		case 4:
			errorMsgSignup = "Passwords not matching";
			break;
		case 5:
			errorMsgLogin = "Invalid inputs received";
			break;
		case 6:
			errorMsgLogin = "Wrong credentials";
		}
		context.setVariable("errorMsgLogin", errorMsgLogin);
		context.setVariable("errorMsgSignup", errorMsgSignup);
		
		// A webContext is used to pass variables to the template engine, just like the pageContext does
		// in JSP.
		// set error message variable to context
		templateEngine.process(htmlPath, context, response.getWriter());
		// In this case the login page should be identical in everything except for the error message, which gets
		// displayed to the user if they already tried logging in / signing up once and failed.
		
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
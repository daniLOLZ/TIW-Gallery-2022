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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

//@WebServlet("/")
public class GoToLoginPage extends HttpServlet {

	private static final long serialVersionUID = 1L;
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
    	
        final String DB_URL = getServletContext().getInitParameter("dbUrl");
		final String USER = getServletContext().getInitParameter("dbUser");
//		final String PASS = getServletContext().getInitParameter("dbPasswordGreg");
		final String PASS = getServletContext().getInitParameter("dbPasswordDani");
		final String DRIVER_STRING = getServletContext().getInitParameter("dbDriver");
		
		try {
			Class.forName(DRIVER_STRING);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		}
		catch (ClassNotFoundException e){
			throw new UnavailableException("Can't load db driver");
		}
		catch (SQLException e) {
			throw new UnavailableException("Can't connect to database");
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String htmlPath = "/WEB-INF/login_page.html";
		ServletContext servletContext = getServletContext();
		final WebContext context = new WebContext(request, response, servletContext, request.getLocale());
		// A webContext is used to pass variables to the template engine, just like the pageContext does
		// in JSP.
		// set error message variable to context
		templateEngine.process(htmlPath, context, response.getWriter());
		// In this case the login page should be identical in everything except for the error message, which gets
		// displayed to the user if they already tried logging in / signing up once and failed.
		
    }

    @Override
    public void destroy() {
        
    }


}
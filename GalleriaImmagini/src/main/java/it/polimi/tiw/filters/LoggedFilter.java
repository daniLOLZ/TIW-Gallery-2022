package it.polimi.tiw.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoggedFilter extends HttpFilter {
    
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpSession session = request.getSession();
        if(session.isNew() || session.getAttribute("username") == null){
            response.sendRedirect("/");
            return;
        }

        chain.doFilter(request, response);
    }

}

package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.TimeZone;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    private static final String PARAMETER_NAME = "timezone";
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        Set<String> set = Set.of(TimeZone.getAvailableIDs());

    if (request.getQueryString() == null){
        chain.doFilter(request, response);
    }else if (request.getQueryString().contains(PARAMETER_NAME)
            && set.contains(TimeServlet.getTimeZoneValue(request))
            && TimeServlet.getOffSet(request) >= -18
            && TimeServlet.getOffSet(request) <= 18){
        chain.doFilter(request, response);
    }else
        response.setStatus(400);
    response.setContentType("text/html; charset=utf-8");
    response.getWriter().write("Invalid timezone");
    response.getWriter().close();
    }
}
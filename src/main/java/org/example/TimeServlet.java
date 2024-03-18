package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String PARAMETER_NAME = "timezone";
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss z";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=utf-8");
        DateTimeFormatter formatter;
        ZoneId utcZone = ZoneId.of("UTC");
        String formattedTime;

        if (request.getQueryString() != null) {
            ZoneId zoneId = ZoneId.of(getTimeZoneValue(request));
            ZonedDateTime dateTime;

            if (request.getQueryString().contains("+")) {
                formatter = DateTimeFormatter.ofPattern(PATTERN + "+" + getOffSet(request));
                dateTime = LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(zoneId)
                        .plusHours(getOffSet(request));
                formattedTime = dateTime.format(formatter);
            } else if (request.getQueryString().contains("-")) {
                formatter = DateTimeFormatter.ofPattern(PATTERN + "-" + getOffSet(request));
                dateTime = LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .withZoneSameInstant(zoneId)
                        .minusHours(getOffSet(request));
                formattedTime = dateTime.format(formatter);
            } else {
                formatter = DateTimeFormatter.ofPattern(PATTERN);
                dateTime = LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(zoneId);
            formattedTime = dateTime.format(formatter);
            }
                response.getWriter()
                        .write(formattedTime);
                response.getWriter().close();

        } else
            formatter = DateTimeFormatter.ofPattern(PATTERN);
        Instant instant = Instant.now();
        response.getWriter().write(instant.atZone(utcZone).format(formatter));
        response.getWriter().close();
        }

    public static String getTimeZoneValue(HttpServletRequest request){
        String queryString = request.getQueryString();
        String value = request.getParameter(PARAMETER_NAME);
        if (queryString.contains("+")) {
            return value.split(" ")[0];
        }else if (queryString.contains("-")) {
            int minusIndex = value.indexOf("-");
            return value.substring(0, minusIndex);
        }else return value;
    }

    public static long getOffSet (HttpServletRequest request) {
        String query = request.getQueryString();
        String paramValue = request.getParameter(PARAMETER_NAME);
        if (query.contains("+")) {
            return Long.parseLong(paramValue.split(" ")[1]);
        }else if (query.contains("-")) {
            int minusNumber = query.indexOf("-");
            return Long.parseLong(query.substring(minusNumber+1));
        } else return 0L;
    }
}
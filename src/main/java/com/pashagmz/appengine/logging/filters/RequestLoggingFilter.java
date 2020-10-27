package com.pashagmz.appengine.logging.filters;

import com.pashagmz.appengine.logging.builders.RequestInfoBuilder;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {

            boolean isDetailedInfo = true;

            String requestInfo = RequestInfoBuilder.newBuilder()
                .setStartTime(startTime)
                .setEndTime(System.currentTimeMillis())
                .setRequest(request)
                .setResponse(response)
                .useDetailedInfo(isDetailedInfo)
                .build();

            log.info(requestInfo);

        }
    }
}

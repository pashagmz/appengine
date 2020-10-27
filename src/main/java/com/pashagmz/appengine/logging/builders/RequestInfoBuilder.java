package com.pashagmz.appengine.logging.builders;

import static java.util.Collections.list;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;

public class RequestInfoBuilder {

    private static final String DATETIME_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final List<String> RESTRICTED_HEADERS = new ArrayList<>();
    private static final String REQUEST_INFO_PATTERN = "%s|%s|%s|%s|%s|%s|%s";

    private long startTime;
    private long endTime;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private boolean useDetailedInfo = false;

    private RequestInfoBuilder() {}

    public static RequestInfoBuilder newBuilder() {
        return new RequestInfoBuilder();
    }

    public RequestInfoBuilder setStartTime(long startTime) {
        this.startTime = startTime;

        return this;
    }

    public RequestInfoBuilder setEndTime(long endTime) {
        this.endTime = endTime;

        return this;
    }

    public RequestInfoBuilder setRequest(HttpServletRequest request) {
        this.request = request;

        return this;
    }

    public RequestInfoBuilder setResponse(HttpServletResponse response) {
        this.response = response;

        return this;
    }

    public RequestInfoBuilder useDetailedInfo(boolean useDetailedInfo) {
        this.useDetailedInfo = useDetailedInfo;

        return this;
    }

    public String build() {

        if (request == null) {
            return "Request must not be empty!";
        }

        final StringJoiner headers = new StringJoiner(",");
        final StringJoiner requestParams = new StringJoiner(",");

        List<String> values = list(request.getHeaderNames());

        if (useDetailedInfo) {

            values.stream()
                .filter(name -> !RESTRICTED_HEADERS.contains(name))
                .forEach(name -> headers.add(name.concat("=").concat(list(request.getHeaders(name)).toString())));

            request.getParameterMap()
                .forEach((key, value) -> requestParams.add(key.concat("=").concat(Arrays.toString(value))));
        } else {
            headers.add("masked");
            requestParams.add("masked");
        }

        return String.format(
            REQUEST_INFO_PATTERN,
            request.getMethod(),
            getRequestUri(request),
            new SimpleDateFormat(DATETIME_PATTERN_STRING).format(new Date(startTime)),
            endTime - startTime,
            response.getStatus(),
            headers,
            requestParams
        );
    }

    private static String getRequestUri(HttpServletRequest request) {

        String uri = (String) request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE);

        if (uri == null) {
            uri = request.getRequestURI();
        }

        return uri;
    }
}

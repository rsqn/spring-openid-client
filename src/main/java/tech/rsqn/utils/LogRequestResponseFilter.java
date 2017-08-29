package tech.rsqn.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class LogRequestResponseFilter implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LogRequestResponseFilter.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        traceRequest(request, body);
        ClientHttpResponse clientHttpResponse = execution.execute(request, body);
        traceResponse(clientHttpResponse);

        return clientHttpResponse;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        logger.debug("request URI : " + request.getURI());
        logger.debug("request method : " + request.getMethod());
        logger.debug("request body : " + getRequestBody(body));

        for (String hn : request.getHeaders().keySet()) {
            List<String> v = request.getHeaders().get(hn);
            for (String vl : v) {
                logger.debug("request header : (" + hn + ") : (" + vl + ')');
            }
        }
    }

    private String getRequestBody(byte[] body) throws UnsupportedEncodingException {
        if (body != null && body.length > 0) {
            return (new String(body, "UTF-8"));
        } else {
            return null;
        }
    }


    private void traceResponse(ClientHttpResponse response) throws IOException {
        logger.debug("response status code: " + response.getStatusCode());
        logger.debug("response status text: " + response.getStatusText());
    }
}


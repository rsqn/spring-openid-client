package tech.rsqn.utils.jjst.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DevOnlyCORSFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(DevOnlyCORSFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String origin = "*";

        log.debug("DevOnlyCORSFilter - RequstedURI is: " + req.getRequestURL());
        origin = req.getRequestURL().toString();


        String originHeader = req.getHeader("Origin");
        log.debug("DevOnlyCORSFilter - Origin is: " + originHeader);
        if (originHeader != null) {
            origin = originHeader;
        }

        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Content-Range, Content-Disposition, Content-Description");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Methods", "PUT, POST, GET, DELETE");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}

package tech.rsqn.utils.jjst.servlets;

import tech.rsqn.utils.jjst.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JavascriptSourceServlet extends AbstractContentServlet {
    private static Logger log = LoggerFactory.getLogger(JavascriptSourceServlet.class);

    @Override
    protected String getContentType() {
        return "text/javascript";
    }

    @Override
    protected String getContent(HttpServletRequest request) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.debug("Loading {} from classpath", path);
        String s = ResourceUtil.loadContentFromResource(path);
        return s;
    }
}

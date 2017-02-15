package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class TemplateServlet extends AbstractContentServlet {
    private static Logger log = LoggerFactory.getLogger(TemplateServlet.class);
//
//    private String loadTemplate(String path) throws IOException {
//        log.debug("loading template " + path);
//        ClassPathResource resource = new ClassPathResource(path);
//
//        String s = IOUtil.readToString(resource.getInputStream());
//
//        s = s.replaceAll("\\r", "");
//        s = s.replaceAll("\\n", "");
//        s = s.replaceAll("\\t", "");
//        s = StringEscapeUtils.escapeEcmaScript(s);
//        return s;
//    }
//
//    private String parseTemplateToken(String line) {
//        int start = line.indexOf("#template");
//        int end = line.indexOf("#", start + 1);
//        return line.substring(start, end + 1);
//    }
//
//    private String parseTemplateInclude(String line) {
//        String path = line.split("=")[1];
//        path = path.replace('#', ' ').trim();
//        return path;
//    }

    @Override
    protected String getContentType() {
        return "text/html";
    }

    @Override
    protected String getContent(HttpServletRequest request) throws ServletException, IOException {
        File f = resolveRelativeFileFromUri(request);
        return getFileContents(f);
    }
}


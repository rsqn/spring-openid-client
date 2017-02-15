package tech.rsqn.utils.jjst.servlets;

import tech.rsqn.utils.jjst.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

public abstract class AbstractContentServlet extends HttpServlet {
    private static Logger log = LoggerFactory.getLogger(AbstractContentServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected File resolveFile(File cwd, String relativePath) throws IOException {
        File f = new File(cwd, relativePath);
        return f;
    }

    protected String getFileContents(File f) throws IOException {
        String content = new String(Files.readAllBytes(f.toPath()));
        return content;
    }

    protected abstract String getContentType();

    protected abstract String getContent(HttpServletRequest request) throws ServletException, IOException;

    //todo - classpath resources
    protected File resolveRelativeFileFromUri(HttpServletRequest request) throws ServletException, IOException {
        String bootPath = request.getRequestURI();
        File tld = new File(getServletContext().getRealPath("/"));
        File bootFile = new File(getServletContext().getRealPath(bootPath));

        if (!bootFile.getAbsolutePath().contains(tld.getAbsolutePath())) {
            throw new ServletException("Invalid Path");
        }
        return bootFile;
    }

    protected String resolveContent(File f, String path) throws IOException {
        File tld = new File(getServletContext().getRealPath("/"));

        String content = "";

        if (f != null && f.exists()) {
            if (!f.getAbsolutePath().contains(tld.getAbsolutePath())) {
                throw new IOException("Invalid Path");
            }
            content = new String(Files.readAllBytes(f.toPath()));
        } else {
            content = ResourceUtil.loadContentFromResource(path);
        }
        return content;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType(getContentType());
        PrintWriter out = response.getWriter();
        String contents = getContent(request);

        try {
            out.write(contents);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.err.println(e);
            e.printStackTrace();
        } finally {
            out.close();
        }
    }


}


package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public abstract class AbstractAggregationServlet extends AbstractContentServlet {
    public static final String CLEAR_CACHE = "clearcache";
    public static final String NO_CACHE = "nocache";
    private static Logger log = LoggerFactory.getLogger(AbstractAggregationServlet.class);
    private static final Map<String, String> cache = new Hashtable<>();

    private String baseProfiles;

    private String generateCacheKey(String path, List profiles) {
        return path + profiles;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (config.getInitParameter("baseProfiles") != null) {
            baseProfiles = config.getInitParameter("baseProfiles");
        } else {
            baseProfiles = "";
        }
    }

    @Deprecated
    protected File resolveFile(File cwd, String relativePath) throws IOException {
        File f = new File(cwd, relativePath);
        return f;
    }

    @Deprecated
    protected String getFileContents(File f) throws IOException {
        String content = new String(Files.readAllBytes(f.toPath()));
        return content;
    }

    protected abstract String getContentType();

    protected abstract String processFileContent(String content);

    protected abstract String postProcess(String content);

    @Override
    protected String getContent(HttpServletRequest request) throws ServletException, IOException {
        String contents = null;

        File tld = new File(getServletContext().getRealPath("/"));
        String path = request.getRequestURI();

        String profileArgs = request.getParameter("profiles");

        List<String> profileList = new ArrayList<>();

        if (baseProfiles != null) {
            String[] profileSplit = baseProfiles.split(",");
            Collections.addAll(profileList, profileSplit);
        }

        if (profileArgs != null) {
            String[] profileSplit = profileArgs.split(",");
            Collections.addAll(profileList, profileSplit);
        }

        if (profileList.contains(CLEAR_CACHE)) {
            cache.clear();
        }

        String cacheKey = generateCacheKey(path, profileList);

        contents = cache.get(cacheKey);

        if (contents == null || profileList.contains(NO_CACHE)) {
            log.debug("Profiles = " + profileList);
            StringBuffer buffer = new StringBuffer();
            aggregateFromFile(buffer, tld, path, profileList);
            contents = buffer.toString();
            contents = postProcess(contents);
            cache.put(generateCacheKey(path, profileList), contents);
            log.info("Aggregation of {} complete", path);
        } else {
            log.trace("Returning {} from cache ", path);
        }
        return contents;

    }

    protected void aggregateFromFile(StringBuffer buffer, File cwd, String fileName, List<String> profiles) throws IOException {
        String includeToken = "#include";
        String ifProfileToken = "#ifprofile";
        String ifNotProfileToken = "#ifnot_profile";
        String endIfToken = "#endif";

        File f = resolveFile(cwd, fileName);
        File nCwd = f.getParentFile();
        String baseContents = resolveContent(f,fileName);

        BufferedReader reader = new BufferedReader(new StringReader(baseContents));
        String line;
        String includeFileName;

        final int DEFAULT_STATE = 0;
        final int IN_PROFILE_STATE_IGNORE = 1;
        final int IN_PROFILE_STATE_PROCESS = 2;
        int state = DEFAULT_STATE;

        while ((line = reader.readLine()) != null) {
            if (state == IN_PROFILE_STATE_IGNORE || state == IN_PROFILE_STATE_PROCESS) {
                if (line.trim().startsWith(endIfToken)) {
                    state = DEFAULT_STATE;
                    continue;
                }
            }
            if (state == IN_PROFILE_STATE_IGNORE) {
                continue;
            }

            if (line.trim().startsWith(includeToken)) {
                includeFileName = line.replace(includeToken, "").trim();
                buffer.append("\n\n\n");
                buffer.append("// " + includeFileName);
                buffer.append("\n\n");
                aggregateFromFile(buffer, nCwd, includeFileName, profiles);
            }
//            else if (line.trim().contains("#template")) {
//                String templateInclude = parseTemplateToken(line);
//                log.debug("Template include " + templateInclude);
//                String templateContent = loadTemplate(parseTemplateInclude(templateInclude));
//                line = line.replace(templateInclude, templateContent);
//                buffer.append("// " + templateInclude);
//                buffer.append("\n\n");
//                buffer.append(line);
//                buffer.append("\n\n");
//            }
            else if (line.trim().startsWith(ifProfileToken)) {
                String profileName = line.replace(ifProfileToken, "").trim();
                log.debug("IfProfile [" + profileName + "]");
                if (profiles.contains(profileName)) {
                    state = IN_PROFILE_STATE_PROCESS;
                    log.debug("IfProfile process [" + profileName + "]");
                } else {
                    log.debug("IfProfile ignore [" + profileName + "]");
                    state = IN_PROFILE_STATE_IGNORE;
                }
            } else if (line.trim().startsWith(ifNotProfileToken)) {
                String profileName = line.replace(ifNotProfileToken, "").trim();
                log.debug("IfNotProfile [" + profileName + "]");
                if (profiles.contains(profileName)) {
                    log.debug("IfNotProfile ignoring [" + profileName + "]");
                    state = IN_PROFILE_STATE_IGNORE;
                } else {
                    state = IN_PROFILE_STATE_PROCESS;
                    log.debug("IfNotProfile processing [" + profileName + "]");
                }
            } else {
                buffer.append(line);
                buffer.append("\n");
            }
        }
    }
}


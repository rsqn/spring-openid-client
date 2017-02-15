package tech.rsqn.utils.jjst.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavascriptAggregationServlet extends AbstractAggregationServlet {
    private static Logger log = LoggerFactory.getLogger(TemplateServlet.class);

    @Override
    protected String getContentType() {
        return "text/javascript";
    }

    @Override
    protected String processFileContent(String content) {
        // copy replace tags code in here

//        if (!profileList.contains(NOCOMPILE)) {
//            JavaScriptMinifier minifier = new JavaScriptMinifier();
//            contents = minifier.minify(contents, false);
//        }
        return content;
    }

    @Override
    protected String postProcess(String content) {
        return content;
    }


}


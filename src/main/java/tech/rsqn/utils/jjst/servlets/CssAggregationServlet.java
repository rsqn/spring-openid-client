package tech.rsqn.utils.jjst.servlets;

public class CssAggregationServlet extends AbstractAggregationServlet {
    @Override
    protected String getContentType() {
        return "text/css";
    }

    @Override
    protected String processFileContent(String content) {
        return content;
    }

    @Override
    protected String postProcess(String content) {
        return content;
    }
}


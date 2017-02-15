package tech.rsqn.utils.jjst.util;


public class JavaScriptMinifier {
    public String minify(String js, boolean advanced) {

        return js;
        /*
        List<JSSourceFile> externs = Collections.emptyList();
        List<JSSourceFile> inputs = Arrays.asList(JSSourceFile.fromCode("default.js",
                js));

        CompilerOptions options = new CompilerOptions();
        if ( advanced ) {
            CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
        } else {
            CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
        }

        com.google.javascript.jscomp.Compiler compiler = new com.google.javascript.jscomp.Compiler();
        Result result = compiler.compile(externs, inputs, options);

        if (result.success) {
            return compiler.toSource();
//            return new StringReader(compiler.toSource());
        }

        String msg = "errors-";
        for (JSError error : result.errors) {
            msg += error.description;
        }
        throw new RuntimeException("Unable to minify input source " + msg);
        */
    }
}

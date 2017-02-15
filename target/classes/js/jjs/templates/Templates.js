ns("com.rsqn.jjst.core");


com.rsqn.jjst.core.Templates = function() {
    this.templates = {};
    this.enableCache = true;
    this.enableTags = true;
};


com.rsqn.jjst.core.Templates.prototype.getTemplate = function(templateName){
    if (this.templates.hasOwnProperty(templateName)) {
        //Logger.debug("Returning Cached Template " + templateName);
        var ret = $(this.templates[templateName]);
        if ( this.enableTags == true) {
            this.replaceTags(ret)
        }
        return ret;
    }

    var template = this.loadTemplate(templateName);

    if ( this.enableCache ) {
        this.templates[templateName] = template;
        Logger.debug("Cached template " + templateName);
    }

    var ret = $(template);
    if ( this.enableTags == true) {
        this.replaceTags(ret)
    }
    return ret;
};

com.rsqn.jjst.core.Templates.prototype.replaceTags = function(tpl) {

    tpl.find(".i18n").each(function(i,e){
        var elm= $(e);
        elm.html(i18n(elm.attr("data-i18n")));
    });

    tpl.find(".q-pop").each(function(i,e){
        var elm= $(e);
        elm.html(Qpop(elm,elm.attr("data-qpop")));
    });
};

com.rsqn.jjst.core.Templates.prototype.loadTemplate = function(templateName) {
    var uri =  rsConstants.contextPath + '/templates/' + templateName;
    Logger.info("Loading template " + templateName + " from " + uri);

    var txt = $.ajax({
        type: "GET",
        url: uri,
        cache: false,
        async: false
    }).responseText;

    return txt;
};

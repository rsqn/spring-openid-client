ns("com.roguesquadron.core")

var _UIComponentSeq = 0; //todo - fix this 100% reliable hack

com.roguesquadron.core.UIComponent = function () {
    this.isWidget = false;
    this.model = new com.roguesquadron.core.Model();
    this.components = {};
    this.seq = 0;
};

com.roguesquadron.core.UIComponent.prototype.getId = function () {
    if (this.seq == 0) {
        Logger.info("Next Seq " + this.seq);
        this.seq = ++_UIComponentSeq;
    }
    return this.seq;
};

com.roguesquadron.core.UIComponent.prototype.initTemplate = function (container, templateName) {
    this.container = container;
    this.myElement = rsTemplates.getTemplate(templateName);
    this.__initComponents();
};

com.roguesquadron.core.UIComponent.prototype.__initComponents = function () {
    var self = this;

    var parseName = function (s) {
        var m = s.match(/[\w-]+/);
        return m[0];
    };

    var parseArgs = function (s) {
        var m = s.match(/\[(.*)\]/);
        var ret = {};
        if (m.length >= 1) {
            var parts = m[1].split(",");
            for (var i = 0; i < parts.length; i++) {
                var keyValue = parts[i].split("=");
                ret[keyValue[0]] = keyValue[1];
            }
        }
        return ret;
    };

    this.find(".component").each(function (idx, e) {
        var _p = $(e);
        var _dc = _p.attr("data-component");
        Logger.error("Initialising component for " + _dc);

        var n = parseName(_dc);
        var args = parseArgs(_dc);

        var ins = componentRegistry.getInstance(n);
        if (ins) {
            ins.init(_p, args, self.model);
        } else {
            Logger.error("No component found matching (" + n + ")");
        }
    });

};

com.roguesquadron.core.UIComponent.prototype.initDecorator = function (container) {
    this.container = container;
    this.myElement = container;
};

com.roguesquadron.core.UIComponent.prototype.find = function (selector) {
    return this.myElement.find(selector);
};

com.roguesquadron.core.UIComponent.prototype.bindNavigationListeners = function (pageName, path, onNavInCb, onNavOutCb, onLoadCb) {
    var self = this;

    var prfx = "None";
    //if ( self.isWidget == false ) {
    //    $("body").trigger("nav-clear");
    //} else {
    //    prfx = "Widget ";
    //}
    if (self.isWidget == false) {
        prfx = "Page";
    } else {
        prfx = "Widget ";
    }

    Logger.debug(prfx + pageName + " binding ");
    this.docBody = $("body");
    this.STATE_HIDDEN = "hidden";
    this.STATE_VISIBLE = "visible";
    this.STATE_CLOSED = "closed";
    this.state = this.STATE_CLOSED;

    navCommon.registerPath(path, pageName);

    self.docBody.on("nav-clear", function (ev, data) {
        Logger.debug(prfx + pageName + " received nav-clear while in state " + self.state);
        if (self.state == self.STATE_HIDDEN) {
        } else if (self.state == self.STATE_VISIBLE) {
            self.state = self.STATE_HIDDEN;
            onNavOutCb(data);
        } else if (self.state == self.STATE_CLOSED) {

        } else {
            Logger.warn(prfx + pageName + " in unknown state " + self.state);
        }
    });

    self.docBody.on("nav-do-" + path, function (ev, data) {
        Logger.debug("Received " + "nav-do-" + path);

        Logger.debug(prfx + pageName + " received " + path + " while in state " + self.state + " with data " + JSON.stringify(data));
        if (self.state == self.STATE_HIDDEN) {
            self.state = self.STATE_VISIBLE;
            if (self["title"]) {
                document.title = self["title"];
            } else {
                document.title = self["name"];
            }
//            navCommon.recordNavigation(pageName,path);
            onNavInCb(data);
        } else if (self.state == self.STATE_VISIBLE) {

        } else if (self.state == self.STATE_CLOSED) {
            self.state = self.STATE_VISIBLE;
            if (self["title"]) {
                document.title = self["title"];
            } else {
                document.title = self["name"];
            }
//            navCommon.recordNavigation(pageName,path);
            onLoadCb(data);
        } else {
            Logger.warn(prfx + pageName + " in unknown state " + self.state);
        }
    });
};


/*
 TODO - generify this

 .fail(function (jqXHR, textStatus, errorThrown) {
 Logger.info("Status " + jqXHR.status + " responseText " + jqXHR.responseText);
 if (jqXHR.status == 500 || jqXHR.status == 0) {
 notify.fatalNotification("System Error", "Sorry something went wrong, Please try again later.");
 } else {
 Logger.info("RJ= " + JSON.stringify(jqXHR["responseJSON"]));
 Logger.info("Rt= " + jqXHR["responseText"]);
 self.onServerValidationFailed(jqXHR["responseJSON"],null);
 }

 });

 */

com.roguesquadron.core.UIComponent.prototype.simpleSelect2 = function (url, selector, vc, _opts) {
    var self = this;
    $.ajax({
            type: "GET",
            cache: false,
            url: url,
            contentType: "application/json",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            dataType: "json",
            async: false
        }
    ).done(function (data, textStatus, jqXHR) {
        var opts = {
            data: data
        };
        if (_opts) {
            for (var n in _opts) {
                var v = _opts[n];
                opts[n] = v;
            }
        }

        self.find(selector).select2(opts);
        //self.find(selector).val("CX");
    })
        .fail(this.stdFail());
};


com.roguesquadron.core.UIComponent.prototype.attachList = function (url, selector, vc, _opts) {
    var self = this;
    var cache = {};
    var elm = self.find(selector);
    var opts = {
        initSelection: function (element, callback) {
            Logger.info("initSelection " + selector + " as " + url);
        },
        query: function (query) {
            var t = urlUtil.addParameter(url, "term", query.term);
            t = urlUtil.addParameter(t, "c", vc);

            if (cache[t]) {
                query.callback(cache[t]);
                return;
            }
            self.api.one(t).then(function (data) {
                var ret = {
                    results: []
                };
                for (var i = 0; i < data.length; i++) {
                    var rw = {
                        id: data[i][vc],
                        text: data[i][vc]
                    };
                    ret.results[i] = rw;
                }
                cache[t] = ret;
                query.callback(ret);
            });
        }
    };

    if (_opts) {
        for (var n in _opts) {
            var v = _opts[n];
            opts[n] = v;
        }
    }
    elm.select2(opts);
};

// Deprecated
//com.roguesquadron.core.UIComponent.prototype.onPostFailure = function (jqXHR, textStatus, errorThrown) {
//    if (jqXHR.status == 500 || jqXHR.status == 0) {
//        notify.fatalNotification("System Error", "Sorry something went wrong, Please try again later.");
//    } else {
//        if (jqXHR.status == 400) {
//            notify.warnNotification("Validation error 1", "Sorry there was an error in your request, please check your data and try again.");
//        } else {
//            notify.warnNotification("Validation error 2", "Sorry there was an error in your request, please check your data and try again.");
//        }
//    }
//};

com.roguesquadron.core.UIComponent.prototype.stdFail = function () {
    var self = this;
    return function (jqXHR, textStatus, errorThrown) {
        Logger.info("Status " + jqXHR.status + " responseText " + jqXHR.responseText);
        if (jqXHR.status == 500 || jqXHR.status == 0) {
            notify.fatalNotification("System Error", "Sorry something went wrong, Please try again later.");
        } else {
            self.onServerValidationFailed(jqXHR["responseJSON"], null);
        }
    };
};


com.roguesquadron.core.UIComponent.prototype.onServerValidationFailed = function (result, model) {
//    this.find(".registration-part").effect("shake", "slow");
    if (result && result["errors"]) {
        var msg = "<ul>";
        for (var i = 0; i < result.errors.length; i++) {
            var e = result.errors[i];
            msg += "<li>" + e.message + "</li>";
            if (model) {
                model.getBinding(e.id).addClass("rs-has-warning");
            }
        }
        msg += "</ul>";

        notify.warnNotification("Please review these items", msg);
    } else {
        notify.errorNotification("System Error", "Sorry something went wrong, Please try again later.");
    }
};

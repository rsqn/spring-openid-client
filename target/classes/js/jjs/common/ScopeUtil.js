function ns(namespaceString) {
    var parts = namespaceString.split('.'),
        parent = window,
        currentPart = '';

    for(var i = 0, length = parts.length; i < length; i++) {
        currentPart = parts[i];
        parent[currentPart] = parent[currentPart] || {};
        parent = parent[currentPart];
    }

    return parent;
}

function extend(subclass, superclass) {
    subclass.prototype = new superclass;
    subclass.prototype.constructor = subclass;
    subclass.superclass = superclass;
    subclass.superproto = superclass.prototype;
}

function createDelegate(obj, method, args, appendArgs){
    return function() {
        var callArgs = args || arguments;
        if(appendArgs === true){
            callArgs = Array.prototype.slice.call(arguments, 0);
            callArgs = callArgs.concat(args);
        }else if(typeof appendArgs == "number"){
            callArgs = Array.prototype.slice.call(arguments, 0);
            var applyArgs = [appendArgs, 0].concat(args);
            Array.prototype.splice.apply(callArgs, applyArgs);
        }
        return method.apply(obj || window, callArgs);
    };
};
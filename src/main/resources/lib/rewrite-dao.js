let bean = __.newBean('com.enonic.app.rewrite.RewriteBean');


exports.store = function (rule) {

    let params = __.newBean('com.enonic.app.rewrite.StoreRuleParams');
    params.order = rule.order;
    params.target = rule.target;
    params.from = rule.from;
    params.contextKey = rule.contextKey;

    let result = bean.store(params);
    return __.toNativeObject(result);
};

exports.getVirtualHosts = function () {
    let result = bean.getVirtualHosts();
    return __.toNativeObject(result);
};

exports.storeRewriteContext = function (contextKey) {
    let result = bean.createRewriteContext(contextKey);
    return __.toNativeObject(result);
};

exports.deleteRewriteContext = function (contextKey) {
    let result = bean.deleteRewriteContext(contextKey);
    return __.toNativeObject(result);
};
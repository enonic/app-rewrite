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

exports.createRewriteContext = function (contextKey) {
    let result = bean.createRewriteContext(contextKey);
    return __.toNativeObject(result);
};

exports.deleteRewriteContext = function (contextKey) {
    let result = bean.deleteRewriteContext(contextKey);
    return __.toNativeObject(result);
};

exports.createRule = function (contextKey, rule) {
    let params = __.newBean('com.enonic.app.rewrite.CreateRuleParams');
    params.order = rule.order;
    params.source = rule.source;
    params.target = rule.target;
    params.type = rule.type;
    params.contextKey = contextKey;

    log.info("Storing rule with params: %s", rule);

    let result = bean.createRule(params);
    return __.toNativeObject(result);
};

exports.getProviderInfo = function () {
    let result = bean.getProviderInfo();
    return __.toNativeObject(result);
};
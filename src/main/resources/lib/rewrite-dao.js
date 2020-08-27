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

exports.getRewriteConfigurations = function () {
    let result = bean.getRewriteConfigurations();
    return __.toNativeObject(result);
};

exports.getRewriteContext = function (contextKey) {
    let result = bean.getRewriteContext(contextKey);
    return __.toNativeObject(result);
};

exports.getRewriteMapping = function (contextKey) {
    let result = bean.getRewriteMapping(contextKey);
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

exports.getProviderInfo = function (contextKey) {
    let result = bean.getProviderInfo(contextKey);
    return __.toNativeObject(result).providerInfo;
};

exports.createRule = function (contextKey, rule, insertStrategy) {
    let params = __.newBean('com.enonic.app.rewrite.CreateRuleParams');
    params.insertStrategy = insertStrategy;
    params.source = rule.source;
    params.target = rule.target;
    params.type = rule.type;
    params.contextKey = contextKey;
    let result = bean.createRule(params);
    return __.toNativeObject(result);
};

exports.deleteRule = function (contextKey, from) {
    let params = __.newBean('com.enonic.app.rewrite.DeleteRuleParams');
    params.contextKey = contextKey;
    params.from = from;
    let result = bean.deleteRule(params);
    return __.toNativeObject(result);
};

exports.importRules = function (contextKey, mergeStrategy, byteSource, fileName, dryRun) {
    let params = __.newBean('com.enonic.app.rewrite.ImportRulesParams');
    params.contextKey = contextKey;
    params.mergeStrategy = mergeStrategy;
    params.byteSource = byteSource;
    params.fileName = fileName;
    params.dryRun = dryRun;
    let result = bean.importRules(params);
    return __.toNativeObject(result);
};

exports.serializeRules = function (contextKey, format) {
    let params = __.newBean('com.enonic.app.rewrite.ExportRulesParams');
    params.contextKey = contextKey;
    params.format = format;
    let result = bean.serializeRules(params);
    return result;
};
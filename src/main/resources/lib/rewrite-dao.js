/* global __*/

function required(params, name) {
    let value = params[name];

    if (value === undefined) {
        throw 'Parameter \'' + name + '\' is required';
    }

    return value;
}

let bean = __.newBean('com.enonic.app.rewrite.RewriteBean');

exports.getRewriteConfigurations = function () {
    let result = bean.getRewriteConfigurations();
    return __.toNativeObject(result);
};

exports.getRewriteContext = function (contextKey) {
    let result = bean.getRewriteContext(contextKey);
    return __.toNativeObject(result);
};

exports.getRewriteContexts = function () {
    let result = bean.getRewriteContexts();
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

exports.reloadRewriteMappings = function () {
    bean.reloadRewriteMappings();
};

exports.createRule = function (host, contextKey, rule, insertStrategy, position) {
    let params = __.newBean('com.enonic.app.rewrite.UpdateRuleParams');
    params.host = host;
    params.insertStrategy = insertStrategy;
    params.source = rule.source;
    params.target = rule.target;
    params.type = rule.type;
    params.contextKey = contextKey;
    params.position = position;

    let result = bean.saveRule(params);
    return __.toNativeObject(result);
};

exports.deleteRule = function (contextKey, ruleId) {
    let params = __.newBean('com.enonic.app.rewrite.DeleteRuleParams');
    params.contextKey = contextKey;
    params.ruleId = ruleId;
    let result = bean.deleteRule(params);
    return __.toNativeObject(result);
};

exports.editRule = function (host, contextKey, pattern, rule) {
    let params = __.newBean('com.enonic.app.rewrite.UpdateRuleParams');
    params.host = host;
    params.contextKey = contextKey;
    params.source = rule.source;
    params.target = rule.target;
    params.type = rule.type;
    params.position = rule.position;
    params.ruleId = rule.ruleId;

    let result = bean.saveRule(params);
    return __.toNativeObject(result);
};


exports.importRules = function (contextKey, mergeStrategy, byteSource, fileName, dryRun, format) {
    let params = __.newBean('com.enonic.app.rewrite.ImportRulesParams');
    params.contextKey = contextKey;
    params.mergeStrategy = mergeStrategy;
    params.byteSource = byteSource;
    params.fileName = fileName;
    params.dryRun = dryRun;
    if (format) {
        params.format = format;
    }
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

exports.testRequest = function (params) {
    let testerParams = __.newBean('com.enonic.app.rewrite.RequestTesterParams');

    testerParams.host = required(params, 'host');
    testerParams.requestPath = required(params, 'requestPath');
    testerParams.rewriteContext = required(params, 'rewriteContext');

    return __.toNativeObject(bean.requestTester(testerParams));
};

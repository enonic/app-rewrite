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
    params.setHost(__.nullOrValue(host));
    params.setInsertStrategy(__.nullOrValue(insertStrategy));
    params.setSource(__.nullOrValue(rule.source));
    params.setTarget(__.nullOrValue(rule.target));
    params.setType(__.nullOrValue(rule.type));
    params.setContextKey(__.nullOrValue(contextKey));
    params.setPosition(position);

    let result = bean.saveRule(params);
    return __.toNativeObject(result);
};

exports.deleteRule = function (contextKey, ruleId) {
    let params = __.newBean('com.enonic.app.rewrite.DeleteRuleParams');
    params.setContextKey(__.nullOrValue(contextKey));
    params.setRuleId(__.nullOrValue(ruleId));
    let result = bean.deleteRule(params);
    return __.toNativeObject(result);
};

exports.editRule = function (host, contextKey, pattern, rule) {
    let params = __.newBean('com.enonic.app.rewrite.UpdateRuleParams');
    params.setHost(__.nullOrValue(host));
    params.setContextKey(__.nullOrValue(contextKey));
    params.setSource(__.nullOrValue(rule.source));
    params.setTarget(__.nullOrValue(rule.target));
    params.setType(__.nullOrValue(rule.type));
    params.setPosition(rule.position);
    params.setRuleId(__.nullOrValue(rule.ruleId));

    let result = bean.saveRule(params);
    return __.toNativeObject(result);
};


exports.importRules = function (contextKey, mergeStrategy, byteSource, fileName, dryRun, format) {
    let params = __.newBean('com.enonic.app.rewrite.ImportRulesParams');
    params.setContextKey(__.nullOrValue(contextKey));
    params.setMergeStrategy(__.nullOrValue(mergeStrategy));
    params.setByteSource(byteSource);
    params.setFileName(__.nullOrValue(fileName));
    params.setDryRun(dryRun);
    if (format) {
        params.setFormat(format);
    }
    let result = bean.importRules(params);
    return __.toNativeObject(result);
};

exports.serializeRules = function (contextKey, format) {
    let params = __.newBean('com.enonic.app.rewrite.ExportRulesParams');
    params.setContextKey(__.nullOrValue(contextKey));
    params.setFormat(format);
    let result = bean.serializeRules(params);
    return result;
};

exports.testRequest = function (params) {
    let testerParams = __.newBean('com.enonic.app.rewrite.RequestTesterParams');

    testerParams.setHost(required(params, 'host'));
    testerParams.setRequestPath(required(params, 'requestPath'));
    testerParams.setRewriteContext(required(params, 'rewriteContext'));

    return __.toNativeObject(bean.requestTester(testerParams));
};

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
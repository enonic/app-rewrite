let bean = __.newBean('com.enonic.app.rewrite.RewriteBean');


exports.getRewriteMappings = function () {
    let result = bean.getRewriteMappings();
    return __.toNativeObject(result);
};

exports.testRequest = function (requestURL) {
    let result = bean.requestTester(requestURL);
    return __.toNativeObject(result);
};
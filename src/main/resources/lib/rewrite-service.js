let bean = __.newBean('com.enonic.app.rewrite.RewriteBean');


exports.getRewriteMapping = function () {
    let result = bean.getRewriteMapping();
    return __.toNativeObject(result);
};

exports.testRequest = function (requestURL) {
    let result = bean.requestTester(requestURL);
    return __.toNativeObject(result);
};
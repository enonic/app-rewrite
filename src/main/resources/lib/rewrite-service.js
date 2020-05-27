let bean = __.newBean('com.enonic.app.rewrite.RewriteBean');

exports.testRequest = function (requestURL) {
    let result = bean.requestTester(requestURL);
    return __.toNativeObject(result);
};
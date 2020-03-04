var bean = __.newBean('com.enonic.app.rewrite.RewriteBean');


exports.getRewriteMapping = function () {
    var result = bean.getRewriteMapping();
    return __.toNativeObject(result);
};
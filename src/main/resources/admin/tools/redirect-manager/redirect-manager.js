var thymeleaf = require('/lib/thymeleaf');
var portal = require('/lib/xp/portal');
var rewriteService = require('/lib/rewrite-service');


exports.get = function (req) {

    var view = resolve('redirect-manager.html');

    var mapping = rewriteService.getRewriteMapping();

    var model = {
        assetsUrl: portal.assetUrl({path: ""}),
        mappings: mapping
    };

    log.info("Model: %s", JSON.stringify(model, null, 4));

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};

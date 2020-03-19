let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteService = require('/lib/rewrite-service');


exports.get = function (req) {

    let view = resolve('redirect-manager.html');

    let mapping = rewriteService.getRewriteMapping();

    let model = {
        assetsUrl: portal.assetUrl({path: ""}),
        mappings: mapping,
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1)
    };

    log.info("Model: %s", JSON.stringify(model, null, 4));
    let rewriteTest = rewriteService.testRequest("localhost/oldURL");
    log.info("rewriteTest: %s", JSON.stringify(rewriteTest, null, 4));


    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)

    };

};

let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteService = require('/lib/rewrite-service');
let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let view = resolve('rewrite-manager.html');

    let mapping = rewriteService.getRewriteMappings();

    let model = {
        assetsUrl: portal.assetUrl({path: ""}),
        mappings: mapping,
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        providerInfo: rewriteDao.getProviderInfo().providerInfo
    };

    //  log.info("Model: %s", JSON.stringify(model, null, 4));

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)

    };

};

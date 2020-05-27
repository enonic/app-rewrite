let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let view = resolve('tool-rules-render.html');

    let model = {
        configs: getConfigurations(),
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        toolKey: "tool-rules"
    };

    // log.info("Model: %s", JSON.stringify(model, null, 4));

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};

let getConfigurations = function () {

    let configs = [];

    let result = rewriteDao.getRewriteConfigurations();

    result.configurations.forEach(function (config) {

        let contextKey = config.contextKey;

        let mappingResult = rewriteDao.getRewriteMapping(contextKey);

        if (mappingResult) {
            configs.push({
                contextKey: contextKey,
                rules: mappingResult.mapping.rules.length
            });
        }
    });

    return configs;
};
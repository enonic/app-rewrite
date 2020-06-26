const rewriteDao = require('/lib/rewrite-dao');
const thymeleaf = require('/lib/thymeleaf');

exports.get = function (req) {

    let view = resolve('selector-options.html');

    let model = {};
    model.configs = getConfigurations();


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
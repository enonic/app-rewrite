const thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let result = rewriteDao.getRewriteConfigurations();

    log.info("##### Fetching data from tool context: %s", JSON.stringify(result, null, 2));

    let model = {
        columns: [
            {title: "VirtualHost", data: "contextKey"},
            {title: "Url", data: "url"},
            {title: "Provider", data: "provider"}
        ]
    };

    model.data = [];

    result.configurations.forEach(function (configuration) {

        let contextKey = configuration.contextKey;
        let result = rewriteDao.getRewriteContext(contextKey);
        let rewriteContext = result.rewriteContext;

        model.data.push({
            contextKey: contextKey,
            url: createUrl(rewriteContext),
            mapped: configuration.provider != null && configuration.provider !== "",
            provider: configuration.provider ? configuration.provider : createAddProviderButton(contextKey)
        });
    });

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};

let createAddProviderButton = function (contextKey) {

    let view = resolve('add-provider-button.html');

    let model = {
        buttonText: "Enable",
        contextKey: contextKey
    };

    return thymeleaf.render(view, model)
};

let createUrl = function (rewriteContext) {
    return rewriteContext.host + rewriteContext.source;
};

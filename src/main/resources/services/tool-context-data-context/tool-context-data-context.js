const thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let result = rewriteDao.getRewriteConfigurations();

    log.info("##### Fetching data from tool context: %s", JSON.stringify(result, null, 2));

    let model = {
        columns: [
            {title: "Name", data: "contextKey"},
            {title: "Url", data: "url"},
            {title: "Rules", data: "rules"},
            {title: "Provider", data: "provider"},
            {title: "Actions", data: "actions"}
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
            rules: 5,
            provider: configuration.provider ? configuration.provider : "",
            actions: configuration.provider ? "" : createAddProviderButton(contextKey)
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
        buttonText: "Enable rewrite",
        contextKey: contextKey
    };

    return thymeleaf.render(view, model)
};

let createUrl = function (rewriteContext) {
    return rewriteContext.host + rewriteContext.source;
};

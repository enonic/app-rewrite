const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let result = rewriteDao.getRewriteConfigurations();

    let model = {
        columns: [
            {title: "ContextKey", data: "contextKey"},
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
            provider: configuration.provider
        });
    });

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};

let createUrl = function (rewriteContext) {
    return rewriteContext.host + rewriteContext.source;
};

const thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

function getNumberOfRules(contextKey) {
    const mappingResult = rewriteDao.getRewriteMapping(contextKey);

    if (!mappingResult) {
        return "";
    }

    return mappingResult.mapping ? mappingResult.mapping.rules.length : "N/A";
}

function getProviderInfo(configuration, contextKey) {

    let providerInfo = {};

    if (configuration.provider && configuration.provider !== "") {
        providerInfo = rewriteDao.getProviderInfo(contextKey);
    } else {
        providerInfo.readOnly = true;
    }

    return providerInfo;
}

exports.get = function (req) {

    let result = rewriteDao.getRewriteConfigurations();

    let model = {
        columns: [
            {title: "Name", data: "contextKey"},
            {title: "Url", data: "url"},
            {title: "Rules", data: "rules"},
            {title: "Provider", data: "provider"},
            {title: "Read Only", data: "readOnly"},
            {title: "Actions", data: "actions"}
        ]
    };

    model.data = [];

    result.configurations.forEach(function (configuration) {

        let contextKey = configuration.contextKey;
        let result = rewriteDao.getRewriteContext(contextKey);
        let rewriteContext = result.rewriteContext;

        let providerInfo = getProviderInfo(configuration, contextKey);
        const rules = getNumberOfRules(contextKey);

        let hasProvider = configuration.provider && configuration.provider !== "";

        model.data.push({
            contextKey: contextKey,
            url: createUrl(rewriteContext),
            rules: rules,
            provider: hasProvider ? configuration.provider : "",
            readOnly: hasProvider ? providerInfo.readOnly : "",
            actions: createActionButtons(contextKey, configuration)
        });
    });

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};

let createActionButtons = function (contextKey, configuration) {
    let view = resolve('action-buttons.html');
    let providerInfo = getProviderInfo(configuration, contextKey);

    let model = {
        contextKey: contextKey,
        readOnly: providerInfo ? providerInfo.readOnly : true,
        hasProvider: configuration.provider && configuration.provider !== ""
    };

    return thymeleaf.render(view, model)
};

let createUrl = function (rewriteContext) {
    return rewriteContext.host + rewriteContext.source;
};

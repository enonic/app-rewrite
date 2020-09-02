const rewriteDao = require('/lib/rewrite-dao');
const thymeleaf = require('/lib/thymeleaf');

exports.get = function (req) {

    let contextKey = req.params.contextKey;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let model = {
        columns: [
            {width: "30px", title: "Order", data: "order"},
            {title: "Pattern", data: "from"},
            {title: "Substitution", data: "target.path"},
            {title: "Redirect-type", data: "type"},
            {title: "Actions", data: "actions"}
        ]
    };

    let result = rewriteDao.getRewriteMapping(contextKey);

    if (!result.mapping) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "No mapping found: "
        }
    }

    model.data = [];


    result.mapping.rules.forEach(function (rule) {
        const actions = generateActions(contextKey, rule);
        rule.actions = actions;
        model.data.push(rule);
    });

    model.data = result.mapping.rules;

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};


let generateActions = function (contextKey, rule) {

    let view = resolve('action-buttons.html');

    let providerInfo = rewriteDao.getProviderInfo(contextKey);

    let model = {
        contextKey: contextKey,
        from: rule.from,
        readOnly: providerInfo ? providerInfo.readOnly : true
    };

    return thymeleaf.render(view, model)
};

const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let contextKey = req.params.contextKey;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let model = {
        columns: [
            {title: "Order", data: "order"},
            {title: "From", data: "from"},
            {title: "Target", data: "target.path"},
            {title: "External", data: "target.external"},
            {title: "Type", data: "type"},
        ]
    };

    let result = rewriteDao.getRewriteMapping(contextKey);
    log.info("#####RESULT: %s", JSON.stringify(result, null, 3));

    if (!result.mapping) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "No mapping found: "
        }
    }

    model.data = result.mapping.rules;

    log.info("Model: %s", JSON.stringify(model, null, 4));

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};
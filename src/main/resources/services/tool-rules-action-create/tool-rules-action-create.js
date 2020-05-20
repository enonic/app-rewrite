const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let contextKey = params.createRuleContextKey;

    log.info("Params: %s", JSON.stringify(params));

    let rule = {
        source: params.createRuleSource,
        target: params.createRuleTarget,
        type: params.createRuleType,
        order: params.createRuleOrder,
    };

    if (!contextKey) {
        let model = {
            error: "context-key is missing"
        };

        return {
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify(model)
        }
    }

    try {
        rewriteDao.createRule(contextKey, rule);
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot create context: " + e
        }
    }

    let model = {
        message: "context with key [" + contextKey + "] created",
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};
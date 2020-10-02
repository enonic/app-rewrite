const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let contextKey = params.createRuleContextKey;

    let rule = {
        source: params.createRuleSource,
        target: params.createRuleTarget,
        type: params.createRuleType,
    };

    let insertStrategy = params.insertStrategy;
    let insertPosition = params.insertPosition;

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
        rewriteDao.createRule(contextKey, rule, insertStrategy, insertPosition);
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

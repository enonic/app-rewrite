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
        let result = rewriteDao.createRule(params.__host, contextKey, rule, insertStrategy, insertPosition);

        if (result !== null && result.error) {
            return {
                status: 500,
                contentType: 'text/plain',
                body: "cannot create rule: " + result.error.message
            }
        }
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot create rule: " + e
        }
    }

    let model = {
        message: 'The rule was successfully created for virtual host "' + contextKey + '"',
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};

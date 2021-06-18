const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let pattern = params.originalRulePattern;
    let contextKey = params.editRuleContextKey;

    try {
        let result = rewriteDao.editRule(params.__host, contextKey, pattern, {
            source: params.editRulePattern,
            target: params.editRuleSubstitution,
            type: params.editRuleType,
            position: params.editInsertPosition,
            ruleId: params.editRuleId
        });

        if (result !== null && result.error) {
            return {
                status: 500,
                contentType: 'text/plain',
                body: "cannot update context: " + result.error.message
            }
        }
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot update context: " + e
        }
    }

    let model = {
        message: "rule with context [" + contextKey + "], from [" + pattern + "] updated",
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};

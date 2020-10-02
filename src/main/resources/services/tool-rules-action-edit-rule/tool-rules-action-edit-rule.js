const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let pattern = params.originalRulePattern;
    let contextKey = params.editRuleContextKey;

    rewriteDao.editRule(contextKey, pattern, {
        pattern: params.editRulePattern,
        substitution: params.editRuleSubstitution,
        type: params.editRuleType,
        position: params.editInsertPosition,
        ruleId: params.editRuleId
    });

    let model = {
        message: "rule with context [" + contextKey + "], from [" + pattern + "] updated",
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};

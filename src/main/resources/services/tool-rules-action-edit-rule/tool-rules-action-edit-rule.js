const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let pattern = params.originalRulePattern;
    let newPattern = params.editRulePattern;
    let substitution = params.editRuleSubstitution;
    let type = params.editRuleType;
    let contextKey = params.editRuleContextKey;

    log.info("PARAMS: %s", JSON.stringify(params, null, 4));

    rewriteDao.editRule(contextKey, pattern, {
        pattern: newPattern,
        substitution: substitution,
        type: type
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
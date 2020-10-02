let dao = require("rewrite-dao");

exports.getRule = function (contextKey, ruleId) {

    const result = dao.getRewriteMapping(contextKey);

    if (!result) {
        return null;
    }

    const rules = result.mapping.rules;
    if (!rules) {
        return null;
    }

    for (let i = 0; i < rules.length; i++) {
        if (rules[i].ruleId === ruleId) {
            return rules[i];
        }
    }

    return null;
};

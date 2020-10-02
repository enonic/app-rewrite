let thymeleaf = require('/lib/thymeleaf');
const rewriteService = require('/lib/rewrite-service');
const rewriteDao = require('/lib/rewrite-dao');
const helpers = require('/lib/helpers');


function createSourceUrl(result) {
    return "https://" + result.rewriteContext.host + "" + result.rewriteContext.source;
}


exports.get = function (req) {

    let params = req.params;
    let contextKey = params.actionContext;
    let identifier = params.identifier;

    if (!contextKey) {
        return helpers.returnMissingValueError("context")
    } else if (!identifier) {
        return helpers.returnMissingValueError("identifier")
    }

    let rule = rewriteService.getRule(contextKey, identifier);

    if (!rule) {
        return helpers.returnMissingValueError("Rule with identifier: " + identifier);
    }

    let url = createSourceUrl(rewriteDao.getRewriteContext(contextKey));

    let ruleMappings = rewriteDao.getRewriteMapping(contextKey);

    let maxPosition = ruleMappings.mapping ? ruleMappings.mapping.rules.length - 1 : 0;

    let model = {
        form: {
            id: "tool-rules-action-edit-form",
        },
        contextKey: contextKey,
        source: {
            url: url,
            maxPosition: maxPosition
        },
        rule: {
            ruleId: rule.ruleId,
            pattern: rule.from,
            substitution: rule.target.path,
            type: rule.type,
            order: rule.order
        },
        selected: rule.type
    };

    let view = resolve('tool-rules-modal-edit.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model),
        message: "Rule created"
    };
};

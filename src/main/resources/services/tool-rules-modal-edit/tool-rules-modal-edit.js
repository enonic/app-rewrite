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
    let pattern = params.identifier;

    if (!contextKey) {
        return helpers.returnMissingValueError("context")
    } else if (!pattern) {
        return helpers.returnMissingValueError("context")
    }

    let rule = rewriteService.getRule(contextKey, pattern);

    if (!rule) {
        return helpers.returnMissingValueError("Rule with pattern: " + pattern);
    }

    log.info("RULE: %s", JSON.stringify(rule, null, 4));

    let url = createSourceUrl(rewriteDao.getRewriteContext(contextKey));

    let model = {
        form: {
            id: "tool-rules-action-edit-form",
        },
        contextKey: contextKey,
        source: {
            url: url
        },
        rule: {
            pattern: rule.from,
            substitution: rule.target.path
        }
    };

    let view = resolve('tool-rules-modal-edit.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model),
        message: "Rule created"
    };
};
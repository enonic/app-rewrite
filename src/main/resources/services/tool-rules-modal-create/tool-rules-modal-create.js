let thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

function createSourceUrl(result) {
    return "https://" + result.rewriteContext.host + "" + result.rewriteContext.source;
}

exports.get = function (req) {
    let params = req.params;
    let contextKey = params.toolRulesContextSelector;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "No context given"
        }
    }

    let result = rewriteDao.getRewriteContext(contextKey);
    log.info("getRewriteContext: %s", JSON.stringify(result, null, 2));

    let url = createSourceUrl(result);

    let model = {
        form: {
            id: "tool-rules-action-create-form",
        },
        contextKey: contextKey,
        source: {
            url: url
        }
    };

    let view = resolve('tool-rules-modal-create.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model),
        message: "Rule created"
    };
};
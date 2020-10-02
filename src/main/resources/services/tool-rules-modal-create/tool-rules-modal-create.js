let thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

function createSourceUrl(result) {
    return "https://" + result.rewriteContext.host + "" + result.rewriteContext.source;
}

exports.get = function (req) {
    let params = req.params;
    let contextKey = params.contextKey;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }

    let result = rewriteDao.getRewriteContext(contextKey);

    let ruleMappings = rewriteDao.getRewriteMapping(contextKey);

    let maxPosition = ruleMappings.mapping ? ruleMappings.mapping.rules.length : 0;

    let url = createSourceUrl(result);

    let model = {
        form: {
            id: "tool-rules-action-create-form",
        },
        contextKey: contextKey,
        source: {
            url: url,
            maxPosition: maxPosition
        }
    };

    let view = resolve('tool-rules-modal-create.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model),
        message: "Rule created"
    };
};

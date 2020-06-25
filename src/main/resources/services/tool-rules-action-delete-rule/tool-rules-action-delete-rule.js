const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;

    let from = params.identifier;
    let contextKey = params.actionContext;

    try {
        const deleteRule = rewriteDao.deleteRule(contextKey, from)
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot create context: " + e
        }
    }

    let model = {
        message: "rule with context [" + contextKey + "], from [" + from + "] deleted",
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};
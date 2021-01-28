let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;
    let contextKey = params.actionContext;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'application/json',
            body: "cannot delete context, param contextKey missing"
        }
    }

    try {
        rewriteDao.deleteRewriteContext(contextKey);
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot delete context: " + e
        }
    }

    let model = {
        message: 'Virtual host "' + contextKey + '" deleted',
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }

};

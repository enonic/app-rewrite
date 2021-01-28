const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {
    let params = req.params;
    let contextKey = params.actionContext;
    if (!contextKey) {
        let model = {
            error: "context-key is missing"
        };

        return {
            status: 500,
            contentType: 'application/json',
            body: JSON.stringify(model)
        }
    }

    try {
        rewriteDao.createRewriteContext(contextKey);
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot create context: " + e
        }
    }

    let model = {
        message: 'Virtual host "' + contextKey + '" created',
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }

};

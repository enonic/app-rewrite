let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {
    let contextKey = req.params.contextKey;

    let result = rewriteDao.getRewriteContext(contextKey);

    return {
        status: 200,
        body: {
            "status": result.rewriteContext.active
        },
        contentType: 'application/json'
    }
};

const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;

    log.info("Params: %s", JSON.stringify(params, null, 2));

    let contextName = params.createContextName;

    log.info("Creating entry for contextKey: " + contextName);

    let result = rewriteDao.storeRewriteContext(contextName);

    log.info("Result: %s", JSON.stringify(result, null, 2));

    return {
        status: 200,
        contentType: 'application/json',
        result: result
    }
};
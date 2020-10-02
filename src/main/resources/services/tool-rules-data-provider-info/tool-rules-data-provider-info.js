const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let contextKey = req.params.contextKey;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let model = rewriteDao.getProviderInfo(contextKey);

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};

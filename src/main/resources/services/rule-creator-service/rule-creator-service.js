const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let rule = {
        contextKey: req.params.contextKey,
        from: req.params.from,
        target: req.params.target,
        order: req.params.order
    };

    rewriteDao.store(rule);

    return {
        status: 200,
        contentType: 'application/json'
    }

};
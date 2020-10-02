const rewriteDao = require('/lib/rewrite-dao');

exports.post = function (req) {

    let params = req.params;

    let ruleId = params.identifier;
    let contextKey = params.actionContext;

    try {
        rewriteDao.deleteRule(contextKey, ruleId)
    } catch (e) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "cannot create context: " + e
        }
    }

    let model = {
        message: "The rule was successfully deleted"
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};

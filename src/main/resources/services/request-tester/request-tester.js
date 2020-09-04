let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let requestURL = req.params.requestURL;

    if (!requestURL) {
        return {
            status: 500,
            body: {
                error: "missing request-url"
            },
            contentType: 'application/json'
        }
    }

    let result = rewriteDao.testRequest(requestURL);

    if (result.error) {

        log.error("Error in request tester: %s", JSON.stringify(result.error, null, 2));

        return {
            status: 500,
            body: result.error,
            contentType: 'application/json'
        }
    }

    return {
        status: 200,
        body: result,
        contentType: 'application/json'
    }

};
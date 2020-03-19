let rewriteService = require('/lib/rewrite-service');


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

    let result = rewriteService.testRequest(requestURL);

    if (result.error) {

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
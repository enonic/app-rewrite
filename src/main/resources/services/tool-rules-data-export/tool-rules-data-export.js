let thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

function createSourceUrl(result) {
    return "https://" + result.rewriteContext.host + "" + result.rewriteContext.source;
}

exports.get = function (req) {
    let params = req.params;
    let contextKey = params.contextKey;

    log.info("############# Exporting rules");

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }

    let result = rewriteDao.getRewriteMapping(contextKey);

    return {
        contentType: 'text/plain',
        headers: {
            'Content-Disposition': 'attachment'
        },
        status: 200,
        body: result
    }
};
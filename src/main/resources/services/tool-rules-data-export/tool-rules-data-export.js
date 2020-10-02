const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {
    let params = req.params;
    let data = JSON.parse(params.data);
    let contextKey = data.contextKey;
    let exportFormat = data.exportFormat;
    let fileName = data.fileName;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }

    let result = rewriteDao.serializeRules(contextKey, exportFormat);

    return {
        contentType: 'text/plain',
        headers: {
            'Content-Disposition': 'attachment;filename="' + fileName + '"'
        },
        status: 200,
        body: result
    }
};

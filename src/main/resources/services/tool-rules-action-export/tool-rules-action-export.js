const rewriteDao = require('/lib/rewrite-dao');
const ioLib = require('/lib/xp/io');
const portalLib = require('/lib/xp/portal');

exports.post = function (req) {
    let params = req.params;
    let contextKey = params.exportRulesContextKey;
    let exportFormat = params.exportFormat;
    let fileName = params.fileName ? params.fileName : "rewrites-" + contextKey;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }


    let serviceParams = {
        contextKey: contextKey,
        exportFormat: exportFormat,
        fileName: createFileName(fileName, exportFormat)
    };

    let model = {
        data: JSON.stringify(serviceParams)
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};

let createFileName = function (baseName, exportFormat) {
    let postFix = exportFormat === "apache_rewrite" ? "conf" : "csv";
    return baseName + "." + postFix;
}
const rewriteDao = require('/lib/rewrite-dao');
const ioLib = require('/lib/xp/io');
const portalLib = require('/lib/xp/portal');

exports.post = function (req) {
    let params = req.params;

    log.info("PARAMS %s", JSON.stringify(params, null, 4));

    let contextKey = params.importRulesContextKey;
    let dryRun = params.importRulesDryRun === 'true';
    let mergeStrategy = params.toolRulesMergeStrategy ? params.toolRulesMergeStrategy : "delete";
    let importFormat = params.importFormat;

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }

    let byteSource = portalLib.getMultipartStream("file");
    const multipartItem = portalLib.getMultipartItem('file');
    const importResult = rewriteDao.importRules(contextKey, mergeStrategy, byteSource, multipartItem.fileName, dryRun,
        importFormat === "auto" ? null : importFormat);

    let model = {
        message: "Importing rules to virtualHost [" + contextKey + "] preview",
        result: importResult
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: model
    }
};
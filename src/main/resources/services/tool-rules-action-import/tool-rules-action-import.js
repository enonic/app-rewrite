const rewriteDao = require('/lib/rewrite-dao');
const ioLib = require('/lib/xp/io');
const portalLib = require('/lib/xp/portal');

exports.post = function (req) {
    let params = req.params;
    let contextKey = params.importRulesContextKey;
    let dryRun = params.importRulesDryRun === 'true';
    let mergeStrategy = params.toolRulesMergeStrategy ? params.toolRulesMergeStrategy : "delete";

    if (!contextKey) {
        return {
            status: 500,
            contentType: 'text/plain',
            body: "Missing context"
        }
    }

    let byteSource = portalLib.getMultipartStream("file");

    const importResult = rewriteDao.importRules(contextKey, mergeStrategy, byteSource, dryRun);

    let model = {
        message: "imported rules to virtualHost [" + contextKey + "]",
        result: importResult
    };

    return {
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify(model)
    }
};
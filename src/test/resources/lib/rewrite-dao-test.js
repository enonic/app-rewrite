var rewriteDao = require('/lib/rewrite-dao');
var assert = require('/lib/xp/testing');

exports.testCreateRule = function () {
    var rule = {
        source: '/old',
        target: '/new',
        type: 'permanent'
    };

    var result = rewriteDao.createRule('create.example.com', 'createvhost', rule, 'BOTTOM', 3);

    assert.assertNull(result);
    assert.assertEquals('create.example.com', testInstance.getSavedHost());
    assert.assertEquals('BOTTOM', testInstance.getSavedInsertStrategy());
    assert.assertEquals('/old', testInstance.getSavedSource());
    assert.assertEquals('/new', testInstance.getSavedTarget());
    assert.assertEquals('permanent', testInstance.getSavedType());
    assert.assertEquals('createvhost', testInstance.getSavedContextKey());
    assert.assertEquals(3, testInstance.getSavedPosition());
};

exports.testEditRule = function () {
    var rule = {
        source: '/from',
        target: '/to',
        type: 'forward',
        position: 5,
        ruleId: 'rule-123'
    };

    var result = rewriteDao.editRule('edit.example.com', 'editvhost', '/pattern', rule);

    assert.assertNull(result);
    assert.assertEquals('edit.example.com', testInstance.getSavedHost());
    assert.assertEquals('editvhost', testInstance.getSavedContextKey());
    assert.assertEquals('/from', testInstance.getSavedSource());
    assert.assertEquals('/to', testInstance.getSavedTarget());
    assert.assertEquals('forward', testInstance.getSavedType());
    assert.assertEquals(5, testInstance.getSavedPosition());
    assert.assertEquals('rule-123', testInstance.getSavedRuleId());
};

exports.testDeleteRule = function () {
    var result = rewriteDao.deleteRule('deletevhost', 'rule-999');

    assert.assertNull(result);
    assert.assertEquals('deletevhost', testInstance.getDeletedContextKey());
    assert.assertEquals('rule-999', testInstance.getDeletedRuleId());
};

exports.testImportRules = function () {
    var result = rewriteDao.importRules('importvhost', 'overwrite', null, 'rules.csv', true, 'csv');

    assert.assertNotNull(result);
    assert.assertEquals('importvhost', testInstance.getImportedContextKey());
    assert.assertEquals('overwrite', testInstance.getImportedMergeStrategy());
    assert.assertEquals('rules.csv', testInstance.getImportedFileName());
    assert.assertEquals(true, testInstance.getImportedDryRun());
    assert.assertEquals('csv', testInstance.getImportedFormat());
};

exports.testSerializeRules = function () {
    var result = rewriteDao.serializeRules('exportvhost', 'csv');

    assert.assertEquals('SERIALIZED_RULES', result);
    assert.assertEquals('exportvhost', testInstance.getExportedContextKey());
    assert.assertEquals('CSV', testInstance.getExportedFormat());
};

exports.testTestRequest = function () {
    var params = {
        host: 'tester.example.com',
        requestPath: '/some/path',
        rewriteContext: 'testervhost'
    };

    var result = rewriteDao.testRequest(params);

    assert.assertNotNull(result);
    assert.assertEquals('OK', result.state);
    assert.assertEquals('tester.example.com', testInstance.getTestedHost());
    assert.assertEquals('/some/path', testInstance.getTestedRequestPath());
    assert.assertEquals('testervhost', testInstance.getTestedRewriteContext());
};

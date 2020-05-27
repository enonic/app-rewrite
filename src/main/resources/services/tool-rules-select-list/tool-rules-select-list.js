const thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let contextKey = req.params.id;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let configurations = rewriteDao.getRewriteConfigurations();


    let model = {};

    model.mapping = mappings[mappingKey];


    let view = resolve('tool-rules-select-list.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };
};
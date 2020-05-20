let thymeleaf = require('/lib/thymeleaf');
const rewriteService = require('/lib/rewrite-service');

exports.get = function (req) {

    let contextKey = req.params.id;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    log.info("ContextKey:" + contextKey);

    let mappings = rewriteService.getRewriteMappings();


    let model = {
        columns: [
            "order",
            "from",
            "target",
            "external",
            "type"
        ]
    };


    for (let mappingKey in mappings) {

        log.info("####### MappingKey: %s, contextKey: %s", mappingKey, contextKey);

        if (mappings.hasOwnProperty(mappingKey)) {
            if (contextKey === mappingKey) {
                model.data = mappings[mappingKey].rules;
            }
        }
    }


    let view = resolve('tool-rules-select-list.html');

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};
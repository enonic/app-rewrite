let thymeleaf = require('/lib/thymeleaf');
const rewriteService = require('/lib/rewrite-service');

exports.get = function (req) {

    let contextKey = req.params.id;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let mappings = rewriteService.getRewriteMappings();

    let model = {
        columns: [
            {title: "Order", data: "order"},
            {title: "From", data: "from"},
            {title: "Target", data: "target.path"},
            {title: "External", data: "target.external"},
            {title: "Type", data: "type"},
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

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};
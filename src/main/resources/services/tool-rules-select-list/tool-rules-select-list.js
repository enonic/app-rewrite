let thymeleaf = require('/lib/thymeleaf');
const rewriteService = require('/lib/rewrite-service');

exports.get = function (req) {

    let contextKey = req.params.id;

    if (!contextKey) {
        throw "contextKey is missing";
    }

    let mappings = rewriteService.getRewriteMappings();


    let model = {};

    for (let mappingKey in mappings) {

        log.info("####### MappingKey: %s, contextKey: %s", mappingKey, contextKey);

        if (mappings.hasOwnProperty(mappingKey)) {
            if (contextKey === mappingKey) {
                model.mapping = mappings[mappingKey];
            }
        }
    }


    let view = resolve('tool-rules-select-list.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };
};
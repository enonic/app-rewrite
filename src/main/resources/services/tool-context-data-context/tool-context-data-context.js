let thymeleaf = require('/lib/thymeleaf');
const rewriteService = require('/lib/rewrite-service');

exports.get = function (req) {

    let mappings = rewriteService.getRewriteMappings();
    log.info("Mapping: %s", JSON.stringify(mappings, null, 2));

    let model = {
        columns: [
            {title: "Order", data: "contextKey"},
            {title: "Host", data: "host"},
            {title: "Source", data: "source"}
        ]
    };

    model.data = [];
    for (let mappingKey in mappings) {

        if (mappings.hasOwnProperty(mappingKey)) {
            model.data.push({
                contextKey: mappingKey,
                host: mappings[mappingKey].host,
                source: mappings[mappingKey].source
            });
        }
    }

    return {
        contentType: 'application/json',
        status: 200,
        body: model
    };
};
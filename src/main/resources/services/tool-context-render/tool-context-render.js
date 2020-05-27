let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');

exports.get = function (req) {

    let view = resolve('tool-context-render.html');

    let model = {
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        toolKey: "tool-context"
    };

    //  log.info("Model: %s", JSON.stringify(model, null, 4));

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};
let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let view = resolve('tool-rules-render.html');

    let model = {
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        toolKey: "tool-rules"
    };


    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};

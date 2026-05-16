let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let assetLib = require('/lib/enonic/asset');

exports.get = function (req) {

    let view = resolve('tool-rules-render.html');

    let model = {
        assetsUrl: assetLib.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        toolKey: "tool-rules"
    };


    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};

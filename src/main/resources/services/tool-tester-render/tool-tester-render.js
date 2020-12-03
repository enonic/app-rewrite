let thymeleaf = require('/lib/thymeleaf');
let portal = require('/lib/xp/portal');
let rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let view = resolve('tool-tester-render.html');

    let rewriteContexts = rewriteDao.getRewriteContexts();

    let contexts = [];

    rewriteContexts.rewriteContexts.forEach(function (item) {
        let rewriteContext = item.rewriteContext;
        contexts.push({
            name: rewriteContext.name,
            inactive: !rewriteContext.active
        });
    });

    let model = {
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        toolKey: "tool-tester",
        rewriteContexts: contexts
    };


    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };

};

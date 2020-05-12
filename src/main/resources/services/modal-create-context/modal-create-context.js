let thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let params = req.params;

    let result = rewriteDao.getVirtualHosts();

    let model = {
        virtualHosts: result.virtualHosts
    };

    log.info("MODEL %s", JSON.stringify(model, null, 3));

    let view = resolve('modal-create-context.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)

    };
};
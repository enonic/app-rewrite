let thymeleaf = require('/lib/thymeleaf');
const rewriteDao = require('/lib/rewrite-dao');

exports.get = function (req) {

    let result = rewriteDao.getVirtualHosts();

    let model = {
        virtualHosts: result.virtualHosts,
        form: {
            id: "tool-context-action-create-form",
        }
    };

    let view = resolve('tool-context-modal-create.html');

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)
    };
};
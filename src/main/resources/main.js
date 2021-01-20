const eventLib = require('/lib/xp/event');
const rewriteDao = require('/lib/rewrite-dao');

eventLib.listener({
    type: 'node.*',
    callback: function (event) {
        if (event.localOrigin === false) {
            event.data.nodes.some(node => {
                if (node.repo === 'com.enonic.app.rewrite') {
                    rewriteDao.reloadRewriteMappings();
                    return true;
                }
            });
        }
    }
});

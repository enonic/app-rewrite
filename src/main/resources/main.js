const eventLib = require('/lib/xp/event');
const rewriteDao = require('/lib/rewrite-dao');
const licenseManagerLib = require('/lib/license-manager');
const authLib = require('/lib/xp/auth');
const contextLib = require('/lib/xp/context');
const clusterLib = require('/lib/xp/cluster');

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

if (licenseManagerLib.isCurrentLicenseValid()) {
    licenseManagerLib.activateLicense();
}

if(clusterLib.isMaster()) {
    contextLib.run({
        principals: ['role:system.admin'],
        }, () => {
            if (!authLib.getPrincipal('role:com.enonic.app.rewrite.admin')) {
                authLib.createRole({
                    name: 'com.enonic.app.rewrite.admin',
                    displayName: 'Rewrite Manager',
                    description: 'Users with this role has access to the Rewrite Manager app.'
                });
            }
        }
    );
}

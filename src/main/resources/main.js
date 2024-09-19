const eventLib = require('/lib/xp/event');
const rewriteDao = require('/lib/rewrite-dao');
const licenseManagerLib = require('/lib/license-manager');
const authLib = require('/lib/xp/auth');
const contextLib = require('/lib/xp/context');

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

contextLib.run({
    principals: ['role:system.admin'],
    }, () => {
        if (!authLib.getPrincipal('role:com.enonic.xp.rewritemanager')) {
            authLib.createRole({
                name: 'com.enonic.xp.rewritemanager',
                displayName: 'Rewrite Manager',
                description: 'Users with this role has access to the Rewrite Manager app.'
            });
        }
    }
);

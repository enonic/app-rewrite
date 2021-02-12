const eventLib = require('/lib/xp/event');
const rewriteDao = require('/lib/rewrite-dao');
const licenseManagerLib = require('/lib/license-manager');
const licenseLib = require("/lib/license");

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

const licenseDetail = licenseLib.validateLicense({
    appKey: app.name,
});

if (licenseDetail !== undefined && licenseDetail !== null) {
    licenseManagerLib.activateLicense();
}

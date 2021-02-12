const thymeleaf = require('/lib/thymeleaf');
const portal = require('/lib/xp/portal');
const license = require("/lib/license");
const libVHost = require('/lib/xp/vhost');

exports.get = function (req) {
    const licenseDetail = license.validateLicense({
        appKey: app.name,
    });

    const licenseUrl = portal.serviceUrl({
        service: "license",
        type: "absolute",
    })

    const view = resolve('rewrite-manager.html');
    const licenseValid = !(licenseDetail == null || licenseDetail.expired);
    let errorMessage = '';

    if (licenseValid) {
        if (JSON.stringify(app.config) == '{}') {
            errorMessage = 'Application config is not found. Rewrites are disabled';
        } else if (!app.config["enabled"] || app.config["enabled"] !== 'true') {
            errorMessage = 'Rewrites are disabled in the application config.';
        } else if (!libVHost.isEnabled()) {
            errorMessage = 'Mapping is disabled in the VHost config file. Rewrites are disabled';
        }
    }

    const model = {
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        error: !licenseValid || !!errorMessage,
        errorMessage,
        licenseUrl,
        licenseValid,
        licenseText: licenseDetail ? `Licensed to ${licenseDetail.issuedTo}` : "Invalid license",
    };

    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)

    };

};

const thymeleaf = require('/lib/thymeleaf');
const portal = require('/lib/xp/portal');
const license = require("/lib/license");

exports.get = function (req) {
    const licenseDetail = license.validateLicense({
        appKey: app.name,
    });
    const view = resolve('rewrite-manager.html');

    const model = {
        assetsUrl: portal.assetUrl({path: ""}),
        svcUrl: portal.serviceUrl({service: 'Z'}).slice(0, -1),
        licenseValid: !(licenseDetail == null || licenseDetail.expired),
        licenseText: licenseDetail ? `Licensed to ${licenseDetail.issuedTo}` : "Error",
    };


    return {
        contentType: 'text/html',
        body: thymeleaf.render(view, model)

    };

};

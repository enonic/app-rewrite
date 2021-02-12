const portalLib = require("/lib/xp/portal");
const ioLib = require("/lib/xp/io");
const licenseLib = require("/lib/license");
const licenseManagerLib = require('/lib/license-manager');

exports.post = function () {
    let licenseStream = portalLib.getMultipartStream("license");
    let license = ioLib.readText(licenseStream);

    const licenseDetails = licenseLib.validateLicense({
        license,
        appKey: app.name,
    });

    const isValid = licenseDetails && !licenseDetails.expired;

    if (isValid) {
        licenseLib.installLicense({
            license: license,
            appKey: app.name,
        });

        licenseManagerLib.activateLicense();

        return {
            status: 200,
            contentType: "application/json",
            body: {
                licenseValid: true
            },
        };
    } else {
        return {
            status: 500,
            body: {
                licenseValid: false
            },
        };
    }
};

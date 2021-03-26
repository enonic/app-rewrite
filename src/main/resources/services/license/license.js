const portalLib = require("/lib/xp/portal");
const ioLib = require("/lib/xp/io");
const licenseManager = require('/lib/license-manager');

exports.post = function () {
    let licenseStream = portalLib.getMultipartStream("license");
    let license = ioLib.readText(licenseStream);

    const licenseInstalled = licenseManager.installLicense(license);

    if (licenseInstalled) {

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

/* global __*/

const licenseLib = require("/lib/license");

const subscriptionKey = 'enonic.platform.subscription';

const getLicenseDetails = function (license) {
    const params = {
        appKey: subscriptionKey,
    };
    if (license) {
        params.license = license;
    }

    return licenseLib.validateLicense(params);
}

const isCurrentLicenseValid = function () {
    const licenseDetails = getLicenseDetails();

    return licenseDetails && !licenseDetails.expired;
}

const isLicenseValid = function (license) {
    const licenseDetails = getLicenseDetails(license);

    return licenseDetails && !licenseDetails.expired;
}

const activateLicense = function () {
    const bean = __.newBean('com.enonic.app.rewrite.LicenceManagerBean');
    bean.activateLicense();
};

exports.isCurrentLicenseValid = isCurrentLicenseValid;
exports.isLicenseValid = isLicenseValid;

exports.getIssuedTo = function () {
    if (!isCurrentLicenseValid()) {
        return 'Invalid license';
    }

    return 'Issued to ' + getLicenseDetails().issuedTo;
}

exports.installLicense = function (license) {
    if (!isLicenseValid(license)) {
        return false;
    }

    licenseLib.installLicense({
        license: license,
        appKey: subscriptionKey,
    });

    activateLicense();

    return true;
}

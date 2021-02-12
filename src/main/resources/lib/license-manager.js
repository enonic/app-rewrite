/* global __*/

exports.activateLicense = function () {
    const bean = __.newBean('com.enonic.app.rewrite.LicenceManagerBean');
    bean.activateLicense();
};

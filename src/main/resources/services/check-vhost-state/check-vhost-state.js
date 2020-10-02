let libVHost = require('/lib/xp/vhost');


exports.get = function (req) {
    return {
        status: 200,
        body: libVHost.isEnabled(),
        contentType: 'application/json'
    }
};

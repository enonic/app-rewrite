exports.returnMissingValueError = function (name) {
    return {
        status: 500,
        contentType: 'text/plain',
        body: "Missing value [" + name + "]"
    }
};

exports.returnMissingDataError = function (name) {
    return {
        status: 500,
        contentType: 'text/plain',
        body: "Entity not found [" + name + "]"
    }
};
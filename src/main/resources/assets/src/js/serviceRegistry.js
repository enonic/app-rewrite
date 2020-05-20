export let createActionServiceUrl = function (svcUrl, toolKey, actionType) {
    return doCreateUrl(svcUrl, toolKey, "action", actionType);
};

export let createDataServiceUrl = function (svcUrl, toolKey, serviceName) {
    return doCreateUrl(svcUrl, toolKey, "data", serviceName);
};

export let createToolRendererUrl = function (svcUrl, toolKey) {
    return doCreateUrl(svcUrl, toolKey, "render");
};

export let createModalUrl = function (svcUrl, toolKey, modalType) {
    return doCreateUrl(svcUrl, toolKey, "modal", modalType);
};

export let createModalSelector = function (toolKey, modalType) {
    return "#" + toolKey + "-modal-" + modalType;
};

let doCreateUrl = function (svcUrl, toolKey, verb, target) {
    return svcUrl + toolKey + "-" + verb + (target ? "-" + target : "");
};
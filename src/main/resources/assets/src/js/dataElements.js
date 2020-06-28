import {showError} from "./info-bar";


export let populateDataElement = function (selector, serviceUrl, dataFunction, onPopulated, onRefreshed) {
    jQuery.ajax({
        url: serviceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response) {
            showError(response.responseText);
        },
        success: function (response) {
            storeDataElementProperties(selector, serviceUrl, dataFunction, onRefreshed);
            onPopulated(response);
        }
    });
};

export let refreshDataElement = function (refreshSelectors) {
    $(refreshSelectors).each(function () {
        let serviceUrl = $(this).data("serviceUrl");
        let dataFunction = $(this).data("dataFunction");
        let onDataRefresh = $(this).data("onDataRefresh");
        let selector = $(this).data("selector");
        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'GET',
            data: dataFunction(),
            error: function (response) {
                showError(response.responseText);
            },
            success: function (response) {
                onDataRefresh(selector, response);
            }
        });
    });

};

function storeDataElementProperties(selector, serviceUrl, dataFunction, onTableRefresh) {
    $(selector).data("serviceUrl", serviceUrl);
    $(selector).data("dataFunction", dataFunction);
    $(selector).data("onDataRefresh", onTableRefresh);
    $(selector).data("selector", selector);
}


import {showError} from "./info-bar";


export let populateDataElement = function (selector, serviceUrl, dataFunction, onPopulated, onRefreshed) {

    console.log("Populating data-element", selector, serviceUrl, dataFunction, onPopulated, onRefreshed);

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

    console.log("Refresh data-element for selector", refreshSelectors);

    $(refreshSelectors).each(function () {
        let element = $(this);
        let serviceUrl = element.data("serviceUrl");
        let dataFunction = element.data("dataFunction");
        let onDataRefresh = element.data("onDataRefresh");

        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'GET',
            data: dataFunction(),
            error: function (response) {
                showError(response.responseText);
            },
            success: function (response) {
                console.log("Element: ", element);
                onDataRefresh(element, response);
            }
        });
    });

};

function storeDataElementProperties(selector, serviceUrl, dataFunction, onTableRefresh) {
    $(selector).data("serviceUrl", serviceUrl);
    $(selector).data("dataFunction", dataFunction);
    $(selector).data("onDataRefresh", onTableRefresh)
}


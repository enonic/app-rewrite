// Data-triggers is elements that when they are acted upon (clicked / changed) populates a data-display-element

/* Input:

    service to call -> toolKey, datafetcher, name
    parameters ?
    selector to populate -> typically html-element id


*/


import {model} from "./model";
import {createDataServiceUrl} from "./serviceRegistry";
import {showError, showInfo} from "./info-bar";

export let initDataTriggers = function (toolKey, svcUrl) {

    console.log("Init selects");

    let toolSelector = "#" + toolKey;

    // Find onChange
    $(toolSelector).find(model.tool.dataTriggers.select).change(function () {
        let elem = $(this);

        // Get target and service info
        let targetType = elem.data("target-type");
        let targetSelector = elem.data("target-selector");
        let serviceName = elem.data("service-name");

        // Get selected value
        let selectedValue = elem.val();

        let data = {
            id: selectedValue
        };

        let serviceUrl = createDataServiceUrl(svcUrl, toolKey, serviceName);

        populateDataTable(serviceUrl, targetSelector, data);

        /*
        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'GET',
            data: data,
            error: function (response, status, error) {
                console.log("ERROR: ", response);
                showError(response.responseText);
            },
            success: function (result, textStatus, jqXHR) {
                console.log("SUCCESS: ", result, textStatus, jqXHR);
                showInfo(textStatus);
                if (targetType === "table") {
                    populateDataTable(serviceUrl, targetSelector, result);
                }
            }
        });

         */
    });
};


let populateDataTable = function (serviceUrl, selector, data) {

    console.log("Populating [" + selector + "] with data from " + serviceUrl)

    $(selector).DataTable({
        pageLength: 100,
        ajax: {
            url: serviceUrl,
            dataSrc: "data",
            data: data
        },
        columns: [
            {data: 'order'},
            {data: 'from'},
            {data: 'target.path'},
            {data: 'target.external'},
            {data: 'type'}
        ]
    });

};

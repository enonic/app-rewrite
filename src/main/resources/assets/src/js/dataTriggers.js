// Data-triggers is elements that when they are acted upon (clicked / changed) populates a data-display-element

/* Input:

    service to call -> toolKey, datafetcher, name
    parameters ?
    selector to populate -> typically html-element id


*/


import {model} from "./model";
import {createDataServiceUrl} from "./serviceRegistry";

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


        let dataFunction = function (d) {
            d.id = elem.val();
        };

        let serviceUrl = createDataServiceUrl(svcUrl, toolKey, serviceName);

        populateDataTable(serviceUrl, targetSelector, dataFunction);

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


let populateDataTable = function (serviceUrl, selector, dataFunction) {

    console.log("Populating [" + selector + "] with data from " + serviceUrl);

    if ($.fn.DataTable.isDataTable(selector)) {
        console.log("reload data");
        $(selector).DataTable().ajax.reload();
    } else {
        console.log("initialize data")
        $(selector).DataTable({
            pageLength: 50,
            ajax: {
                url: serviceUrl,
                dataSrc: "data",
                data: dataFunction
            },
            columns: [
                {data: 'order'},
                {data: 'from'},
                {data: 'target.path'},
                {data: 'target.external'},
                {data: 'type'}
            ]
        });

    }


};

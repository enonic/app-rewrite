import {showError} from "./info-bar";
import {initTableActions} from "./dataTableActions";

export let populateDataTable = function (toolKey, svcUrl, serviceUrl, selector, dataFunction) {
    jQuery.ajax({
        url: serviceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response) {
            showError(response.responseText);
        },
        success: function (response) {
            doPopulateData(selector, response, serviceUrl);
            storeTableData(selector, serviceUrl, dataFunction);
            initTableActions(toolKey, svcUrl);
        }
    });
};

export let refreshDataTable = function (selector) {

    console.log("Trying to refresh the selector " + selector);

    if ($.fn.DataTable.isDataTable(selector)) {
        let dataTableElement = $(selector);
        let serviceUrl = dataTableElement.data("serviceUrl");
        let dataFunction = dataTableElement.data("dataFunction");

        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'GET',
            data: dataFunction(),
            error: function (response) {
                showError(response.responseText);
            },
            success: function (response) {
                doRefreshTable(selector, response);
            }
        });
    } else {
        throw "Cannot refresh non-data-table-instance";
    }
};


function doPopulateData(selector, response, serviceUrl) {
    if ($.fn.DataTable.isDataTable(selector)) {
        doRefreshTable(selector, response);
    } else {
        let tableElement = $(selector);
        tableElement.DataTable({
            pageLength: 50,
            data: response.data,
            columns: response.columns
        });
    }
}

function doRefreshTable(selector, response) {
    let dataTable = $(selector).DataTable();
    dataTable.clear();
    dataTable.rows.add(response.data);
    dataTable.draw();
}

function storeTableData(selector, serviceUrl, dataFunction) {
    $(selector).data("serviceUrl", serviceUrl);
    $(selector).data("dataFunction", dataFunction);
}

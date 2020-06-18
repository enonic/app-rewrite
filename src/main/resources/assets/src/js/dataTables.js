import {showError} from "./info-bar";
import {initTableActions} from "./dataTableActions";


export let populateDataTable = function (toolKey, serviceUrl, tableSelector, dataFunction, rowCallback, success) {
    jQuery.ajax({
        url: serviceUrl,
        cache: false,
        type: 'GET',
        data: dataFunction(),
        error: function (response) {
            showError(response.responseText);
        },
        success: function (response) {
            doPopulateData(tableSelector, response, serviceUrl, rowCallback);
            storeTableData(tableSelector, serviceUrl, dataFunction);
            //initTableActions(toolKey, svcUrl);
            success(response);
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


function doPopulateData(selector, response, serviceUrl, rowCallback) {
    if ($.fn.DataTable.isDataTable(selector)) {
        doRefreshTable(selector, response);
    } else {
        let tableElement = $(selector);
        tableElement.DataTable({
            pageLength: 50,
            data: response.data,
            columns: response.columns,
            rowCallback: rowCallback
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


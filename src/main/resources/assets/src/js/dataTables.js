import {showError} from "./info-bar";


export let populateDataTable = function (serviceConfig, onTablePopulated) {

    jQuery.ajax({
        url: serviceConfig.dataServiceUrl,
        cache: false,
        type: 'GET',
        data: serviceConfig.dataFunction(),
        error: function (response) {
            showError(response.responseText);
        },
        success: function (response) {
            doPopulateData(serviceConfig.tableSelector, response, serviceConfig.tableConfig);
            storeTableData(serviceConfig.tableSelector, serviceConfig.dataServiceUrl, serviceConfig.dataFunction);
            onTablePopulated();
        }
    });
};

let doPopulateData = function (selector, response, tableConfig) {
    if ($.fn.DataTable.isDataTable(selector)) {
        doRefreshTable(selector, response);
    } else {
        let tableElement = $(selector);

        tableConfig.data = response.data;
        tableConfig.columns = response.columns;
        tableElement.DataTable(
            tableConfig
        );
    }
};

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


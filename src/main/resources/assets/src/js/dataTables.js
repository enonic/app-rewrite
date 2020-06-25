import {showError} from "./info-bar";

export let serializeDataTable = function (tableSelector) {
    if ($.fn.DataTable.isDataTable(tableSelector)) {
        return $(tableSelector).dataTable().$('input, select').serialize();
    } else {
        throw "Not a table"
    }
};


export let populateDataTable = function (serviceConfig, onTablePopulated, onTableRefresh) {

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
            storeTableData(serviceConfig.tableSelector, serviceConfig.dataServiceUrl, serviceConfig.dataFunction, onTableRefresh);
            onTableRefresh();
            onTablePopulated();
        }
    });
};

let doPopulateData = function (selector, response, tableConfig) {
    if ($.fn.DataTable.isDataTable(selector)) {
        doRefreshTable($(selector).DataTable(), response);
    } else {
        let tableElement = $(selector);
        tableConfig.data = response.data;
        tableConfig.columns = response.columns;
        tableElement.DataTable(
            tableConfig
        );
    }
};

export let refreshDataTable = function (refreshSelectors) {

    $(refreshSelectors).each(function () {

        let element = $(this);

        if ($.fn.DataTable.isDataTable(element)) {
            let dataTable = $(this).DataTable();
            let serviceUrl = element.data("serviceUrl");
            let dataFunction = element.data("dataFunction");
            let onTableRefresh = element.data("onTableRefresh");

            jQuery.ajax({
                url: serviceUrl,
                cache: false,
                type: 'GET',
                data: dataFunction(),
                error: function (response) {
                    showError(response.responseText);
                },
                success: function (response) {
                    doRefreshTable(dataTable, response);
                    onTableRefresh();
                }
            });
        } else {
            // ignore, not a DataTable
        }
    });


};

function doRefreshTable(dataTable, response) {
    dataTable.clear();
    dataTable.rows.add(response.data);
    dataTable.draw();
}

function storeTableData(selector, serviceUrl, dataFunction, onTableRefresh) {
    $(selector).data("serviceUrl", serviceUrl);
    $(selector).data("dataFunction", dataFunction);
    $(selector).data("onTableRefresh", onTableRefresh)
}


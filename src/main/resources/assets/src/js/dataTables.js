import {populateDataElement, refreshDataElement} from "./dataElements";

export let populateDataTable = function (serviceConfig, onTablePopulated, onTableRefresh) {

    function doRefreshTable(tableElement, response) {
        let dataTable = tableElement.DataTable();
        dataTable.clear();
        dataTable.rows.add(response.data);
        dataTable.draw();
        onTableRefresh(response);
    }

    let doPopulateData = function (response) {
        if ($.fn.DataTable.isDataTable(tableSelector)) {
            doRefreshTable($(tableSelector).DataTable(), response);
        } else {
            let tableElement = $(tableSelector);
            tableConfig.data = response.data;
            tableConfig.columns = response.columns;
            tableElement.DataTable(
                tableConfig
            );
        }
        onTablePopulated(response);
        onTableRefresh(response);
    };

    let tableConfig = serviceConfig.tableConfig;
    let tableSelector = serviceConfig.tableSelector;
    let dataServiceUrl = serviceConfig.dataServiceUrl;
    let dataFunction = serviceConfig.dataFunction;

    populateDataElement(tableSelector, dataServiceUrl, dataFunction, doPopulateData, doRefreshTable)
};

export let refreshDataTable = function (refreshSelectors) {
    refreshDataElement(refreshSelectors);
};



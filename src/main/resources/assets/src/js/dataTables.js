import {populateDataElement, refreshDataElement} from "./dataElements";

export let populateDataTable = function (serviceConfig, onTablePopulated, onTableRefresh, onRedraw) {

    function doRefreshTable(selector, response) {
        let dataTable = $(selector).DataTable();
        dataTable.clear();
        dataTable.rows.add(response.data);
        dataTable.draw();
        onTableRefresh(response);
    }

    let doPopulateData = function (response) {
        if ($.fn.DataTable.isDataTable(tableSelector)) {
            refreshDataElement(tableSelector, response);
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

    addOnRedrawEventHandler(tableSelector, onRedraw);

    populateDataElement(tableSelector, dataServiceUrl, dataFunction, doPopulateData, doRefreshTable)
};

let addOnRedrawEventHandler = function (tableSelector, onFiltering) {
    $(tableSelector)
        .off('draw.dt')
        .on('draw.dt', function () {
            onFiltering();
        });
};



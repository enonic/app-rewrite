import {enableHelp, loadTool} from "./tools";
import {createDataServiceUrl, createToolRendererUrl} from "./serviceRegistry";
import {selectTool} from "./toolbar";
import {setRuleContext} from "./toolRules";
import {populateDataTable} from "./dataTables";
import {enableActionButtons} from "./tableActions";
import {model} from "./model";

const toolKey = "tool-context";
const toolSelector = "#" + toolKey;
const virtualHostDataTableSelector = "#toolContextContextTable";

let toolConfig = {};

let rowCallbackFunction = function (row, data) {
    if (data.provider && data.provider !== "") {
        $(row).addClass('has-provider');
    }
};

const tableSettings = {
    pageLength: 25,
    autoWidth: false,
    rowCallback: rowCallbackFunction
};

export let initToolContext = function (svcUrl) {
    toolConfig.svcUrl = svcUrl;

    let dataFunction = function () {
    };

    loadTool(createToolRendererUrl(toolConfig.svcUrl, toolKey), dataFunction, onToolLoaded);
};

let onToolLoaded = function (result) {

    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);

    let df = function () {
    };

    let serviceConfig = {
        svcUrl: toolConfig.svcUrl,
        dataServiceUrl: createDataServiceUrl(toolConfig.svcUrl, toolKey, "context"),
        dataFunction: df,
        tableConfig: tableSettings,
        tableSelector: virtualHostDataTableSelector
    };

    populateDataTable(serviceConfig, onDataPopulated, onTableRefresh, onRedraw);
    enableHelp(toolSelector);
};

let onRedraw = function () {
    enableActionButtons(toolConfig.svcUrl, toolSelector, toolKey);
};

let onDataPopulated = function (response) {
    //
};

let onTableRefresh = function (response) {
    makeRowsClickable();
    enableActionButtons(toolConfig.svcUrl, toolSelector, toolKey);
};

let makeRowsClickable = function () {
    let table = $(virtualHostDataTableSelector).DataTable();
    $(virtualHostDataTableSelector + ' tbody')
        .off('click')
        .on('click', 'tr.has-provider', function () {
            let data = table.row(this).data();
            setRuleContext(data['contextKey']);
            selectTool(model.toolbar.tools.rules);
        });
};

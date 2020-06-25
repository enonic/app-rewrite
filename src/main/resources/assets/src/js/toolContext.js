import {loadTool} from "./tools";
import {createActionServiceUrl, createDataServiceUrl, createToolRendererUrl} from "./serviceRegistry";
import {selectTool} from "./toolbar";
import {setRuleContext} from "./toolRules";
import {populateDataTable, refreshDataTable} from "./dataTables";
import {showError, showInfo} from "./info-bar";
import {enableActionButtons} from "./tableActions";

const toolKey = "tool-context";
const toolSelector = "#" + toolKey;
const virtualHostDataTableSelector = "#toolContextContextTable";

let toolConfig = {};


export let initToolContext = function (svcUrl) {
    toolConfig.svcUrl = svcUrl;

    let dataFunction = function () {
    };

    loadTool(createToolRendererUrl(toolConfig.svcUrl, toolKey), dataFunction, onToolLoaded);
};

let onToolLoaded = function (result) {

    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);

    let dataFunction = function () {
    };

    let serviceConfig = {
        svcUrl: toolConfig.svcUrl,
        dataServiceUrl: createDataServiceUrl(toolConfig.svcUrl, toolKey, "context"),
        dataFunction: dataFunction,
        tableConfig: createTableConfig(),
        tableSelector: virtualHostDataTableSelector
    };

    populateDataTable(serviceConfig, onDataPopulated)
};


let onDataPopulated = function () {
    console.log("Data populated for " + virtualHostDataTableSelector);
    makeRowsClickable();
    enableActionButtons(toolConfig.svcUrl, toolSelector, toolKey, virtualHostDataTableSelector);
};

let makeRowsClickable = function () {
    let table = $(virtualHostDataTableSelector).DataTable();
    $(virtualHostDataTableSelector + ' tbody').on('click', 'tr.has-provider', function () {
        let data = table.row(this).data();
        setRuleContext(data['contextKey']);
        selectTool($("#nav-tool-rules"));
    });
};

let rowCallbackFunction = function (row, data) {
    if (data.provider && data.provider !== "") {
        $(row).addClass('has-provider');
    }
};

let createTableConfig = function () {
    return {
        pageLength: 10,
        rowCallback: rowCallbackFunction
    };
};

import {createDataServiceUrl, createModalSelector, createModalUrl, createToolRendererUrl} from "./serviceRegistry";
import {loadTool} from "./tools";
import {populateDataTable} from "./dataTables";
import {displayModal} from "./modals";
import {enableActionButtons} from "./tableActions";
import {fetch} from "./dataService";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const contextSelectorSelector = "#toolRulesContextSelect";

const createRuleButton = "#btnCreateRule";

let svcUrl;

export let initToolRules = function (svc) {
    svcUrl = svc;
    let data = function () {
    };
    loadTool(createToolRendererUrl(svcUrl, toolKey), data, onToolLoaded);
};

export let setRuleContext = function (contextKey) {
    let contextSelector = $(contextSelectorSelector);
    contextSelector.val(contextKey).change();
};

function triggerContextChanged() {
    $(contextSelectorSelector).change(function () {
        let context = $(contextSelectorSelector).val();
        loadRules(context);
        setRuleButtonState(context);
    });
}

function setRuleButtonState(contextKey) {

    function doSetButtonState(response) {

        if (response.readOnly) {
            $(createRuleButton).prop('disabled', true);
        } else {
            $(createRuleButton).prop('disabled', false);
        }
    }

    let providerInfoUrl = createDataServiceUrl(svcUrl, toolKey, "provider-info");
    let dataFunction = function () {
        return {
            contextKey: contextKey
        }
    };
    fetch(providerInfoUrl, dataFunction, doSetButtonState);
}

function setContextSelectorData() {

    let doPopulateSelectorValues = function (response) {
        $(contextSelectorSelector).html(response);
    };

    let serviceUrl = createDataServiceUrl(svcUrl, toolKey, "context-selector");
    let dataFunction = function () {
    };

    fetch(serviceUrl, dataFunction, doPopulateSelectorValues);
}


let onToolLoaded = function (result) {
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);
    triggerContextChanged();
    setContextSelectorData();
};

let createTableConfig = function () {
    return {
        pageLength: 10
    };
};

let contextSelectorDataContext = function () {
    return {
        contextKey: $(contextSelectorSelector).val()
    }
};

let loadRules = function () {

    let serviceConfig = {
        dataServiceUrl: createDataServiceUrl(svcUrl, toolKey, "rules"),
        dataFunction: contextSelectorDataContext,
        tableConfig: createTableConfig(),
        tableSelector: ruleTableSelector
    };

    populateDataTable(serviceConfig, onDataLoaded, onTableRefreshed);
};

let onDataLoaded = function (response) {
    console.log("Rule-data loaded successfully");
    enableRuleButtons();
};

let onTableRefreshed = function (response) {
    enableActionButtons(svcUrl, toolSelector, toolKey);
};

let enableRuleButtons = function () {
    $(createRuleButton).click(function () {
        let modalSelector = createModalSelector(toolKey, "create");
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "create");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext)
    });
};


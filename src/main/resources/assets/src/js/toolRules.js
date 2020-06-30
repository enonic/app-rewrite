import {createDataServiceUrl, createModalSelector, createModalUrl, createToolRendererUrl} from "./serviceRegistry";
import {loadTool} from "./tools";
import {populateDataTable} from "./dataTables";
import {displayModal} from "./modals";
import {enableActionButtons} from "./tableActions";
import {fetch} from "./dataService";
import {populateDataElement} from "./dataElements";
import {model} from "./model";

const toolKey = "tool-rules";
const toolSelector = "#" + toolKey;
const ruleTableSelector = "#toolRulesRulesTable";
const contextSelectorSelector = "#toolRulesContextSelect";

let svcUrl;

const rulesTableSettings = {
    pageLength: 50,
    autoWidth: false,
    dom: "<'row'<'col-sm-12 col-md-6'l><'col-sm-12 col-md-6'f>>" +
         "<'row'<'col-sm-12'tr>>" +
         "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>"
};


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
            $(model.buttons.rule.create).prop('disabled', true);
        } else {
            $(model.buttons.rule.create).prop('disabled', false);
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
    let doRefreshContextSelector = function (selector, response) {
        let html = "<option disabled selected value> -- select a vhost --</option>";
        html += response;
        $(selector).html(html);
    };

    let doPopulateSelectorValues = function (response) {
        let html = "<option disabled selected value> -- select a vhost --</option>";
        html += response;
        $(contextSelectorSelector).html(html);
    };

    let serviceUrl = createDataServiceUrl(svcUrl, toolKey, "context-selector");
    let dataFunction = function () {
    };

    populateDataElement(contextSelectorSelector, serviceUrl, dataFunction, doPopulateSelectorValues, doRefreshContextSelector);
}

let onToolLoaded = function (result) {
    console.log("Tool [" + toolKey + "] loaded");
    $(toolSelector).html(result);
    triggerContextChanged();
    setContextSelectorData();
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
        tableConfig: rulesTableSettings,
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
    $(model.buttons.rule.create).click(function () {
        let modalSelector = createModalSelector(toolKey, "create");
        let modalServiceUrl = createModalUrl(svcUrl, toolKey, "create");
        displayModal(modalServiceUrl, svcUrl, modalSelector, contextSelectorDataContext)
    });
};


import {model} from "./model";
import {createDataServiceUrl} from "./serviceRegistry";
import {populateDataTable} from "./dataTables";

export let initDataTriggers = function (toolKey, svcUrl) {

    let toolSelector = "#" + toolKey;

    $(toolSelector).find(model.tool.dataTriggers.select).change(function () {
        let elem = $(this);
        let targetSelector = elem.data("target-selector");
        let serviceName = elem.data("service-name");
        let serviceUrl = createDataServiceUrl(svcUrl, toolKey, serviceName);

        let dataFunction = function () {
            return {
                id: elem.val()
            }
        };

        populateDataTable(toolKey, svcUrl, serviceUrl, targetSelector, dataFunction);
    });

    $(toolSelector).find(model.tool.dataTriggers.auto).each(function () {
        let elem = $(this);
        let targetSelector = elem.data("target-selector");
        let serviceName = elem.data("service-name");
        let serviceUrl = createDataServiceUrl(svcUrl, toolKey, serviceName);

        let dataFunction = function () {
            return {}
        };

        populateDataTable(toolKey, svcUrl, serviceUrl, targetSelector, dataFunction);
    });

};

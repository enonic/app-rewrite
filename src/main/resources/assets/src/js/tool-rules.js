import {model} from "./model";
import {createDataServiceUrl} from "./serviceRegistry";
import {showError, showInfo} from "./info-bar";

export let initToolRules = function () {


};


let initContextSelector = function (svcUrl) {

    console.log("Init selects");

    let toolSelector = "#" + toolKey;
    $(toolSelector).find(model.tool.select.selector).change(function () {
        let elem = $(this);
        let selectType = elem.data("select-type");
        let selectedValue = elem.val();
        console.log("Selected: " + selectType, selectedValue);

        let data = {
            id: selectedValue
        };

        let serviceUrl = createDataServiceUrl(svcUrl, toolKey, selectType);

        jQuery.ajax({
            url: serviceUrl,
            cache: false,
            type: 'GET',
            data: data,
            error: function (response, status, error) {
                console.log("ERROR: ", response);
                showError(response.responseText);
            },
            success: function (response, textStatus, jqXHR) {
                console.log("SUCCESS: ", response, textStatus, jqXHR);
                showInfo(response.message);
            }
        });
    });
};
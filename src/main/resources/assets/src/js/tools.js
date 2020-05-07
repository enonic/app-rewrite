import {model} from "./model";


export let initToolActions = function () {

    $(model.buttons.tool.action).each(function () {

        $(this).click(function () {
            let context = $(this).data("context");
            let action = $(this).data("action");
            console.log("invoking action: " + action + " on context: " + context);
        })


    });


};
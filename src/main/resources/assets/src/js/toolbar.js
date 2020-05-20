import {model} from "./model";


export let initToolbar = function () {

    let allNavs = $(model.toolbar.toolNav);

    allNavs.each(function () {
        $(this).click(function () {
            allNavs.removeClass("selected");
            $(this).addClass("selected");
            let activates = $(this).data("activator");
            toggleTool(activates);
        })
    })


};


let toggleTool = function (id) {

    console.log("Toggle tool: " + id);
    $(model.components.tool).each(function () {
        if ($(this).attr('id') === id) {
            $(this).addClass("selected");
        } else {
            $(this).removeClass("selected");
        }
    });

};

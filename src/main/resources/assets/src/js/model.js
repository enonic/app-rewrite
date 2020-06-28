import {selectTool} from "./toolbar";

export const model = {

    input: {
        requestURL: "#requestURL"
    },
    elements: {
        vhosts: ".vhost",
        result: "#result",
        rows: "tr",
        errorMsg: "#errorMsg"
    },
    components: {
        tool: ".tool"
    },
    toolbar: {
        toolNav: ".tool-nav",
        tools: {
            rules: "#nav-tool-rules",
            virtualHosts: "#nav-tool-context"
        }
    },
    buttons: {
        tool: {
            action: "button.action"
        },
        rule: {
            create: "#btnCreateRule"
        }
    },
    modals: {
        all: ".modal",
        overlay: "#modal-overlay",
        triggers: ".modal-trigger",
        modalCancel: ".modal-cancel",
        modalAction: ".modal-action",
        contextCreate: {
            selector: "#modalContextCreate",
        },
        rule: {
            create: "#tool-rules-modal-create-rule"
        }
    },
    selectors: {
        contextSelectorSelector: "#toolRulesContextSelect"
    },
    infoBar: {
        selector: "#bottom-bar"
    },

};
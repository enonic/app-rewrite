import {selectTool} from "./toolbar";

export const model = {

    input: {
        rewriteContext: "#rewriteContextSelection",
        requestURL: "#requestURL"
    },
    elements: {
        vhosts: ".vhost",
        requestTesterResult: "#request-tester-result",
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
            import: "#btnImportRules",
            export: "#btnExportRules",
            create: "#btnCreateRule"
        }
    },
    modals: {
        all: ".modal",
        overlay: "#modal-overlay",
        triggers: ".modal-trigger",
        modalCancel: ".modal-cancel",
        modalAction: ".modal-action",
        insertStrategy: "#insertStrategy",
        insertPosition: "insertPosition",
        contextCreate: {
            selector: "#modalContextCreate",
        },
        rules: "#tool-rules-modal",
        forms: {
            importRules: "#tool-rules-action-import-form"
        }
    },
    selectors: {
        contextSelectorSelector: "#toolRulesContextSelect"
    },
    infoBar: {
        selector: "#bottom-bar"
    },

};

import {showError, showInfo} from "./info-bar";

export const listenToLicenseUpdates = () => {
    const uploadInput = document.getElementById("upload-license");

    uploadInput.addEventListener("change", () => {
        const formData = new FormData();
        formData.append("license", uploadInput.files[0]);

        fetch(CONFIG.licenseUrl, {
            body: formData,
            "Content-type": "multipart/form-data",
            method: "POST",
        })
        .then(response => response.json())
        .then(data => {
            if (data.licenseValid) {
                showInfo('Licence file is successfully uploaded', 2000, () => location.reload());
            } else {
                showError('Failed to upload or process the license file');
            }
        });
    });
};

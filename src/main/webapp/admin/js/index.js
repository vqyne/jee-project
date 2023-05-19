/**========================================================
 **                 openExplorer
 *? Opens the file explorer to select a CSV file.
 * @return {void}
 **=========================================================**/
function openExplorer() {
    // Open the file explorer here
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = '.csv';
    input.addEventListener('change', fileSelection);
    input.click();
}

/**========================================================
 **                 fileSelection
 *? Handles the file selection from the file explorer.
 * @param {Event} event - The file selection event.
 * @return {void}
 **=========================================================**/
function fileSelection(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        const content = e.target.result;
        sendCSV(content);
    };

    reader.readAsText(file);
}

/**========================================================
 **                 sendCSV
 *? Sends the CSV file to the server for importing athletes.
 * @param {string} file - The content of the CSV file.
 * @return {void}
 **=========================================================**/
function sendCSV(file) {
    const formData = new FormData();
    formData.append('file', file);

    fetch('/jee-project/api/athlete-controller/import-athletes-from-csv', {
        method: 'POST',
        headers: {
            'Content-Type': 'text/csv' // Specify the correct content type here
        },
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log('The file has been imported successfully.');
                alert("The athletes from the file have been imported successfully!");
            } else {
                console.error('An error occurred while importing the file.');
            }
        })
        .catch(error => {
            console.error('An error occurred:', error);
        });
}

// Event listeners
const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

const drop_div = document.getElementById('drag-div');

drop_div.addEventListener('dragover', function (e) {
    e.preventDefault();
    drop_div.style.backgroundColor = 'rgba(0, 0, 255, 0.2)';
});

drop_div.addEventListener('dragleave', function () {
    drop_div.style.backgroundColor = 'transparent';
});

drop_div.addEventListener('drop', function (e) {
    e.preventDefault();
    drop_div.style.backgroundColor = 'transparent';

    const file = e.dataTransfer.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        const content = e.target.result;
        sendCSV(content);
    };

    reader.readAsText(file);
});

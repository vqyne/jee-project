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
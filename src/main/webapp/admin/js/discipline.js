/**========================================================
 **                 loadDiscipline
 *? Loads the discipline data from the provided URL.
 * @param {string} url - The URL to fetch the discipline data from.
 * @return {void}
 **=========================================================**/
function loadDiscipline(url) {
    // Show the loader
    document.getElementById("loader").style.display = "block";
    fetch(url)
        .then(response => response.json())
        .then(response => process(response))
        .catch(error => console.log("Erreur : " + error));
}

/**========================================================
 **                 process
 *? Processes the discipline data and populates the table.
 * @param {Array} discipline - The discipline data to process.
 * @return {void}
 **=========================================================**/
function process(discipline) {
    var table = document.getElementById("tbody-discipline");

    while (table.childElementCount > 0) {
        table.removeChild(table.lastChild);
    }

    for (var a of discipline) {
        var tr = document.createElement("tr");
        tr.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700');

        var td_name = document.createElement("th");
        var link = document.createElement("a");
        link.href = "/jee-project/admin/discipline/page_discipline.html?d=" + a.name;
        link.textContent = a.name;
        td_name.appendChild(link);

        td_name.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
        td_name.setAttribute("scope", "col");

        var icon = document.createElement("img");
        icon.setAttribute("alt", "");
        icon.setAttribute("width", "24");
        icon.setAttribute("height", "24");
        if (a.flag) {
            icon.setAttribute("src", "/jee-project/img/check.svg");
        } else {
            icon.setAttribute("src", "/jee-project/img/x.svg");
        }

        var td_flag = document.createElement("td");
        td_flag.appendChild(icon);

        td_flag.classList.add('px-6', 'py-4');
        tr.appendChild(td_name);
        tr.appendChild(td_flag);
        table.appendChild(tr);
    }
    // Show the loader
    document.getElementById("loader").style.display = "none";
}

const form = document.getElementById('add-discipline-form');
const nameInput = document.getElementById('discipline-name');
const accessibleInput = document.getElementById('accessible-paralympiques');
const submitBtn = document.getElementById('submit-btn');

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

submitBtn.addEventListener('click', function (event) {
    event.preventDefault();

    const name = nameInput.value;
    const accessible = accessibleInput.checked;

    var accessibleBit = 0;
    if (accessible) {
        accessibleBit = 1;
    }

    const url = '/jee-project/api/discipline-controller/discipline-add/';

    const formData = new URLSearchParams();
    formData.append('name', name);
    formData.append('accessible', accessibleBit.toString());

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                console.log(response);
                throw new Error('Error network');
            }
            return response.json();
        })
        .then(data => {
            console.log('Discipline ajoutée avec succès:', data);
            form.reset();
            loadDiscipline('/jee-project/api/discipline-controller/get-disciplines')
        })
        .catch(error => {
            console.error('Erreur lors de l\'ajout de la discipline:', error);
        });
});
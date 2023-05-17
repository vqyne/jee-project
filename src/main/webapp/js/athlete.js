/**========================================================
 **                 loadAthletes
 * ? Loads athletes from the specified URL.
 * @param {string} url - The URL to fetch the athletes from.
 * @return {void}
 **=========================================================**/
function loadAthletes(url) {
    // Show the loader
    document.getElementById("loader").style.display = "block";
    fetch(url)
        .then(response => response.json())
        .then(response => process(response))
        .catch(error => console.log("Erreur : " + error));
}

/**========================================================
 **                 process
 * ? Processes the athletes data.
 * @param {Array} athletes - The athletes data to process.
 * @return {void}
 **=========================================================**/
function process(athletes) {
    var athletelist = document.getElementById("athletelist");

    if (athletelist.hasChildNodes()) {
        athletelist.removeChild(athletelist.firstChild);
    }

    var table = document.getElementById("tbody-athlete");

    while (table.childElementCount > 0) {
        table.removeChild(table.lastChild);
    }

    for (var a of athletes) {
        var tr = document.createElement("tr");
        tr.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700');
        var td_id = document.createElement("th");
        td_id.append(a.id);
        td_id.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_id.setAttribute("scope", "col");
        var td_lastname = document.createElement("td");
        td_lastname.append(a.lastname);
        td_lastname.classList.add('px-6', 'py-4')
        var td_firstname = document.createElement("td");
        td_firstname.append(a.firstname);
        td_firstname.classList.add('px-6', 'py-4')
        var td_country = document.createElement("td");
        td_country.append(a.country);
        td_country.classList.add('px-6', 'py-4')
        var td_birthdate = document.createElement("td");
        td_birthdate.append(a.birthdate);
        td_birthdate.classList.add('px-6', 'py-4')
        var td_genre = document.createElement("td");
        td_genre.append(a.genre);
        td_genre.classList.add('px-6', 'py-4')
        var td_discipline = document.createElement("td");
        td_discipline.append(a.discipline.name);
        td_discipline.classList.add('px-6', 'py-4')
        tr.appendChild(td_id);
        tr.appendChild(td_firstname);
        tr.appendChild(td_lastname);
        tr.appendChild(td_country);
        tr.appendChild(td_birthdate);
        tr.appendChild(td_genre);
        tr.appendChild(td_discipline);
        table.appendChild(tr);
    }
    // Hide the loader
    document.getElementById("loader").style.display = "none";
}

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

const searchInput = document.getElementById("searchInput");

searchInput.addEventListener("keyup", (event) => {
    if (/[a-zA-Z]/.test(event.key)) {
        executeSearch();
    }
});

/**========================================================
 **                 executeSearch
 * ? Executes a search based on the provided search term.
 * @return {void}
 **=========================================================**/
function executeSearch() {
    const searchTerm = searchInput.value;

    if (searchTerm.length > 0) {
        loadAthletes('/jee-project/api/athlete-controller/get-athletes-name?name=' + searchTerm);
    } else {
        loadAthletes('/jee-project/api/athlete-controller/get-athletes');
    }
}
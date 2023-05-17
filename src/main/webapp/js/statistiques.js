/**=========================================================
 *                         load
 * ? Loads data from the specified URLs and processes them.
 * @param {string} urlSites
 * @param {string} urlDisciplines
 * @return {void}
 *=========================================================**/
function load(urlSites, urlDisciplines) {
    fetch(urlSites)
        .then(response => response.json())
        .then(response => processSites(response))
        .catch(error => console.log("Erreur : " + error));

    fetch(urlDisciplines)
        .then(response => response.json())
        .then(response => processDisciplines(response))
        .catch(error => console.log("Erreur : " + error));
}

/**=========================================================
 *                   convertMinutesToShow
 * ? Converts minutes to a formatted string representation 
 * ? of days, hours, and minutes.
 * @param {number} minutes
 * @return {string}
*=========================================================**/
function convertMinutesToShow(minutes) {
    var jours = Math.floor(minutes / 1440);
    var heures = Math.floor((minutes % 1440) / 60);
    var minutes = minutes % 60;
    return jours + " jour(s), " + heures + " heure(s), " + minutes + " minute(s)";
}

/**=========================================================
 *                   processSites
 * ? Processes the sites data and updates the table.
 * @param {Array} sites
 * @return {void}
*=========================================================**/
function processSites(sites) {

    var table = document.getElementById("tbody-site");

    while (table.childElementCount > 0) {
        table.removeChild(table.lastChild);
    }

    for (var s of sites) {
        var tr = document.createElement("tr");
        tr.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700');

        var td_id = document.createElement("th");
        td_id.append(s.id)
        td_id.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_id.setAttribute("scope", "col");

        var td_name = document.createElement("th");
        td_name.append(s.name)
        td_name.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_name.setAttribute("scope", "col");

        var td_city = document.createElement("th");
        td_city.append(s.city)
        td_city.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_city.setAttribute("scope", "col");

        var td_categorie = document.createElement("th");
        td_categorie.append(s.category)
        td_categorie.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_categorie.setAttribute("scope", "col");

        var td_number = document.createElement("th");
        td_number.append(s.numberUsed)
        td_number.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_number.setAttribute("scope", "col");

        tr.appendChild(td_id);
        tr.appendChild(td_name);
        tr.appendChild(td_city);
        tr.appendChild(td_categorie);
        tr.appendChild(td_number);
        table.appendChild(tr);
    }
}

/**=========================================================
 *                   processSites
 * ? Processes the disciplines data and updates the table.
 * @param {Array} discipline
 * @return {void}
*=========================================================**/
function processDisciplines(discipline) {
    // Show the loader
    document.getElementById("loader").style.display = "block";
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
        td_name.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
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

        var td_time = document.createElement("th");
        td_time.append(convertMinutesToShow(a.allTime))
        td_time.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_time.setAttribute("scope", "col");

        td_flag.classList.add('px-6', 'py-4')
        tr.appendChild(td_name);
        tr.appendChild(td_flag);
        tr.appendChild(td_time);

        table.appendChild(tr);
    }
    // Hide the loader
    document.getElementById("loader").style.display = "none";
}

/**------------------------------------------------------------------------
 *                           CONST & LOAD
 *------------------------------------------------------------------------**/
load('/jee-project/api/site-controller/get-top-five-sites', '/jee-project/api/discipline-controller/get-disciplines-duration')

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});
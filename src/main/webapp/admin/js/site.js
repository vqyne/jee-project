/**========================================================
 **                 loadSite
 *? Loads the site data from the provided URL.
 * @param {string} url - The URL to fetch the site data from.
 * @return {void}
 **=========================================================**/
 function loadSite(url) {
    // Show the loader
    document.getElementById("loader").style.display = "block";
    fetch(url)
        .then(response => response.json())
        .then(response => process(response))
        .catch(error => console.log("Erreur : " + error));
}

/**========================================================
 **                 process
 *? Processes the site data and populates the table.
 * @param {Array} sites - The site data to process.
 * @return {void}
 **=========================================================**/
function process(sites) {
    var table = document.getElementById("tbody-site");

    while (table.childElementCount > 0) {
        table.removeChild(table.lastChild);
    }

    for (var s of sites) {
        var tr = document.createElement("tr");
        tr.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700');

        var td_id = document.createElement("th");
        td_id.append(s.id);
        td_id.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
        td_id.setAttribute("scope", "col");

        var td_name = document.createElement("th");
        td_name.append(s.name);
        td_name.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
        td_name.setAttribute("scope", "col");

        var td_city = document.createElement("th");
        td_city.append(s.city);
        td_city.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
        td_city.setAttribute("scope", "col");

        var td_categorie = document.createElement("th");
        td_categorie.append(s.category);
        td_categorie.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
        td_categorie.setAttribute("scope", "col");

        tr.appendChild(td_id);
        tr.appendChild(td_name);
        tr.appendChild(td_city);
        tr.appendChild(td_categorie);
        table.appendChild(tr);
    }
    // Show the loader
    document.getElementById("loader").style.display = "none";
}

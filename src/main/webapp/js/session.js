/**==============================================
 **              loadSessions
 *?  Loads sessions from a specified URL.
 *@param {string} url - The URL to fetch the sessions from.
 *@return {Promise} - A promise that resolves with the fetched sessions.
 *=============================================**/
function loadSessions(url) {
    // Show the loader
    document.getElementById("loader").style.display = "block";

    fetch(url)
        .then(response => response.json())
        .then(response => {
            process(response)

        })
        .catch(error => {
            alert("Erreur : " + error)

        });
}

/**==============================================
 **              process
 *?  Processes the sessions data.
 *@param {Array} sessions - The sessions data to process.
 *@return {void}
 *=============================================**/
function process(sessions) {

    var table = document.getElementById("tbody-session");

    while (table.childElementCount > 0) {
        table.removeChild(table.lastChild);
    }

    for (var a of sessions) {
        var tr = document.createElement("tr");
        tr.classList.add('bg-white', 'border-b', 'dark:bg-gray-800', 'dark:border-gray-700');
        var td_code = document.createElement("th");
        td_code.append(a.code);
        td_code.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_code.setAttribute("scope", "col");
        var td_date = document.createElement("td");
        td_date.append(a.date);
        td_date.classList.add('px-6', 'py-4')
        var td_fromHour = document.createElement("td");
        td_fromHour.append(a.fromHour);
        td_fromHour.classList.add('px-6', 'py-4')
        var td_toHour = document.createElement("td");
        td_toHour.append(a.toHour);
        td_toHour.classList.add('px-6', 'py-4')
        var td_discipline = document.createElement("td");
        td_discipline.append(a.discipline.name);
        td_discipline.classList.add('px-6', 'py-4')
        var td_site = document.createElement("td");
        td_site.append(a.site.name);
        td_site.classList.add('px-6', 'py-4')
        var td_description = document.createElement("td");
        td_description.append(a.description);
        td_description.classList.add('px-6', 'py-4')
        var td_type = document.createElement("td");
        td_type.append(a.type);
        td_type.classList.add('px-6', 'py-4')
        var td_category = document.createElement("td");
        td_category.append(a.category);
        td_category.classList.add('px-6', 'py-4')
        tr.appendChild(td_code);
        tr.appendChild(td_date);
        tr.appendChild(td_fromHour);
        tr.appendChild(td_toHour);
        tr.appendChild(td_discipline);
        tr.appendChild(td_site);
        tr.appendChild(td_description);
        tr.appendChild(td_type);
        tr.appendChild(td_category);
        table.appendChild(tr);
    }
    // Hide the loader
    document.getElementById("loader").style.display = "none";
}

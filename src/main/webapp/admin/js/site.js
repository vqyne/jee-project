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

        var td_modify = document.createElement("th");

        var editIcon = document.createElement("img");
        editIcon.setAttribute("alt", "");
        editIcon.setAttribute("width", "24");
        editIcon.setAttribute("height", "24");
        editIcon.setAttribute("src", "/jee-project/img/edit.svg");
        td_modify.classList.add("edit-class")
        td_modify.setAttribute("id", "edit-icon-" + s.id)

        td_modify.append(editIcon)
        td_modify.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
        td_modify.setAttribute("scope", "col");

        td_modify.addEventListener('click', function () {
            const currentURL = window.location.href;
            const projectUrl = currentURL.split("/jee-project")[0];
            const newURL = projectUrl + "/jee-project/admin/site/modify.html?id=" + this.getAttribute('id').split('-')[2];
            location.replace(newURL);
        })

        if (!(s.hasSessions)) {
            var td_delete = document.createElement("th");

            var deleteIcon = document.createElement("img");
            deleteIcon.setAttribute("alt", "");
            deleteIcon.setAttribute("width", "24");
            deleteIcon.setAttribute("height", "24");
            deleteIcon.setAttribute("src", "/jee-project/img/trash.svg");
            td_delete.classList.add("delete-class")
            td_delete.setAttribute("id", "delete-icon-" + s.id)

            td_delete.append(deleteIcon)
            td_delete.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
            td_delete.setAttribute("scope", "col");

            td_delete.addEventListener('click', function () {
                const url = '/jee-project/api/site-controller/delete-site/' + this.getAttribute('id').split('-')[2];;

                fetch(url, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            console.log(response);
                            throw new Error('Erreur réseau');
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('Site supprimé avec succès:', data);
                        loadSite('/jee-project/api/site-controller/get-sites')
                    })
                    .catch(error => {
                        console.error('Erreur lors de la suppression du site:', error);
                    });

            })
        } else {
            var td_delete = document.createElement("th");

            td_delete.classList.add("delete-class");
            td_delete.setAttribute("id", "delete-icon-" + s.id);
            td_delete.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white');
            td_delete.setAttribute("scope", "col");
        }

        tr.appendChild(td_id);
        tr.appendChild(td_name);
        tr.appendChild(td_city);
        tr.appendChild(td_categorie);
        tr.appendChild(td_modify);
        tr.appendChild(td_delete);

        table.appendChild(tr);
    }
    // Show the loader
    document.getElementById("loader").style.display = "none";
}

const form = document.getElementById('add-site-form');
const nameInput = document.getElementById('site-name');
const cityInput = document.getElementById('site-city');
const categoryInput = document.getElementById('site-category');

const submitBtn = document.getElementById('submit-btn');

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

const selectElement = document.getElementById('site-category');

/**========================================================
 **                 generateCategoryOptions
 *? Processes the site data and populates the table.
 * @param {Array} categories - The site data to process.
 * @return {void}
 **=========================================================**/
function generateCategoryOptions(categories) {
    categories.forEach((categorie) => {
        const optionElement = document.createElement('option');
        optionElement.value = categorie;
        optionElement.textContent = categorie;
        selectElement.appendChild(optionElement);
    });
}

fetch('/jee-project/api/site-controller/get-categories')
    .then((response) => response.json())
    .then((data) => {
        generateCategoryOptions(data);
    })
    .catch((error) => {
        console.error('Une erreur s\'est produite lors de la récupération des catégories :', error);
    });


submitBtn.addEventListener('click', function (event) {
    event.preventDefault();

    const name = nameInput.value;
    const city = cityInput.value;
    const category = categoryInput.value;

    const url = '/jee-project/api/site-controller/site-add';

    const formData = new URLSearchParams();
    formData.append('name', name);
    formData.append('city', city);
    formData.append('category', category);

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
            console.log('Site ajouté avec succès:', data);
            form.reset();
            loadSite('/jee-project/api/site-controller/get-sites')
        })
        .catch(error => {
            console.error('Erreur lors de l\'ajout de le site:', error);
        });
});

/**========================================================
 **                 loadSession
 *? Loads the session data from the provided URL.
 * @param {string} url - The URL to fetch the session data from.
 * @return {void}
 **=========================================================**/
function loadSession(url) {
    // Show the loader
    document.getElementById("loader").style.display = "block";
    fetch(url)
        .then(response => response.json())
        .then(response => process(response))
        .catch(error => console.log("Erreur : " + error));
}

/**========================================================
 **                 process
 *? Processes the session data and populates the table.
 * @param {Array} sessions - The session data to process.
 * @return {void}
 **=========================================================**/
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

const form = document.getElementById('add-session-form');
const codeInput = document.getElementById('session-code');
const dateInput = document.getElementById('session-date');
const fromHourInput = document.getElementById('session-fromhour');
const toHourInput = document.getElementById('session-tohour');
const descriptionInput = document.getElementById('session-description');

const disciplineInput = document.getElementById('session-discipline');
const siteInput = document.getElementById('session-site');
const typeInput = document.getElementById('session-type');
const categoryInput = document.getElementById('session-categorie');


const submitBtn = document.getElementById('submit-btn');

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

const selectDisciplineElement = document.getElementById('session-discipline');
const selectSiteElement = document.getElementById('session-site');
const selectTypeElement = document.getElementById('session-type');
const selectCategorieElement = document.getElementById('session-categorie');

/**========================================================
 **                 generateOptions
 *? Processes the site data and populates the table.
 * @param {Array} options - The data to process.
 * @param {} elements - Element in which we add the options
 * @return {void}
 **=========================================================**/
function generateOptions(options, element) {
    options.forEach((option) => {
        const optionElement = document.createElement('option');
        optionElement.value = option;
        optionElement.textContent = option;
        element.appendChild(optionElement);
    });
}

/**========================================================
 **                 generateOptionsForDisciplines
 *? Processes the site data and populates the table.
 * @param {Array} options - The data to process.
 * @param {} elements - Element in which we add the options
 * @return {void}
 **=========================================================**/
function generateOptionsForDisciplines(data, selectElement) {
    // Clear existing options
    selectElement.innerHTML = '';

    // Generate options for disciplines
    data.forEach((discipline) => {
        const option = document.createElement('option');
        option.value = discipline.name; // Use the appropriate property as the value
        option.textContent = discipline.name; // Use the appropriate property as the label
        selectElement.appendChild(option);
    });
}
/**========================================================
 **                 generateOptionsForSites
 *? Processes the site data and populates the table.
 * @param {Array} options - The data to process.
 * @param {} elements - Element in which we add the options
 * @return {void}
 **=========================================================**/
function generateOptionsForSites(data, selectElement) {
    // Clear existing options
    selectElement.innerHTML = '';

    // Generate options for sites
    data.forEach((site) => {
        const option = document.createElement('option');
        option.value = site.id; // Use the appropriate property as the value
        option.textContent = site.name; // Use the appropriate property as the label
        selectElement.appendChild(option);
    });
}

fetch('/jee-project/api/session-controller/get-categories')
    .then((response) => response.json())
    .then((data) => {
        generateOptions(data, selectCategorieElement);
    })
    .catch((error) => {
        console.error('Une erreur s\'est produite lors de la récupération des catégories :', error);
    });

fetch('/jee-project/api/session-controller/get-types')
    .then((response) => response.json())
    .then((data) => {
        generateOptions(data, selectTypeElement);
    })
    .catch((error) => {
        console.error('Une erreur s\'est produite lors de la récupération des catégories :', error);
    });

fetch('/jee-project/api/discipline-controller/get-disciplines')
    .then((response) => response.json())
    .then((data) => {
        generateOptionsForDisciplines(data, selectDisciplineElement);
    })
    .catch((error) => {
        console.error('Une erreur s\'est produite lors de la récupération des catégories :', error);
    });

fetch('/jee-project/api/site-controller/get-sites')
    .then((response) => response.json())
    .then((data) => {
        generateOptionsForSites(data, selectSiteElement);
    })
    .catch((error) => {
        console.error('Une erreur s\'est produite lors de la récupération des catégories :', error);
    });

submitBtn.addEventListener('click', function (event) {
    event.preventDefault();

    const code = codeInput.value;
    
    if (code.trim() === '') {
	    alert("Vous devez renseigner un code");
	    return;
  	}
  	
    const date = dateInput.value;
    
    if (date.trim() === '') {
	    alert("Vous devez renseigner une date");
	    return;
  	}
  	
    const fromHour = fromHourInput.value;
    
    if (fromHour.trim() === '') {
	    alert("Vous devez renseigner une heure de début valide");
	    return;
  	}
  	
    const toHour = toHourInput.value;
    
    if (toHour.trim() === '') {
	    alert("Vous devez renseigner une heure de fin valide");
	    return;
  	}
  	
    const discipline = disciplineInput.value;
  
    var description = descriptionInput.value;
    
   	if (description.trim() === '') {
	    description = " "
  	}
  	
    const site = siteInput.value;
    const type = typeInput.value;
    const category = categoryInput.value;

    const url = '/jee-project/api/session-controller/session-add';

    const formData = new URLSearchParams();
    formData.append('code', code);
    formData.append('date', date);
    formData.append('fromHour', fromHour);
    formData.append('toHour', toHour);
    formData.append('discipline', discipline);
    formData.append('site', site);
    formData.append('description', description);
    formData.append('type', type);
    formData.append('category', category);
    
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData 
    })
        .then(response => {
         
            if (response.status === 409) {
            	alert('Cette session chevauche une autre de la même discipline, ajout impossible')
            	throw new Error('Conflict timeline');
        	}
        	
        	if (response.status === 400) {
            	alert('Le format des heures doit respecter le format suivant hh:mm')
            	throw new Error('Conflict timeline');
        	}
        	
        	if (!response.ok) {
                throw new Error('Error network');
            }
        	
            return response.json();
        })
        .then(data => {
           console.log('Session ajouté avec succès:', data);
           form.reset();
           loadSession('/jee-project/api/session-controller/get-sessions');
        })
        .catch(error => {
            console.error('Erreur lors de l\'ajout de la session:', error);
        });
});
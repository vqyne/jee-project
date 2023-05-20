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
        
        if(!(a.linked)){
			var td_delete = document.createElement("th");
					
			var deleteIcon = document.createElement("img");
			deleteIcon.setAttribute("alt", "");
			deleteIcon.setAttribute("width", "24");
			deleteIcon.setAttribute("height", "24");
			deleteIcon.setAttribute("src", "/jee-project/img/trash.svg"); 
			td_delete.setAttribute("id","delete-icon-"+a.name)
					
			td_delete.append(deleteIcon)
			td_delete.classList.add('px-6', 'py-4', 'font-medium', 'text-gray-900', 'whitespace-nowrap', 'dark:text-white')
			td_delete.setAttribute("scope", "col"); 
					
			td_delete.addEventListener('click',function(){

				const url = '/jee-project/api/discipline-controller/delete-discipline/'+this.getAttribute('id').split('-')[2];;

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
				    console.log('Discipline supprimée avec succès:', data);
				    loadDiscipline('/jee-project/api/discipline-controller/get-disciplines')
				  })
				  .catch(error => {
				    console.error('Erreur lors de la suppression de la discipline:', error);
				  });

			})
		}
        
        tr.appendChild(td_name);
        tr.appendChild(td_flag);
        if(!(a.linked)){
			tr.appendChild(td_delete);
		}
        table.appendChild(tr);
    }
    // Show the loader
    document.getElementById("loader").style.display = "none";
}
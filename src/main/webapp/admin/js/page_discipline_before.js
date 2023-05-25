	
		function loadAthleteSession(urlAthlete,urlSession) {
			fetch(urlAthlete)
			.then(response => response.json())
			.then(response => processAthlete(response))
			.catch(error => console.log("Erreur : " + error));
			
			fetch(urlSession)
			.then(response => response.json())
			.then(response => processSession(response))
			.catch(error => console.log("Erreur : " + error));
		}
		
		function processAthlete(athletes) {
			
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
		}
		
		function processSession(sessions) {
			
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
		}
		

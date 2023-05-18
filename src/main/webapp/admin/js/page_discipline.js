const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const disciplineParam = urlParams.get('d')

const h2 = document.getElementById("h2-discipline");
h2.textContent = disciplineParam

h2.addEventListener("click", function () {
    var h2 = this;
    var text = h2.innerText;

    var input = document.createElement("input");
    input.value = text;
    input.className = "text-center text-4xl font-bold mb-4";

    h2.parentNode.replaceChild(input, h2);

    input.focus();

    input.addEventListener("keydown", function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            var newName = input.value;

            var newH2 = document.createElement("h2");
            newH2.innerText = newName;
            newH2.classList.add("text-center", "text-4xl", "font-bold", "mb-4")

            const url = '/jee-project/api/discipline-controller/discipline-edit/';

            const formData = new URLSearchParams();
            formData.append('name', disciplineParam);
            formData.append('newName', newName);

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
                    console.log('Discipline modifiée avec succès:', data);
                    var url = window.location.href;
                    var newUrl = url.replace("d=" + disciplineParam, "d=" + newName);
                    window.location.href = newUrl;
                })
                .catch(error => {
                    console.error('Erreur lors de la modification de la discipline:', error);
                });
        }
    });
});


loadAthleteSession('/jee-project/api/athlete-controller/get-athletes?discipline=' + disciplineParam, '/jee-project/api/session-controller/get-sessions?discipline=' + disciplineParam)

const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});
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
/**========================================================
 **                 togglePasswordVisibility
 * ? toggle the password visibility
 * @param None
 * @return {void}
 **=========================================================**/
function togglePasswordVisibility() {
    var passwordInput = document.getElementById("password");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
    } else {
        passwordInput.type = "password";
    }
}

// Check the logged-in status in localStorage
var isLoggedIn = localStorage.getItem('isLoggedIn');

// Make a GET request to the server-side endpoint
$.ajax({
    url: 'admin/getUserInfo', // Replace with the actual server-side endpoint URL
    method: 'GET',
    dataType: 'json',
    success: function (response) {

        if (isLoggedIn) {
            // User is logged in, redirect to admin/index.html
            window.location.href = '/jee-project/admin/index.html';
            
        } else {
            //pass
        }

    },
    error: function (xhr, status, error) {
        console.log('Error: ' + error);
        //window.location.href = '/jee-project/login.html';
        
    }
});

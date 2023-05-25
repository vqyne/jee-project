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
localStorage.setItem('isLoggedIn', 'false');

// Make a GET request to the server-side endpoint
$.ajax({
    url: 'admin/getUserInfo',
    method: 'GET',
    dataType: 'json',
    success: function (response) {
        console.log(isLoggedIn);
        // When login is successful, set the isLoggedIn flag to 'true'
        localStorage.setItem('isLoggedIn', 'true');
        // User is logged in, redirect to admin/index.html
        window.location.href = '/jee-project/admin/index.html';
    },
    error: function (xhr, status, error) {
        console.log('Error: ' + error);
        //window.location.href = '/jee-project/login.html';
    }
});

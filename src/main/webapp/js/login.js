/**==============================================
 **              togglePasswordVisibility
 *?  toggles the password visibility
 *@param None
 *@return Void
 *=============================================**/
function togglePasswordVisibility() {
    var passwordInput = document.getElementById("password");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
    } else {
        passwordInput.type = "password";
    }
}
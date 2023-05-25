// Make a GET request to the server-side endpoint
$.ajax({
    url: 'getUserInfo', // Replace with the actual server-side endpoint URL
    method: 'GET',
    dataType: 'json',
    success: function (response) {
        // Handle the response containing user information
        var username = response.login;
        var category = response.category;
        var isLoggedIn = response.isLoggedIn;

        // Use the retrieved information to update the HTML
        var usernameElement = document.querySelector('#userInfo h5');
        var categoryElement = document.querySelector('#userInfo span');

        usernameElement.textContent = username;
        categoryElement.textContent = category;

        if (isLoggedIn) {
            // console.log('Username: ' + username);
            // console.log('Category: ' + category);
            // console.log('Logged-in: ' + isLoggedIn);
            // After successful login
            localStorage.setItem('isLoggedIn', 'true');
            
        } else {
            // User is not logged in, redirect to index.html
            window.location.href = '/jee-project/login.html';
        }

    },
    error: function (xhr, status, error) {
        console.log('Error: ' + error);
        window.location.href = '/jee-project/login.html';
        
    }
});
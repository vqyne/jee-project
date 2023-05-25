// Disconnect function
function disconnect() {
    // Perform the disconnect logic using an endpoint URL
    $.ajax({
      url: '/jee-project/admin/disconnect',
      method: 'GET',
      success: function(response) {
        console.log('Successfully disconnected');
        localStorage.setItem('isLoggedIn', 'false');
        // Redirect the user to the desired page
        window.location.href = '/jee-project/login.html'; // Replace with the desired logout page URL
      },
      error: function(xhr, status, error) {
        // Handle the error response, e.g., display an error message to the user
        alert('Error: ' + error);
      }
    });
  }
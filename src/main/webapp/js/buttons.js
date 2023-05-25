document.addEventListener('DOMContentLoaded', function () {
  // Check the logged-in status in localStorage
  var isLoggedIn = localStorage.getItem('isLoggedIn');

  // Display buttons based on the logged-in status
  if (isLoggedIn === 'true') {
    // User is logged in, display the logged-in buttons
    document.getElementById('logoutButton').style.display = 'block';
    document.getElementById('dashboardButton').style.display = 'block';
    document.getElementById('loginButton').style.display = 'none';
  } else {
    // User is not logged in, display the log in button
    document.getElementById('loginButton').style.display = 'block';
    document.getElementById('logoutButton').style.display = 'none';
    document.getElementById('dashboardButton').style.display = 'none';
  }

  console.log(isLoggedIn);
})

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

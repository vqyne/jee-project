var themeToggleDarkIcon = document.getElementById('theme-toggle-dark-icon');
var themeToggleLightIcon = document.getElementById('theme-toggle-light-icon');

// Change the icons inside the button based on previous settings
if (localStorage.getItem('color-theme') === 'dark' || (!('color-theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
  themeToggleLightIcon.classList.remove('hidden');
} else {
  themeToggleDarkIcon.classList.remove('hidden');
}

var themeToggleBtn = document.getElementById('theme-toggle');

// Save the user's color theme preference when the page is unloaded or refreshed
window.addEventListener('beforeunload', function () {
  var currentTheme = document.documentElement.classList.contains('dark') ? 'dark' : 'light';
  localStorage.setItem('color-theme', currentTheme);
  var logo = document.getElementById('paris_logo');
  var darkModeLogo = document.getElementById('paris_logo_white');
  var fresque = document.getElementById('fresque');
  var fresqueWhite = document.getElementById('fresque_white');
  localStorage.setItem('logo-display', logo.style.display);
  localStorage.setItem('dark-mode-logo-display', darkModeLogo.style.display);
  localStorage.setItem('fresque-display', fresque.style.display);
  localStorage.setItem('fresque-white-display', fresqueWhite.style.display);
});

// Apply the saved color theme preference when the page loads
window.addEventListener('DOMContentLoaded', function () {
  if (localStorage.getItem('color-theme')) {
    if (localStorage.getItem('color-theme') === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }

  // Retrieve logo and fresque image preferences from localStorage
  if (localStorage.getItem('logo-display')) {
    var logo = document.getElementById('paris_logo');
    var darkModeLogo = document.getElementById('paris_logo_white');
    logo.style.display = localStorage.getItem('logo-display');
    darkModeLogo.style.display = localStorage.getItem('dark-mode-logo-display');
  }

  if (localStorage.getItem('fresque-display')) {
    var fresque = document.getElementById('fresque');
    var fresqueWhite = document.getElementById('fresque_white');
    fresque.style.display = localStorage.getItem('fresque-display');
    fresqueWhite.style.display = localStorage.getItem('fresque-white-display');
  }
});

themeToggleBtn.addEventListener('click', function () {
  var logo = document.getElementById('paris_logo');
  var darkModeLogo = document.getElementById('paris_logo_white');
  var fresque = document.getElementById('fresque');
  var fresqueWhite = document.getElementById('fresque_white');

  // toggle icons inside button
  themeToggleDarkIcon.classList.toggle('hidden');
  themeToggleLightIcon.classList.toggle('hidden');

  // if set via local storage previously
  if (localStorage.getItem('color-theme')) {
    if (localStorage.getItem('color-theme') === 'light') {
      document.documentElement.classList.add('dark');
      localStorage.setItem('color-theme', 'dark');
      logo.style.display = 'none';
      darkModeLogo.style.display = 'block';

      fresque.style.display = 'none';
      fresqueWhite.style.display = 'block';
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('color-theme', 'light');
      logo.style.display = 'block';
      darkModeLogo.style.display = 'none';

      fresque.style.display = 'block';
      fresqueWhite.style.display = 'none';
    }
  } else {
    if (document.documentElement.classList.contains('dark')) {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('color-theme', 'light');
      logo.style.display = 'block';
      darkModeLogo.style.display = 'none';

      fresque.style.display = 'block';
      fresqueWhite.style.display = 'none';
    } else {
      document.documentElement.classList.add('dark');
      localStorage.setItem('color-theme', 'dark');
      logo.style.display = 'none';
      darkModeLogo.style.display = 'block';

      fresque.style.display = 'none';
      fresqueWhite.style.display = 'block';
    }
  }
});

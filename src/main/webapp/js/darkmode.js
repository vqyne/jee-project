var themeToggleDarkIcon = document.getElementById('theme-toggle-dark-icon');
		var themeToggleLightIcon = document.getElementById('theme-toggle-light-icon');

		// Change the icons inside the button based on previous settings
		if (localStorage.getItem('color-theme') === 'dark' || (!('color-theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
			themeToggleLightIcon.classList.remove('hidden');
		} else {
			themeToggleDarkIcon.classList.remove('hidden');
		}

		var themeToggleBtn = document.getElementById('theme-toggle');

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

				// if NOT set via local storage previously
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
	    const paris_logo = document.getElementById("paris_logo");
	    paris_logo.addEventListener("click", function () {
	        window.location.href = '/jee-project/';
	    });
	
	    const searchInput = document.getElementById("searchInput");
	
	    searchInput.addEventListener("keyup", (event) => {
	        if (/[a-zA-Z]/.test(event.key)) {
	            executeSearch();
	        }
	    });
	
	    /**==============================================
	     **              executeSearch
	     *?  Executes a search based on the provided search term.
	     *@return {void}
	     *=============================================**/
	    function executeSearch() {
	        const searchTerm = searchInput.value;
	
	        if (searchTerm.length > 0) {
	            loadSessions('/jee-project/api/session-controller/get-sessions-code?code=' + searchTerm);
	        } else {
	            loadSessions('/jee-project/api/session-controller/get-sessions');
	        }
	    }
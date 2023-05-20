
/**========================================================
 **                  Overlapping issues
 *=========================================================**/
// Need to specifically import the distributed JS file
import 'overlapping-marker-spiderfier-leaflet/dist/oms';

/**========================================================
 **                       initializeMap
 *? Initializes the map.
 * @return {void}
 **=========================================================**/
function initializeMap() {
    // Initialize the map
    var map = L.map("map").setView([48.8566, 2.3522], 10);
    // map refers to your leaflet map object
    const oms = new window.OverlappingMarkerSpiderfier(map);
    // Add a tile layer for the map
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "Map data © <a href='https://openstreetmap.org'>OpenStreetMap</a> contributors",
        maxZoom: 18,
    }).addTo(map);

    // Fetch sessions data and create markers
    fetch("/jee-project/api/session-controller/get-sessions")
        .then((response) => response.json())
        .then((sessions) => {
            sessions.forEach(function (session) {
                // Geocode the session location using Mapbox Geocoding API
                var location = session.site.name + ", Paris"; // Update with the appropriate location field from your data

                fetch(
                    "https://api.mapbox.com/geocoding/v5/mapbox.places/" +
                    encodeURIComponent(location) +
                    ".json?access_token=pk.eyJ1Ijoia3JvbmVja2VyIiwiYSI6ImNsaG5qeW5vYzFuN24zaHFwZjg4b2c0ZTkifQ.eA48E0drQ_DRl-smLCiBRg"
                )
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.features.length > 0) {
                            //console.log(data);
                            var coordinates = data.features[0].center;
                            var latitude = coordinates[1];
                            var longitude = coordinates[0];

                            // Create a custom icon
                            var customIcon = L.icon({
                                iconUrl: assignIcon(session.site.category),
                                iconSize: [32, 32],
                            });
                            // Note access to constructor via window object
                            // Create a marker with the custom icon
                            var marker = L.marker([latitude, longitude], { icon: customIcon }).addTo(map);
                            marker.bindPopup(
                                "<div style='text-align: center;'>" +
                                "<b>" + session.code + "</b><br>" +
                                "<span style='display: block;'>" + session.description + "</span>" +
                                "<span style='display: block;'>" + session.site.name + ", " + session.site.city + "</span>" +
                                "<span style='display: block;'>" + session.date + "</span>" +
                                "<span style='display: block;'>" + session.fromHour + " - " + session.toHour + "</span>" +
                                "</div>"
                            );
                            oms.addListener('click', (marker) => {
                                // Your callback when marker is clicked
                              });
                              // Markers need to be added to OMS to spider overlapping markers
                            markers.forEach((marker) => {
                                oms.addMarker(marker);
                            });
                        }
                    })
                    .catch((error) => console.error("Error: " + error));
            });
        })
        .catch((error) => alert("Error: " + error));
}

/**========================================================
 **                 assignIcon
 * ? Assigns an icon based on the given category.
 * @param {string} category - The category to assign an icon for.
 * @return {string} - The URL of the icon.
 **=========================================================**/

function assignIcon(category) {
    switch (category) {
        case 'stade':
            return 'category/stadium.png';
        case 'salle_de_spectacle':
            return 'category/spectacle.png';
        case 'lieu_public':
            return 'category/public.png';
        case 'centre_aquatique':
            return 'category/pool.png';
        case 'salle':
            return 'category/salle.png';
        case 'gymnase':
            return 'category/gymnase.png';
        case 'golf':
            return 'category/golf.png';
    }
}


/**========================================================
 **                       Event Listeners
 *=========================================================**/

// Event listener for clicking the Paris logo
const paris_logo = document.getElementById("paris_logo");
paris_logo.addEventListener("click", function () {
    window.location.href = '/jee-project/';
});

    



// Conversely use oms.removeMarker(marker) if a marker is removed
/**========================================================
 **                Execution and Initialization
 *=========================================================**/

// Initialize the map
initializeMap();

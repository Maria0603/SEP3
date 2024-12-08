let map;
let donut;
let marker;

export function load_map() {
    // Initialize the map
    map = L.map('mapContainer').setView([51.505, -0.09], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 17,
        minZoom: 5
    }).addTo(map);
    const geocoder = L.Control.geocoder({defaultMarkGeocode: false}).addTo(map);
    console.log("This message is from the leafletmap.js file");
    geocoder.on('markgeocode', function (e) {
        const bbox = e.geocode.bbox; // Get the bounding box of the result
        const bounds = L.latLngBounds(bbox); // Convert to LatLngBounds
        map.fitBounds(bounds); // Adjust map to fit the bounds
        console.log('Geocode result:', e.geocode); // Log the geocode result for debugging
    });
    // Add a marker at the map center
    marker = L.marker(map.getCenter()).addTo(map);

    // Check if L.donut is available
    console.log("L.donut is available:", typeof L.donut);

    // Create the donut overlay
    donut = L.donut(map.getCenter(), {
        radius: 20000000,               // Outer radius in meters
        innerRadius: 5000,           // Inner radius in meters
        innerRadiusAsPercent: false, // Use meters, not percent
        color: '#000',              // Outer ring color
        weight: 2,                  // Border thickness
    }).addTo(map);

    map.on('move', () => {
        if (donut) {
            donut.setLatLng(map.getCenter());
        }
        if (marker) {
            marker.setLatLng(map.getCenter());
        }
    });
}


export function update_circle_radius(radius) {
    // Update the overlay with the new radius
    if (donut) {
        donut.setInnerRadius(radius * 1000);
    }
}

export function get_map_center_and_radius() {
    if (map && donut) {
        const center = map.getCenter();
        const radius = donut.getInnerRadius(); // Assuming inner radius is in meters
        console.log("Center:", center, "Radius:", radius);
        return {
            latitude: center.lat,
            longitude: center.lng,
            radius: radius / 1000, // Convert radius to kilometers
        };
    }
    return null; // Return null if map or donut is not initialized
}


export function set_zoom_level(zoomLevel) {
    if (map) {
        map.setZoom(zoomLevel);
    }
}

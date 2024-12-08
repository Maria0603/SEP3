let map;
let donut;
let marker;

export function load_map(latitude = 56.156486066837665, longitude = 10.19591121665965, radius = 10) {
    // Check if the map has already been initialized
    if (map) {
        // If the map is already initialized, just update the center and radius
        map.setView([latitude, longitude], 10);
        update_circle_radius(radius);
        return;
    }
    
    // Initialize the map
    map = L.map('mapContainer').setView([latitude, longitude], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 17,
        minZoom: 5
    }).addTo(map);
    const geocoder = L.Control.geocoder({defaultMarkGeocode: false}).addTo(map);
    console.log("This message is from the leafletmap.js file");

    // Adjust the map's view based on the radius
    fit_map_to_radius(latitude, longitude, radius);

    geocoder.on('markgeocode', function (e) {
        const bbox = e.geocode.bbox; // Get the bounding box of the result
        const bounds = L.latLngBounds(bbox); // Convert to LatLngBounds
        map.fitBounds(bounds); // Adjust map to fit the bounds
        console.log('Geocode result:', e.geocode); // Log the geocode result for debugging
    });

    // Add a marker at the provided location
    marker = L.marker([latitude, longitude]).addTo(map);

    // Create the donut overlay
    donut = L.donut([latitude, longitude], {
        radius: radius * 1000, // Outer radius in meters
        innerRadius: 0,     // Inner radius in meters
        innerRadiusAsPercent: false, // Use meters, not percent
        color: '#000',         // Outer ring color
        weight: 2,             // Border thickness
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
    if (donut)
        donut.setRadius(radius * 1000); // radius in meters
    if (map && donut)
        fit_map_to_radius(map.getCenter().lat, map.getCenter().lng, radius);
}


export function set_zoom_level(zoomLevel) {
    if (map) {
        map.setZoom(zoomLevel);
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


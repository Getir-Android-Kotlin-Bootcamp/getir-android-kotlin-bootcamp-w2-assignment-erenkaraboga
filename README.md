

<h1>LocationPicker</h1>

<h2>Purpose</h2>
<p>This app helps the user to pick their location and displays the selected location's address by reverse geocoding. It utilizes Google Maps API and Places API for location selection.</p>

<h2>Features</h2>
<ul>
<li>Displaying and selecting user's location on the map.</li>
<li>Autocomplete location search functionality.</li>
<li>Permission checking and location service control.</li>
<li>Reverse geocoding the selected location and displaying its address.</li>
<li>Checking GPS services and displaying necessary alerts to the user.</li>
</ul>

<h2>Technologies Used</h2>
<ul>
<li>Google Maps Android API</li>
<li>Google Reverse Geocode API</li>
<li>Google Places API</li>
<li>Android components (ViewModel, LiveData)</li>
<li>Kotlin Coroutines</li>
<li>Retrofit</li>
<li>Dagger - Hilt</li>
<li>Clean Architecture</li>
<li>MVVM</li>
</ul>

<h2>Key Methods and Functions</h2>
<ul>
<li><strong>onCreate()</strong>: Called when the activity is created. Initializes views, sets up listeners, checks permissions, and starts location services.</li>
<li><strong>startLocationSearch()</strong>: Initiates the autocomplete location search process.</li>
<li><strong>getLocation()</strong>: Retrieves the device's current location. If location permission is granted and location services are enabled, it fetches the location and displays it on the map.</li>
<li><strong>showLocationServicePopup()</strong>: Checks location services and shows an appropriate dialog box if necessary.</li>
<li><strong>observeReverseAddress()</strong>: Observes the reverse address lookup and displays the address when it's successfully obtained.</li>
<li><strong>handleStateChange()</strong>: Handles the state change of reverse address lookup and displays the address when it's successful.</li>
<li><strong>animateCamera(latLng: LatLng)</strong>: Animates the camera to a specific latitude and longitude on the map.</li>
<li><strong>addMarkerToLocation(latLng: LatLng)</strong>: Adds a marker to a specific latitude and longitude on the map.</li>
<li><strong>isLocationEnabled(): Boolean</strong>: Checks if location services are enabled on the device.</li>
<li><strong>checkPermissions()</strong>: Checks and requests necessary permissions for location access.</li>
<li><strong>onActivityResult()</strong>: Handles the result from open location service pop-up.</li>
</ul>


<h2>Associated Classes and Resources</h2>
<ul>
<li>LocationPickerViewModel: Manages the location selection logic.</li>
<li>AutocompleteSupportFragment: Provides autocomplete for location search.</li>
<li>Google Maps Android API: Provides map functionality.</li>
<li>Google Places API: Provides location search functionality.</li>
<li>Google Reverse Geocode API: Provides get address detail from latlng </li>
</ul>

<h2>Notes</h2>
<ul>
<li>This app utilizes Google Maps API for selecting and displaying the user's location.</li>
<li>The built-in geocoding package in Kotlin was NOT USED because Google Reverse Geocoding API was utilized to ensure accuracy.</li>


<h2>Apk Drive Link</h2>
https://drive.google.com/file/d/1PLks1f8IUa7oHFcEuz0K71vr_9-0JEqc/view?usp=sharing

https://github.com/Getir-Android-Kotlin-Bootcamp/getir-android-kotlin-bootcamp-w2-assignment-erenkaraboga/assets/74095539/0d212420-c20c-4167-9205-752b7b93984e
</body>
</html>

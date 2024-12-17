document.addEventListener("DOMContentLoaded", function () {

    if (!sessionStorage.getItem('resetPerformed')) {


        // Initial connection message
        updateStatusMessage("Connecting...", "status-connecting");


        // Automatically call the reset function on page load
        const resetButton = document.getElementById("resetButton");
        handleButtonClick(resetButton, "http://localhost:8080/api/reset", "Ready to brew", "status-ready");
    }
    else {
        // Initial connection message
        updateStatusMessage("Go to settings to choose beer-type, amount and speed ", "status-connecting");
    }
});
function updateStatusMessage(text, statusClass) {
    const statusMessageText = document.getElementById("statusMessageText");
    const brewingStatus = document.getElementById("brewingStatus");

    statusMessageText.innerText = text;
    brewingStatus.className = ""; // Clear previous status classes
    brewingStatus.classList.add(statusClass);
}

function handleButtonClick(button, url, statusText, statusClass) {
    button.classList.add("loading");
    button.disabled = true;

    fetch(url, {method: "POST"})
        .then(response => response.ok ? response.text() : Promise.reject("Request failed"))
        .then(data => {
            console.log(data);
            updateStatusMessage(data, statusClass);
        })
        .catch(error => {
            console.error("Error:", error);
            updateStatusMessage("There was an error processing the request.", "status-error");
        })
        .finally(() => {
            button.classList.remove("loading");
            button.disabled = false;
        });
}

document.getElementById("startButton").addEventListener("click", function () {
    handleButtonClick(this, "http://localhost:8080/api/start", "Brewing process started successfully!", "status-started");
});

document.getElementById("resetButton").addEventListener("click", function () {
    handleButtonClick(this, "http://localhost:8080/api/reset", "Ready to brew.", "status-paused");
});

document.getElementById("stopButton").addEventListener("click", function () {
    handleButtonClick(this, "http://localhost:8080/api/stop", "Brewing process stopped.", "status-stopped");
});
// Create a new EventSource instance to connect to the SSE endpoint
const evtSource = new EventSource('http://127.0.0.1:8080/api/brew-status-stream');


// Handle any errors that occur
evtSource.onerror = function (event) {
    console.error("EventSource failed:", event);
    evtSource.close(); // Close the connection if errors occur
};

// Optionally handle specific named events if your server sends them
evtSource.addEventListener('produced', function (event) {
    const data = JSON.parse(event.data); // Assuming JSON data is sent
    console.log("Produced amount:", data);
    document.getElementById('batchReportProduced').innerHTML = "Produced: " + data + " Bottles";

});
evtSource.addEventListener('temperature', function (event) {
    const data = JSON.parse(event.data);
    console.log("Temperature amount:", data);
    document.getElementById('batchRepTemperature').innerHTML = "Temperature: " + data + "°C";

});

evtSource.addEventListener('humidity', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    document.getElementById('batchReportHumidity').innerHTML = "Humidity: " + data + "%";

});
evtSource.addEventListener('defective', function (event) {
    const data = JSON.parse(event.data);
    console.log("Defect beers:", data);
    document.getElementById('batchReportDefects').innerText = "Defects: " + data + " Bottles";

});

evtSource.addEventListener('state', function (event) {
    const data = JSON.parse(event.data);
    console.log("State amount:", data);
    document.getElementById('stateId').innerHTML = "State: " + data;
});

// Ingredients ******************

evtSource.addEventListener('hops', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    percent = (data/35000) * 100
    document.getElementById('hops').innerHTML = percent.toFixed(2) + "%";

});
evtSource.addEventListener('barley', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    percent = (data/35000) * 100
    document.getElementById('barley').innerHTML = percent.toFixed(2) + "%";

});
evtSource.addEventListener('wheat', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    percent = (data/35000) * 100
    document.getElementById('wheat').innerHTML = percent.toFixed(2) + "%";

});
evtSource.addEventListener('malt', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    percent = (data/35000) * 100
    document.getElementById('malt').innerHTML = percent.toFixed(2) + "%";

});
evtSource.addEventListener('yeast', function (event) {
    const data = JSON.parse(event.data);
    console.log("Humidity amount:", data);
    percent = (data/35000) * 100
    document.getElementById('yeast').innerHTML = percent.toFixed(2) + "%";

});

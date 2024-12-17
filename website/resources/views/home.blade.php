<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="{{ csrf_token() }}">
    <title>Home Brew Hub Dashboard</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
</head>
<body>
<!-- Sidebar -->
@include('partials.header')
@include('partials.sidebar')

<!-- Main Content -->
<div class="content">
    <h2>Dashboard</h2>
    <p>Welcome to the brewing management dashboard. Here, you can control and monitor the brewing process.</p>
    <br>
    <!-- Control Buttons -->
    <div class="button-group">
        <button id="startButton" class="start-button">Start Brewing</button>
        <button id="resetButton" class="reset-button">Reset Brewing</button>
        <button id="stopButton" class="stop-button">Stop Brewing</button>
        <!--        <button class="report-button">Generate Report</button>-->
        <!--        <button class="history-button">View Batch History</button>-->
    </div>

    <!-- Dashboard Sections -->
    <div class="dashboard-section">
        <div class="box">
            <h3>Current Brewing Status</h3>
            <p id="brewingStatus">
                <span class="status-icon">&#128345;</span> <!-- Clock emoji as a placeholder icon -->
                <span id="statusMessageText">Live status of the ongoing brewing process will be displayed here.</span>
            </p>
        </div>

        <!-- <div class="box">
            <h3>Upcoming Batches</h3>
            <p>Details about upcoming batches, scheduled timings, and ingredients will be shown here.</p>
        </div> -->

        <div class="box">
            <h3>Recent Reports</h3>
            <p>Quick access to the most recent reports generated will be available here.</p>
        </div>
    </div>
    <!--************************************************************************-->


    <div class="box" id="brewStatus">

        <h3 id="stateId">Current state</h3>


    </div>
    <!--************************************************************************-->


    <!-- Batch Overview Section -->
    <div class="batch-overview">
        <div class="batch-details">
            <h3><img src="{{ asset('Images/details-icon.png') }}" alt="Details Icon" class="icon"> Batch Details</h3>
            <ul>
                <li>
                    <img src="{{ asset('Images/temperature-icon.png') }}" alt="Temperature Icon" class="inline-icon">
                    <span id="batchRepTemperature"> - °C</span>
                </li>
                <li>
                    <img src="{{ asset('Images/humidity-icon.png') }}" alt="Humidity Icon" class="inline-icon">
                    <span id="batchReportHumidity"> - %</span>
                </li>
                <li>
                    <img src="{{ asset('Images/production-icon.png') }}" alt="Produced Icon" class="inline-icon">
                    <span id="batchReportProduced"> - Bottles</span>
                </li>
                <li>
                    <img src="{{ asset('Images/defect-icon.png') }}" alt="Defects Icon" class="inline-icon">
                    <span id="batchReportDefects"> - Bottles</span>
                </li>
            </ul>
        </div>
        <div class="ingredient-levels">
            <h3><img src="{{ asset('Images/ingredients-icon.png') }}" alt="Ingredients Icon" class="icon"> Ingredient
                Levels</h3>
            <ul>
                <li>
                    <img src="{{ asset('Images/barley-icon.png') }}" alt="Barley Icon" class="inline-icon">
                    <strong>Barley:</strong> <span id="barley"> - %</span>
                </li>
                <li>
                    <img src="{{ asset('Images/hops-icon.png') }}" alt="Hops Icon" class="inline-icon">
                    <strong>Hops:</strong> <span id="hops"> - %</span>
                </li>
                <li>
                    <img src="{{ asset('Images/malt-icon.png') }}" alt="Malt Icon" class="inline-icon">
                    <strong>Malt:</strong> <span id="malt"> - %</span>
                </li>
                <li>
                    <img src="{{ asset('Images/wheat-icon.png') }}" alt="Wheat Icon" class="inline-icon">
                    <strong>Wheat:</strong> <span id="wheat"> - %</span>
                </li>
                <li>
                    <img src="{{ asset('Images/yeast-icon.png') }}" alt="Yeast Icon" class="inline-icon">
                    <strong>Yeast:</strong> <span id="yeast"> - %</span>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script>
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


</script>
</body>
</html>

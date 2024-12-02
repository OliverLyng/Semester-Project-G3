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
        <button class="report-button">Generate Report</button>
        <button class="history-button">View Batch History</button>
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
        <br>
        <h3 id="producedId">Produced</h3>
        <h3 id="temperatureId">Temperature</h3>
        <h3 id="humidityId">Humility</h3>
        <h3 id="stateId">Current state</h3>
        <script>
            // Create a new EventSource instance to connect to the SSE endpoint
            const evtSource = new EventSource('http://127.0.0.1:8080/api/brew-status-stream');

            // Handle messages received from the server
            // evtSource.onmessage = function (event) {
            //     const data = JSON.parse(event.data); // Parse the JSON data received
            //     document.getElementById('producedId').innerHTML = data;
            // };

            // Handle any errors that occur
            evtSource.onerror = function (event) {
                console.error("EventSource failed:", event);
                evtSource.close(); // Close the connection if errors occur
            };

            // Optionally handle specific named events if your server sends them
            evtSource.addEventListener('produced', function (event) {
                const data = JSON.parse(event.data); // Assuming JSON data is sent
                console.log("Produced amount:", data);
                document.getElementById('producedId').innerHTML = "Produced: " + data;
            });
            evtSource.addEventListener('temperature', function(event) {
                const data = JSON.parse(event.data);
                console.log("Temperature amount:", data);
                document.getElementById('temperatureId').innerHTML = "Temperature: " + data + "°C";
            });

            evtSource.addEventListener('humidity', function(event) {
                const data = JSON.parse(event.data);
                console.log("Humidity amount:", data);
                document.getElementById('humidityId').innerHTML = "Humidity: " + data + "%";
            });

            evtSource.addEventListener('state', function(event) {
                const data = JSON.parse(event.data);
                console.log("State amount:", data);
                document.getElementById('stateId').innerHTML = "State: " + data;
            });

        </script>
    </div>
    <!--************************************************************************-->


    <!-- Batch Overview Section -->
    <div class="batch-overview">
        <div class="batch-details">
            <h3><img src="{{ asset('Images/details-icon.png') }}" alt="Details Icon" class="icon"> Batch Details</h3>
            <ul>
                <li>
                    <img src="{{ asset('Images/temperature-icon.png') }}" alt="Temperature Icon" class="inline-icon">
                    <strong>Temperature:</strong> <span>22°C</span>
                </li>
                <li>
                    <img src="{{ asset('Images/humidity-icon.png') }}" alt="Humidity Icon" class="inline-icon">
                    <strong>Humidity:</strong> <span>45%</span>
                </li>
                <li>
                    <img src="{{ asset('Images/vibration-icon.png') }}" alt="Vibration Icon" class="inline-icon">
                    <strong>Vibration:</strong> <span>Low</span>
                </li>
                <li>
                    <img src="{{ asset('Images/production-icon.png') }}" alt="Produced Icon" class="inline-icon">
                    <strong>Produced:</strong> <span>120 Bottles</span>
                </li>
                <li>
                    <img src="{{ asset('Images/defect-icon.png') }}" alt="Defects Icon" class="inline-icon">
                    <strong>Defects:</strong> <span>2 Bottles</span>
                </li>
            </ul>
        </div>
        <div class="ingredient-levels">
            <h3><img src="{{ asset('Images/ingredients-icon.png') }}" alt="Ingredients Icon" class="icon"> Ingredient
                Levels</h3>
            <ul>
                <li>
                    <img src="{{ asset('Images/barley-icon.png') }}" alt="Barley Icon" class="inline-icon">
                    <strong>Barley:</strong> <span>75%</span>
                </li>
                <li>
                    <img src="{{ asset('Images/hops-icon.png') }}" alt="Hops Icon" class="inline-icon">
                    <strong>Hops:</strong> <span>60%</span>
                </li>
                <li>
                    <img src="{{ asset('Images/malt-icon.png') }}" alt="Malt Icon" class="inline-icon">
                    <strong>Malt:</strong> <span>80%</span>
                </li>
                <li>
                    <img src="{{ asset('Images/wheat-icon.png') }}" alt="Wheat Icon" class="inline-icon">
                    <strong>Wheat:</strong> <span>50%</span>
                </li>
                <li>
                    <img src="{{ asset('Images/yeast-icon.png') }}" alt="Yeast Icon" class="inline-icon">
                    <strong>Yeast:</strong> <span>90%</span>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script>
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
        handleButtonClick(this, "http://localhost:8080/api/pause", "Brewing process paused.", "status-paused");
    });

    document.getElementById("stopButton").addEventListener("click", function () {
        handleButtonClick(this, "http://localhost:8080/api/stop", "Brewing process stopped.", "status-stopped");
    });

</script>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Brew Hub Dashboard</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="logo-container">
            <img src="{{ asset('Images/logo.png') }}" alt="Home Brew Logo" class="logo"> 
        </div>
        <h1 class="title">Home Brew Hub</h1>
    </div>

    <!-- Sidebar -->
    <div class="sidebar">
        <div class="menu-item">
            <img src="{{ asset('Images/dashboard-icon.png') }}" alt="Dashboard Icon" class="icon"> 
            <a href="{{ route('dashboard') }}">Dashboard</a>
        </div>
        <div class="menu-item">
            <img src="{{ asset('Images/scheduling-icon.png') }}" alt="Scheduling Icon" class="icon"> 
            <a href="{{ route('scheduling') }}">Scheduling</a>
        </div>
        <div class="menu-item">
            <img src="{{ asset('Images/current-batch-icon.png') }}" alt="Current Batch Icon" class="icon"> 
            <a href="{{ route('current_batch') }}">Current Batch</a>
        </div>
        <div class="menu-item">
            <img src="{{ asset('Images/inventory-icon.png') }}" alt="Inventory Icon" class="icon"> 
            <a href="{{ route('inventory') }}">Inventory</a>
        </div>
        <div class="menu-item">
            <img src="{{ asset('Images/history-icon.png') }}" alt="History Icon" class="icon"> 
            <a href="{{ route('history') }}">History</a>
        </div>
        <div class="menu-item">
            <img src="{{ asset('Images/report-icon.png') }}" alt="Reports Icon" class="icon"> 
            <a href="{{ route('reports') }}">Reports</a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="content">
        <h2>Dashboard</h2>
        <p>Welcome to your brewing management dashboard. Here, you can control and monitor your brewing process.</p>
        
        <!-- Control Buttons -->
        <div class="button-group">
            <button id="startButton" class="start-button">Start Brewing</button>
            <button id="pauseButton" class="pause-button">Pause Brewing</button>
            <button id="stopButton" class="stop-button">Stop Brewing</button>
            <button class="report-button">Generate Report</button>
            <button class="history-button">View Batch History</button>
        </div>
        
        <!-- Dashboard Sections -->
        <div class="dashboard-section">
            <div class="box"> 
                <h3>Current Brewing Status</h3>
                <p>Live status of the ongoing brewing process will be displayed here.</p>
            </div>
            <div class="box">
                <h3>Upcoming Batches</h3>
                <p>Details about upcoming batches, scheduled timings, and ingredients will be shown here.</p>
            </div>
            <div class="box">
                <h3>Recent Reports</h3>
                <p>Quick access to the most recent reports generated will be available here.</p>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
    document.getElementById("startButton").addEventListener("click", function () {
    fetch("http://localhost:8080/api/start", { method: "POST" })
    .then(response => response.ok ? response.text() : Promise.reject("Failed to start brewing"))
    .then(data => {
        console.log(data);
        document.getElementById("statusMessage").innerText = data;
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById("statusMessage").innerText = "There was an error starting the brewing process.";
    });
    });

    document.getElementById("pauseButton").addEventListener("click", function () {
    fetch("http://localhost:8080/api/pause", { method: "POST" })
    .then(response => response.ok ? response.text() : Promise.reject("Failed to pause brewing"))
    .then(data => {
        console.log(data);
        document.getElementById("statusMessage").innerText = data;
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById("statusMessage").innerText = "There was an error pausing the brewing process.";
    });
    });

    document.getElementById("stopButton").addEventListener("click", function () {
    fetch("http://localhost:8080/api/stop", { method: "POST" })
    .then(response => response.ok ? response.text() : Promise.reject("Failed to stop brewing"))
    .then(data => {
        console.log(data);
        document.getElementById("statusMessage").innerText = data;
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById("statusMessage").innerText = "There was an error stopping the brewing process.";
    });
    });
    </script>
</body>
</html>

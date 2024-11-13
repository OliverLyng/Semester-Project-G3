<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Brew Hub Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="logo-container">
            <img src="Images/logo.png" alt="Home Brew Logo" class="logo"> 
        </div>
        <h1 class="title">Home Brew Hub</h1>
    </div>
    
<!-- Sidebar -->
<div class="sidebar">
    <div class="menu-item">
        <img src="Images/dashboard-icon.png" alt="Dashboard Icon" class="icon"> 
        <span>Dashboard</span>
    </div>
    <div class="menu-item">
        <img src="Images/scheduling-icon.png" alt="Scheduling Icon" class="icon"> 
        <span>Scheduling</span>
    </div>
    <div class="menu-item">
        <img src="Images/current-batch-icon.png" alt="Current Batch Icon" class="icon"> 
        <span>Current Batch</span>
    </div>
    <div class="menu-item">
        <img src="Images/inventory-icon.png" alt="Inventory Icon" class="icon"> 
        <span>Inventory</span>
    </div>
    <div class="menu-item">
        <img src="Images/history-icon.png" alt="History Icon" class="icon"> 
        <span>History</span>
    </div>
    <div class="menu-item">
        <img src="Images/report-icon.png" alt="Reports Icon" class="icon"> 
        <span>Reports</span>
    </div>
</div>

    <!-- Main Content -->
    <div class="content">
        <h2>Dashboard</h2>
        <p>Welcome to your brewing management dashboard. Here, you can control and monitor your brewing process.</p>
        
        <!-- Control Buttons -->
        <div class="button-group">
            <button class="start-button">Start Brewing</button>
            <button class="pause-button">Pause Brewing</button>
            <button class="stop-button">Stop Brewing</button>
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
</body>
</html>

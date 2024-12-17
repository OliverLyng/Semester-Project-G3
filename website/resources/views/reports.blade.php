<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Batch Reports - Home Brew Hub</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <!-- For charts -->
</head>
<body>
    @include('partials.header')
    @include('partials.sidebar')

    <div class="content">
        <h2>Batch Reports</h2>
        <p>Select a batch to generate its report:</p>

        <!-- Batch Selection -->
        <div class="report-controls">
            <label for="batchID">Batch ID:</label>
            <select id="batchID" name="batchID">
                <option value="">Select a Batch</option>
                @foreach ($batches as $batch)
                <option value="{{ $batch->id }}">Batch {{ $batch->id }}</option>
                @endforeach
            </select>
            <button id="generateReport" class="button">Generate</button>
            <button id="downloadPDF" class="button">Download PDF</button>
        </div>

        <!-- Report Output -->
        <div id="reportOutput" style="margin-top: 20px;">
            <!-- Table for textual data -->
            <div id="batchDetails">
                <h3>Batch Details</h3>
                <table class="report-table">
                    <thead>
                        <tr>
                            <th>Field</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody id="batchDetailsBody">
                        <!-- Data will be injected dynamically -->
                    </tbody>
                </table>
            </div>

            <!-- Charts -->
<!--            <div id="charts">-->
<!--                <h3>Temperature and Humidity Logs</h3>-->
<!--                <canvas id="temperatureChart"></canvas>-->
<!--                <canvas id="humidityChart" style="margin-top: 30px;"></canvas>-->
<!--            </div>-->
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const generateReportButton = document.getElementById("generateReport");
            const batchIDSelect = document.getElementById("batchID");
            const batchDetailsBody = document.getElementById("batchDetailsBody");
            //
            // // Example data for testing (replace this with real API calls)
            // const batchData = {
            //     1: {
            //         batchID: 1,
            //         productType: "Pilsner",
            //         totalProducts: 1200,
            //         acceptable: 1150,
            //         defective: 50,
            //         timeInStates: {
            //             idle: "2 hours",
            //             production: "8 hours",
            //             cleaning: "1 hour",
            //         },
            //         temperatureLogs: [20, 21, 23, 22, 24, 23],
            //         humidityLogs: [45, 46, 50, 48, 47, 45],
            //     },
            //     // Add more batches here...
            // };

            // Generate the report
            generateReportButton.addEventListener("click", () => {
                const batchID = batchIDSelect.value;

                if (!batchID) {
                    alert("Please select a batch!");
                    return;
                }


                fetch(`/api/batches/${batchID}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data) {
                            console.log("data is: "+ data.body);
                            generateBatchDetails(data);
                            //generateCharts(data);
                        } else {
                            alert("No data found for batch " + batchID);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert("An error occurred while fetching the batch details.");
                    });
            });
            //
            //     const data = batchData[batchID];
            //     generateBatchDetails(data);
            //     generateCharts(data);
            // });

            // Populate the batch details table
            function generateBatchDetails(data) {
                let total = data.produced;
                let totalNum = Number(total);
                let defect = data.defectiveProduce;
                let defectNum = Number(defect);


                let acceptable = totalNum - defectNum;
                batchDetailsBody.innerHTML = `
                    <tr>
                        <td>Batch ID</td>
                        <td>${data.id}</td>
                    </tr>
                    <tr>
                        <td>Product Type</td>
                        <td>${data.productType}</td>
                    </tr>
                    <tr>
                        <td>Total Products</td>
                        <td>${totalNum}</td>
                    </tr>
                    <tr>
                        <td>Acceptable Products</td>
                        <td>${acceptable}</td>
                    </tr>
                    <tr>
                        <td>Defective Products</td>
                        <td>${data.defectiveProduce}</td>
                    </tr>

                `;
            }

            // Generate temperature and humidity charts
            function generateCharts(data) {
                const tempCtx = document.getElementById("temperatureChart").getContext("2d");
                const humidityCtx = document.getElementById("humidityChart").getContext("2d");

                new Chart(tempCtx, {
                    type: "line",
                    data: {
                        labels: data.temperatureLogs.map((_, i) => `T${i + 1}`),
                        datasets: [{
                            label: "Temperature (°C)",
                            data: data.temperatureLogs,
                            borderColor: "rgba(255, 99, 132, 1)",
                            borderWidth: 2,
                            fill: false,
                        }],
                    },
                });

                new Chart(humidityCtx, {
                    type: "line",
                    data: {
                        labels: data.humidityLogs.map((_, i) => `T${i + 1}`),
                        datasets: [{
                            label: "Humidity (%)",
                            data: data.humidityLogs,
                            borderColor: "rgba(54, 162, 235, 1)",
                            borderWidth: 2,
                            fill: false,
                        }],
                    },
                });
            }
        });
    </script>
</body>
</html>

<script>
    document.getElementById("downloadPDF").addEventListener("click", () => {
    const batchID = document.getElementById("batchID").value; // Get selected batch ID

    if (!batchID) {
        alert("Please select a valid batch to download the report!"); // Validation for batch selection
        return;
    }

    // Redirect to the backend route for downloading the PDF
    window.open(`/report/pdf/${batchID}`, "_blank"); // Open the generated PDF in a new tab
});
</script>

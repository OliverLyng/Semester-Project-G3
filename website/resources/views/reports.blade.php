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
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const generateReportButton = document.getElementById("generateReport");
        const batchIDSelect = document.getElementById("batchID");
        const batchDetailsBody = document.getElementById("batchDetailsBody");


        // Generate the report
        generateReportButton.addEventListener("click", () => {
            const batchID = batchIDSelect.value;

            if (!batchID) {
                alert("Please select a valid batch!");
                return;
            }


            fetch(`/api/batches/${batchID}`)
                .then(response => response.json())
                .then(data => {
                    if (data) {
                        console.log("data is: " + data.body);
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


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Batch Report</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1, h2 {
            color: #2C3E50;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <!-- Header Section -->
    <header style="text-align: center; margin-bottom: 20px;">
        <img src="{{ public_path('images/logo.png') }}" alt="Logo" style="width: 100px;">
        <h1>Batch Report</h1>
    </header>

    <h2>Batch Number: {{ $batch['id'] }}</h2>

    <table>
        <tr>
            <th>Field</th>
            <th>Value</th>
        </tr>
        <tr>
            <td>Product Type</td>
            <td>{{ $batch['productType'] }}</td>
        </tr>
        <tr>
            <td>Total Products</td>
            <td>{{ $batch['produced'] }}</td>
        </tr>
        <tr>
            <td>Defective Products</td>
            <td>{{ $batch['defectiveProduce'] }}</td>
        </tr>
        <tr>
            <td>Date</td>
            <td>{{ $batch['created_at'] }}</td>
        </tr>
    </table>
</body>
</html>


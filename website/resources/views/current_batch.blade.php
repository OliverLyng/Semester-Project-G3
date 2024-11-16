<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Current Batch - Home Brew Hub</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
</head>
<body>
    <!-- Include Header and Sidebar (same as in home.blade.php) -->
    @include('partials.header')
    @include('partials.sidebar')

    <!-- Main Content -->
    <div class="content">
        <h2>Current Batch</h2>
        <p>This page shows the current brewing batch details.</p>
    </div>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory - Home Brew Hub</title>
    <link rel="stylesheet" href="{{ asset('style.css') }}">
    <link rel="stylesheet" href="{{ asset('inventory.css') }}">
</head>
<body>
    <!-- Include Header and Sidebar -->
    @include('partials.header')
    @include('partials.sidebar')

    <!-- Main Content -->
    <div class="content">
        <h2>Inventory</h2>
        <p>Manage your brewing inventory and track ingredient levels in real time.</p>

        <!-- Inventory Table Section -->
        <div class="inventory-section">
            <h3>Ingredient Stock Levels</h3>
            <table class="inventory-table">
                <thead>
                    <tr>
                        <th>Ingredient</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    @foreach ($items as $item)
                        <tr data-stock="{{ $item->quantity > 10 ? 'ok' : 'low' }}">
                            <td>
                                <div class="ingredient-container">
                                    <img src="{{ asset('Images/' . strtolower($item->name) . '-icon.png') }}" 
                                         alt="{{ $item->name }} Icon" class="ingredient-icon">
                                    <span>{{ $item->name }}</span>
                                </div>
                            </td>
                            <td>{{ $item->quantity }} kg</td>
                            <td class="{{ $item->quantity > 10 ? '' : 'alert-cell' }}">
                                {{ $item->quantity > 10 ? 'In Stock' : 'Low Stock' }}
                                @if ($item->quantity <= 10)
                                    <span class="alert-icon">&#9888;</span>
                                @endif
                            </td>
                            <td><button class="reorder-button">Reorder</button></td>
                        </tr>
                    @endforeach
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
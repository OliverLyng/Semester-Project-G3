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
                    @foreach($showData as $show)
                    <tr data-stock="ok">
                        <td>
                            <div class="ingredient-container">
                                <img src="{{ asset('Images/hops-icon.png') }}" alt="Hops Icon" class="ingredient-icon"> 
                                <span>Hops</span>
                            </div>
                        </td>
                        <td>{{ucfirst($show)}}</td>
                        <td><button class="reorder-button">Reorder</button></td>
                    </tr>
                    <tr data-stock="low">
                        <td>
                            <div class="ingredient-container">
                                <img src="{{ asset('Images/malt-icon.png') }}" alt="Malt Icon" class="ingredient-icon"> 
                                <span>Malt</span>
                            </div>
                        </td>
                        <td>5 kg</td>
                        <td class="alert-cell"><span class="alert-icon">&#9888;</span> Low Stock</td>
                        <td><button class="reorder-button">Reorder</button></td>
                    </tr>
                    <tr data-stock="ok">
                        <td>
                            <div class="ingredient-container">
                                <img src="{{ asset('Images/yeast-icon.png') }}" alt="Yeast Icon" class="ingredient-icon"> 
                                <span>Yeast</span>
                            </div>
                        </td>
                        <td>20 kg</td>
                        <td>In Stock</td>
                        <td><button class="reorder-button">Reorder</button></td>
                    </tr>
                    <tr data-stock="ok">
                        <td>
                            <div class="ingredient-container">
                                <img src="{{ asset('Images/barley-icon.png') }}" alt="Barley Icon" class="ingredient-icon"> 
                                <span>Barley</span>
                            </div>
                        </td>
                        <td>60 kg</td>
                        <td>In Stock</td>
                        <td><button class="reorder-button">Reorder</button></td>
                    </tr>
                    <tr data-stock="low">
                        <td>
                            <div class="ingredient-container">
                                <img src="{{ asset('Images/wheat-icon.png') }}" alt="Wheat Icon" class="ingredient-icon"> 
                                <span>Wheat</span>
                            </div>
                        </td>
                        <td>3 kg</td>
                        <td class="alert-cell"><span class="alert-icon">&#9888;</span> Low Stock</td>
                        <td><button class="reorder-button">Reorder</button></td>
                    </tr>
                    @endforeach
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

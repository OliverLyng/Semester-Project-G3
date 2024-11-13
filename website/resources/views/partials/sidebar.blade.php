<div class="sidebar">
    <div class="menu-item {{ request()->is('/') ? 'active' : '' }}">
        <img src="{{ asset('Images/dashboard-icon.png') }}" alt="Dashboard Icon" class="icon"> 
        <a href="{{ route('dashboard') }}">Dashboard</a>
    </div>
    <div class="menu-item {{ request()->is('scheduling') ? 'active' : '' }}">
        <img src="{{ asset('Images/scheduling-icon.png') }}" alt="Scheduling Icon" class="icon"> 
        <a href="{{ route('scheduling') }}">Scheduling</a>
    </div>
    <div class="menu-item {{ request()->is('current_batch') ? 'active' : '' }}">
        <img src="{{ asset('Images/current-batch-icon.png') }}" alt="Current Batch Icon" class="icon"> 
        <a href="{{ route('current_batch') }}">Current Batch</a>
    </div>
    <div class="menu-item {{ request()->is('inventory') ? 'active' : '' }}">
        <img src="{{ asset('Images/inventory-icon.png') }}" alt="Inventory Icon" class="icon"> 
        <a href="{{ route('inventory') }}">Inventory</a>
    </div>
    <div class="menu-item {{ request()->is('history') ? 'active' : '' }}">
        <img src="{{ asset('Images/history-icon.png') }}" alt="History Icon" class="icon"> 
        <a href="{{ route('history') }}">History</a>
    </div>
    <div class="menu-item {{ request()->is('reports') ? 'active' : '' }}">
        <img src="{{ asset('Images/report-icon.png') }}" alt="Reports Icon" class="icon"> 
        <a href="{{ route('reports') }}">Reports</a>
    </div>
</div>

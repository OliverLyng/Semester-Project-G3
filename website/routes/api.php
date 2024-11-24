<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\InventoryController;
use Illuminate\Http\Request;
use App\Models\Inventory;


Route::post("/inventory", [InventoryController::class, 'store']);

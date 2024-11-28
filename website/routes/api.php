<?php

use App\Http\Controllers\SettingsController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\InventoryController;
use Illuminate\Http\Request;
use App\Models\Inventory;


Route::post('/inventory', [InventoryController::class, 'store']);
Route::put('/update-inventory',[InventoryController::class,'update']);
Route::post('/settings',[SettingsController::class,'update']);

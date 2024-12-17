<?php

use App\Http\Controllers\SettingsController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\InventoryController;
use Illuminate\Http\Request;
use App\Models\Inventory;
use App\Models\ReportModel;
use App\Http\Controllers\ReportController;




Route::post('/inventory', [InventoryController::class, 'store']);
Route::put('/update-inventory',[InventoryController::class,'update']);
Route::post('/settings',[SettingsController::class,'update']);
Route::post('/batch-report',[ReportController::class,'insertData']);

<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\InventoryController;

Route::post('/inventory', [InventoryController::class, 'store']);


<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\InventoryController;
use Illuminate\Support\Facades\Cache;




Route::get('/', function () {
    return view('home');
})->name('dashboard'); // Home route as Dashboard

Route::view('/scheduling', 'scheduling')->name('scheduling');
Route::view('/current_batch', 'current_batch')->name('current_batch');
Route::view('/history', 'history')->name('history');
Route::view('/reports', 'reports')->name('reports');

//API
Route::post('/start-brewing', [BrewingController::class, 'startBrewing']);

Route::get('/inventory',[InventoryController::class,'show'])->name('inventory');


use App\Http\Controllers\SettingsController;

Route::view('/settings', 'settings')->name('settings');
Route::post('/validate-settings', [SettingsController::class, 'validateSettings']);

<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('home');
})->name('dashboard'); // Home route as Dashboard

Route::view('/scheduling', 'scheduling')->name('scheduling');
Route::view('/current_batch', 'current_batch')->name('current_batch');
Route::view('/inventory', 'inventory')->name('inventory');
Route::view('/history', 'history')->name('history');
Route::view('/reports', 'reports')->name('reports');


//API
Route::post('/start-brewing', [BrewingController::class, 'startBrewing']);
Route::post('/inventory', [InventoryClass::class,'store']);



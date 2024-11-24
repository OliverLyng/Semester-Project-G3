<?php

use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('home');
})->name('dashboard'); // Home route as Dashboard

Route::view('/scheduling', 'scheduling')->name('scheduling');
Route::view('/current_batch', 'current_batch')->name('current_batch');
Route::view('/history', 'history')->name('history');
Route::view('/reports', 'reports')->name('reports');


use App\Http\Controllers\InventoryController;
Route::get('/inventory', [InventoryController::class, 'index'])->name('inventory');

Route::get('/api/inventory', function () {
    return response()->json(DB::table('inventory')->get());
});
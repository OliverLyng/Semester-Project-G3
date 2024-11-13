<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Http\Request;
use App\Http\Controllers\CommandController;
use Illuminate\Auth\Middleware\Authenticate;

Route::post('/button-pressed',[CommandController::class, 'buttonPress']);

Route::get('/status',function(){
    return response()->json(['status' => 'API is working']);
});
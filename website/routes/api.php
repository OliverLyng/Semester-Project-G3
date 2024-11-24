<?php

use Illuminate\Support\Facades\Route;
use Illuminate\Support\Facades\Http;

Route::post('/start-brewing', function () {
    $response = Http::post('http://localhost:8080/api/start');
    return $response->json();
});

Route::post('/pause-brewing', function () {
    $response = Http::post('http://localhost:8080/api/pause');
    return $response->json();
});

Route::post('/stop-brewing', function () {
    $response = Http::post('http://localhost:8080/api/stop');
    return $response->json();
});

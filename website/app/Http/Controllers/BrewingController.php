<?php

// app/Http/Controllers/BrewingController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

class BrewingController extends Controller
{
    public function startBrewing()
    {
        // URL of the Spring Boot API endpoint
        $springBootUrl = 'http://localhost:8080/api/start-brewing';

        // Make a POST request to the Spring Boot API
        $response = Http::post($springBootUrl);

        // Check if the request was successful
        if ($response->successful()) {
            return response()->json([
                'status' => 'success',
                'message' => $response->body(),
            ]);
        } else {
            return response()->json([
                'status' => 'error',
                'message' => 'Failed to start the brewing process.',
            ], 500);
        }
    }
}

<?php

// app/Http/Controllers/BrewingController.php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;

class BrewingController extends Controller
{
    public function startBrewing()
    {
        return response()->json(['message' => 'Brewing process started successfully!']);
    }

    public function pauseBrewing()
    {
        return response()->json(['message' => 'Brewing process paused successfully!']);
    }

    public function stopBrewing()
    {
        return response()->json(['message' => 'Brewing process stopped successfully!']);
    }
}
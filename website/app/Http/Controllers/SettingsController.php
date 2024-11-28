<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;


class SettingsController extends Controller
{
    public function update(Request $request)
    {
        return response()->json(['message' => 'Brewing process started successfully!']);
    }
}

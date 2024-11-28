<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;


class SettingsController extends Controller
{
    public function update()
    {
        return response()->json(['message' => 'Settings processed successfully!']);
    }
}

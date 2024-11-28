<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class SettingsController extends Controller
{
    public function validateSettings(Request $request)
    {
        $data = $request->validate([
            'beerType' => 'required|string',
            'speed' => 'required|integer',
            'amount' => 'required|integer',
        ]);

        $validRanges = [
            'Pilsner' => ['min' => 0, 'max' => 600],
            'Wheat' => ['min' => 0, 'max' => 300],
            'IPA' => ['min' => 0, 'max' => 150],
            'Stout' => ['min' => 0, 'max' => 200],
            'Ale' => ['min' => 0, 'max' => 100],
            'AlcoholFree' => ['min' => 0, 'max' => 125],
        ];

        $beerType = $data['beerType'];
        $speed = $data['speed'];
        $range = $validRanges[$beerType];

        if ($speed < $range['min'] || $speed > $range['max']) {
            return response()->json(['message' => "Speed must be between {$range['min']} and {$range['max']} for $beerType."], 400);
        }

        return response()->json(['message' => 'Settings are valid.']);
    }
}

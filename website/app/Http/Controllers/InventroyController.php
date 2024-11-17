<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class InventroyController extends Controller
{
    public function store(Request $request)
    {
        $validateData = $request->validate
        ([
            'barley' => 'required|float',
            'hops' => 'required|float',
            'malt' => 'required|float',
            'wheat' => 'required|float',
            'yeast' => 'required|float',
        ]);
        return response()->json($response ,201);
    }
}

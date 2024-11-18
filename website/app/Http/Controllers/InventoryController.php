<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class InventoryController extends Controller
{
    public function store(Request $request)
    {
        $validateData = $request->validate
        ([
            'wheat' => 'required|numeric'
        ]);

        $response = [
            'message' => 'Data saved sucessfully',
            'data' => $validateData,
        ];

        return response()->json($response ,200);
    }
}

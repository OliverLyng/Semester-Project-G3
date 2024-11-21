<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class InventoryController extends Controller
{
    public function store(Request $request)
    {
        // Retrieve data from the request
        $data = $request->all();

        // Respond with the data for testing
        return response()->json([
            'message' => 'Inventory data received successfully!',
            'data' => $data,
        ]);
    }
}
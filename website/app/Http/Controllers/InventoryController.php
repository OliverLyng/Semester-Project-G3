<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class InventoryController extends Controller
{
    public function store(Request $request)
    {
        $validated = $request->validate([
            'name'=>'required|string|max:255',
            'quantity'=>'required|float',
        ]);
        $inventory = Inventory::create($validated);
        
        return response()->json([
            'message'=>'Item created successfully!',
            'data'=>$inventory
        ],201);
    }

    public function show(){
        $inventory = Inventory::all();

        return response()->json($inventory,200);
    }
}
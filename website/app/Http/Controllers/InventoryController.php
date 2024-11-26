<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Inventory;

class InventoryController extends Controller
{
    public function store(Request $request)
    {
        $item = Inventory::create($request->all());
        
        return response()->json($item);
    }

    public function update(Request $request)
    {
        $item = Inventory::find($request->id);
        $item->name=$request->name;
        $item->quantity=$request->quantity;
        $result=$item->save();
        return ["result"=>"Data has been saved"];
    }

    public function show(){

        $inventories = Inventory::all();
        return view('inventory',['items'=>$inventories]);
    }
}
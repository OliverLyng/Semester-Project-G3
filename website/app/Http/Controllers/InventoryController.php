<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class InventoryController extends Controller
{
    public function index()
    {
        $items = DB::table('inventory')->get();
        return view('inventory', ['items' => $items]);
    }
}

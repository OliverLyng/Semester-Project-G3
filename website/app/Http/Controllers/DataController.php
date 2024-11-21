<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Http;

class DataController extends Controller
{
    public function show(){
        $showData = Http::get('http://localhost:8000/api/inventory')['message'];
        return view('inventory',compact('showData'));
    }
}

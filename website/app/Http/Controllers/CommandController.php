<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class CommandController extends Controller
{
    public function buttonPress(Request $request)
    {
        $request -> validate([
            'buttonPressed' => 'required|boolean',
        ]);


        return response()->json([
            'message' => 'Button pressed recieved',
            'data' => $request->all()
        ],200);
    }
}
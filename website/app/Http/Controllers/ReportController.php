<?php

namespace App\Http\Controllers;

use Barryvdh\DomPDF\Facade\Pdf;

// Import the DomPDF facade
use Illuminate\Http\Request;
use App\Models\ReportModel;


class ReportController extends Controller
{
    public function getBatchReport($id)
    {
        $batch = ReportModel::find($id);

        if (!$batch) {
            return response()->json(['message' => 'Batch not found'], 404);
        }

        // Optionally, you might want to transform or add additional data here
        return response()->json($batch);
    }

    public function showReports()
    {
        $batches = ReportModel::all(); // Fetch all batches from the database
        return view('reports', compact('batches')); // Pass the batches to the view
    }


    public function generateBatchReport($id)
    {
        $batch = ReportModel::find($id);
        if (!$batch) {
            return redirect('reports')->with('error', 'Batch not found.');
        }
        $timestamp = $batch->created_at->format('F d, Y h: i A');
    }

    // Generate the PDF
    public
    function insertData(Request $request)
    {
        $validatedData = $request->validate([
            'produced' => 'required|numeric',
            'defectiveProduce' => 'required|numeric',
            'productType' => 'required|string|max:255'
        ]);
        $batch = ReportModel::create($request->all());
        Log::info('Inserted Data TJEK TJEK: ', $batch->toArray());
        return response()->json($batch);
    }
}



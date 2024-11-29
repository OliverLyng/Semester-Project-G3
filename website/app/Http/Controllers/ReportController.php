<?php

namespace App\Http\Controllers;

use Barryvdh\DomPDF\Facade\Pdf; // Import the DomPDF facade
use Illuminate\Http\Request;
use App\Models\ReportModel;


class ReportController extends Controller
{
    public function generateBatchReport($id)
    {
        $batch = ReportModel::find($id);

        $timestamp = $batch->created_at->format('F d, Y h: i A');

        // Generate the PDF
        $pdf = Pdf::loadView('pdf.batch-report', compact('batch','timestamp'));

        // Return the PDF for download
        return $pdf->download('batch_report_' . $id . '.pdf');
    }

    public function insertData(Request $request)
    {
        $batch = ReportModel::create($request->all());
    
        return response()->json($batch);
    }

}

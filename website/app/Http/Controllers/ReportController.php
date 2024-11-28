<?php

namespace App\Http\Controllers;

use Barryvdh\DomPDF\Facade\Pdf; // Import the DomPDF facade
use Illuminate\Http\Request;

class ReportController extends Controller
{
    public function generateBatchReport($id)
    {
        // Example data (replace this with actual database query)
        $batch = [
            'id' => $id,
            'productType' => 'Pilsner',
            'totalProducts' => 1200,
            'defective' => 50,
            'acceptable' => 1150,
            'timeUsed' => '10 hours',
        ];

        // Generate the PDF
        $pdf = Pdf::loadView('pdf.batch-report', compact('batch'));

        // Return the PDF for download
        return $pdf->download('batch_report_' . $id . '.pdf');
    }
}

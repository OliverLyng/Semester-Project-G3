<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
Use Illuminate\Database\Eloquent\Factories\HasFactory;

class ReportModel extends Model
{
    use HasFactory;
    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */

    protected $fillable = [
        'produced',
        'batchID',
        'defectiveProduce',
        'productType'
    ];
}

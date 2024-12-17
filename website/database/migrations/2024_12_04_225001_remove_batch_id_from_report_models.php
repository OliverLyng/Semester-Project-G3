<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::table('report_models', function (Blueprint $table) {
            $table->dropColumn('batchID');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('report_models', function (Blueprint $table) {
            $table->string('batchID')->after('produced');  // Adjust 'after' if needed based on your original table structure
        });
    }
};

<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class InventorySeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::table('inventory')->insert([
            ['name' => 'Hops', 'quantity' => 69],
            ['name' => 'Malt', 'quantity' => 49],
            ['name' => 'Yeast', 'quantity' => 45],
            ['name' => 'Barley', 'quantity' => 45],
            ['name' => 'Wheat', 'quantity' => 5], 
        ]);
    }
}

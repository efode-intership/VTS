<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Vehicle;


class VehicleController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        // $this->middleware('auth');
    }

    /**
     * Get vehicle information.
     *
     * @var id vehicle's id.
     * @return \Illuminate\Http\Response
     */
    public function getVehicle($id)
    {
      return response()->json(array(
                'error' => false,
                'content' => Vehicle::all(),
                'status_code' => 200
            ));
    }


}

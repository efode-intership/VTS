<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Vehicle;

use App\ScheduleActive;

use DB;


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

    /**
    * Get nearby vehicle.
    */
    public function getNearbyVehicle($locationLat, $locationLong, $distance)
    {
      // $locationLat = 10;
      // $locationLong = 10;
      // $distance = 100000;
      // 6371 is the earth radius in Kilometer
      $results = DB::select( DB::raw("SELECT
                                        lastLocation.*, (
                                          6371 * acos (
                                            cos ( radians( :locationLat) )
                                            * cos( radians( lastLocation.location_lat ) )
                                            * cos( radians( lastLocation.location_long ) - radians( :locationLong) )
                                            + sin ( radians( :locationLatCopy) )
                                            * sin( radians( lastLocation.location_lat ) )
                                          )
                                        ) AS distance, users.name as driver_name, users.phone as driver_phone
                                      FROM (SELECT schedule_id, location_lat, location_long, MAX(created_at) AS maxDate
                                            FROM schedule_active
                                            GROUP BY schedule_id) lastLocation,  schedule , users
                                      WHERE schedule.schedule_id = lastLocation.schedule_id
                                      AND users.id = schedule.driver_id
                                      HAVING distance < :distance
                                      ORDER BY distance"),
                                      array('locationLat' => $locationLat,
                                            'locationLong' => $locationLong,
                                            'locationLatCopy' => $locationLat,
                                            'distance' => $distance));
      return response()->json(array(
                'error' => false,
                'content' => $results,
                'status_code' => 200
            ));
    }

}

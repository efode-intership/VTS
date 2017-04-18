<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use DB;
use App\Warning;


class WarningController extends Controller
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
     * Add new warning.
     * @var userId
     * @var warningTypeId
     * @var locationLat
     * @var locationLong
     * @var description
     * @var startTime
     * @var endTime
     * @return \Illuminate\Http\Response
     */
    public function createWarning(Request $request)
    {
      $warning = new Warning();
      // $warning->warning_id = 1;
      $warning->driver_id = $request->userId;
      $warning->warning_type_id = $request->warningTypeId;
      $warning->location_lat = $request->locationLat;
      $warning->location_long = $request->locationLong;
      $warning->description = $request->description;
      $warning->start_time = $request->startTime;
      $warning->end_time = $request->endTime;
      try {
        $warning->save();
      } catch (Exception $e) {
        // TODO implement logger
          echo 'Caught exception: ',  $e->getMessage(), "\n";
          return response()->json(array(
                    'error' => true,
                    'content' => 'Failed.',
                    'status_code' => 404
                ));
      }
      return response()->json(array(
                'error' => false,
                'content' => $warning,
                'status_code' => 200
            ));

    }

    /**
     * Find nearby location by distance.
     * @var locationLat
     * @var locationLong
     * @var distance
     * @return \Illuminate\Http\Response
     */
    public function getWarningByRadius($locationLat, $locationLong, $distance)
    {
      // $locationLat = $request->locationLat;
      // $locationLong = $request->locationLong;
      // $distance = $request->distance;
      // 6371 is the radius of the earth in Kilometer
      $results = DB::select( DB::raw("SELECT
                                        *, (
                                          6371 * acos (
                                            cos ( radians( :locationLat) )
                                            * cos( radians( location_lat ) )
                                            * cos( radians( location_long ) - radians( :locationLong) )
                                            + sin ( radians( :locationLatCopy) )
                                            * sin( radians( location_lat ) )
                                          )
                                        ) AS distance
                                      FROM warning
                                      WHERE end_time <
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
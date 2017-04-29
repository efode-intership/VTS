<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Schedule;
use App\ScheduleActive;

use DB;


class ScheduleController extends Controller
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
     * Get schedule list by User id
     * @var userId user's id
     * @return \Illuminate\Http\Response
     */
    public function getScheduleByUserId($userId)
    {
      $result = DB::table('schedule')
            ->join('schedule_status', 'schedule.schedule_status_type_id', '=', 'schedule_status.schedule_status_type_id')
            ->select('schedule.*', 'schedule_status.name as schedule_status_name', 'schedule_status.description as schedule_status_description')
            ->where('schedule.driver_id',$userId)
            ->orderBy('schedule.intend_start_time')
            ->get();
      return response()->json(array(
                'error' => false,
                'content' => $result,
                'status_code' => 200
            ));
    }

    public function getIncomingScheduleSixtyMinutes() {
      return response()->json(array(
                'error' => false,
                'content' => Schedule::where('schedule_status_type_id','1')->where('schedule_status_type_id','3')->orderBy('intend_start_time')->first(),
                'status_code' => 200
            ));
    }

    /**
     * Get schedule list by Schedule id
     * @var id schedule's id
     * @return \Illuminate\Http\Response
     */
    public function getScheduleById($id)
    {
      return response()->json(array(
                'error' => false,
                'content' => Schedule::where('schedule_id',$id)->get(),
                'status_code' => 200
            ));
    }

    /**
     * Get incomming schedule by User id
     * @var userId user's id
     * @return \Illuminate\Http\Response
     */
    public function getIncomingSchedule($userId)
    {
      $results = DB::select( DB::raw("SELECT
                                        *
                                      FROM schedule
                                      WHERE driver_id = :userId
                                      AND (schedule_status_type_id = 1 OR schedule_status_type_id = 3)
                                      ORDER BY intend_start_time LIMIT 1"),
                                      array('userId' => $userId));
      $result = null;
      if (sizeof($results) > 0) {
        $result = $results[0];
      }
      return response()->json(array(
                'error' => false,
                'content' => $result ,
                'status_code' => 200
            ));
    }

    /**
     * Start schedule.
     * @var id schedule's id
     * @return \Illuminate\Http\Response
     */
    public function startSchedule(Request $request)
    {
      $scheduleId = $request->scheduleId;
      $deviceId = $request->deviceId;
      $schedule = Schedule::where('schedule_id',$scheduleId)->first();
      if ($schedule != null) {
        $schedule->device_id = $deviceId;
        $schedule->schedule_status_type_id = '3'; // TODO 'In progress' status
        date_default_timezone_set("Asia/Bangkok");
        $date = date('Y-m-d G:i:s');
        $schedule->real_start_time = $date;
        $schedule->save();
      } else {
        return response()->json(array(
                  'error' => true,
                  'content' => 'Failed',
                  'status_code' => 404
              ));
      }
      return response()->json(array(
                'error' => false,
                'content' => 'Succeeded',
                'status_code' => 200
            ));
    }

    /**
     * Complete schedule.
     * @var id schedule's id
     * @return \Illuminate\Http\Response
     */
    public function completeSchedule(Request $request)
    {
      $scheduleId = $request->scheduleId;
      $deviceId = $request->deviceId;
      $schedule = Schedule::where('schedule_id',$scheduleId)->first();
      if ($schedule != null) {
        $schedule->device_id = $deviceId;
        $schedule->schedule_status_type_id = '2'; // TODO 'Completed' status
        date_default_timezone_set("Asia/Bangkok");
        $date = date('Y-m-d G:i:s');
        $schedule->real_end_time = $date;
        $schedule->save();
      } else {
        return response()->json(array(
                  'error' => true,
                  'content' => 'Failed',
                  'status_code' => 404
              ));
      }
      return response()->json(array(
                'error' => false,
                'content' => 'Succeeded',
                'status_code' => 200
            ));
    }

    /**
     * User cancel started schedule.
     * @var id schedule's id
     * @return \Illuminate\Http\Response
     */
    public function cancelSchedule(Request $request)
    {
      $scheduleId = $request->scheduleId;
      $deviceId = $request->deviceId;
      $schedule = Schedule::where('schedule_id',$scheduleId)->first();
      if ($schedule != null) {
        $schedule->device_id = $deviceId;
        $schedule->schedule_status_type_id = '1'; // TODO 'Not started' status
        $schedule->real_start_time = null;
        $schedule->real_end_time = null;
        $schedule->save();
      } else {
        return response()->json(array(
                  'error' => true,
                  'content' => 'Failed',
                  'status_code' => 404
              ));
      }
      return response()->json(array(
                'error' => false,
                'content' => 'Succeeded',
                'status_code' => 200
            ));
    }

    /**
    * Insert schedule active data.
    * @var scheduleId
    * @var locationLat
    * @var locationLong
    * @var speed
    * @var deviceId
    * @return json data.
    */
    public function insertScheduleActive(Request $request)
    {
      $scheduleActive = new ScheduleActive();
      $scheduleActive->schedule_id = $request->scheduleId;
      $scheduleActive->location_lat = $request->locationLat;
      $scheduleActive->location_long = $request->locationLong;
      $scheduleActive->speed = $request->speed;
      $scheduleActive->device_id = $request->deviceId;
      try {
        $scheduleActive->save();
      } catch (Exception $e) {
          // TODO implement logger
          echo 'Caught exception: ',  $e->getMessage(), "\n";
          return response()->json(array(
                    'error' => true,
                    'content' => 'Failed',
                    'status_code' => 404
                ));
      }
      return response()->json(array(
                'error' => false,
                'content' => 'Succeeded',
                'status_code' => 200
            ));
    }



}

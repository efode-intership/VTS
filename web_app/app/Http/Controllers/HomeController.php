<?php

namespace App\Http\Controllers;
use DB;
use Illuminate\Http\Request;
use App\Schedule;

class HomeController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        // return view('home');
        // $newToken = new Token();
        // $newToken->device_id = '123456789';
        // $newToken->user_id = 6;
        // $newToken->status = 1; // active status
        // $newToken->save();
        $scheduleId = '1';
        $deviceId = '1';
        $schedule = Schedule::where('schedule_id',$scheduleId)->first();
        if ($schedule != null) {
          $schedule->device_id = $deviceId;
          $schedule->schedule_status_type_id = '3'; // TODO 'In progress' status
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
}

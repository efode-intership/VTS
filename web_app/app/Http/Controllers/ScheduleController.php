<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Schedule;


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
     * API: Get schedule list by User id
     * @var userId user's id
     * @return \Illuminate\Http\Response
     */
    public function getScheduleByUserId($userId)
    {
      return response()->json(array(
                'error' => false,
                'content' => Schedule::where('driver_id',$userId)->get(),
                'status_code' => 200
            ));
    }

    /**
     * API: Get schedule list by Schedule id
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



}

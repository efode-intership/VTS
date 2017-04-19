<?php

namespace App\Http\Controllers;
use DB;
use Illuminate\Http\Request;
use App\Schedule;
use DateTime;
use DateInterval;

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
        date_default_timezone_set("Asia/Bangkok");
        $date = new DateTime();
        $minuteToAdd = 5;
        $date->add(new DateInterval('PT' . $minuteToAdd . 'M'));
        $end_time = date_format($date, 'Y-m-d G:i:s');
        return  $end_time;
    }
}

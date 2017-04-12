<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Auth;

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
    public function index(Request $request)
    {
      if (Auth::attempt(['email' => $email, 'password' => $password])) {
            // Authentication passed...
            return response()->json(array(
                      'error' => false,
                      'name' => 'tri',
                      'status_code' => 200
                  ));
        }
      else {
        return response()->json(array(
                'error' => true,
                'name' => 'tri',
                'status_code' => 200
            ));
      }
        return "hihi";
    }


}

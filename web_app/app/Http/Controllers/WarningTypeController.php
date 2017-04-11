<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\WarningType;


class WarningTypeController extends Controller
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
     * Get warning type list.
     *
     * @return \Illuminate\Http\Response
     */
    public function getWarningTypeList()
    {
      return response()->json(array(
                'error' => false,
                'content' => WarningType::all(),
                'status_code' => 200
            ));
    }


}

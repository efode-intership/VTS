<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Token;


class TokenController extends Controller
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
     * Mapping token - user.
     * @var deviceId
     * @var userId
     * @return \Illuminate\Http\Response
     */
    public function saveToken(Request $request)
    {
      $deviceId = $request->deviceId;
      $token = Token::where("device_id", $deviceId)->first();
      if ($token == null) {
        $newToken = new Token;
        $newToken->device_id = $deviceId;
        $newToken->user_id = $request->userId;
        $newToken->status = 1; // active status
        $newToken->save();
      } else {
        $token->user_id = $request->userId;
        $token->save();
      }
      return response()->json(array(
                'error' => false,
                'content' => "Succeeded",
                'status_code' => 200
            ));
    }


}

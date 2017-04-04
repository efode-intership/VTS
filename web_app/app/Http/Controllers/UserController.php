<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Auth;

use App\User;

class UserController extends Controller
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
     * API: Validate user.
     *
     * @return \Illuminate\Http\Response
     */
    public function validateUser(Request $request)
    {
      $email = $request->email;
    	$password = $request->password;
      if (Auth::attempt(['email' => $email, 'password' => $password])) {
            // Authentication passed...
            return response()->json(array(
                      'error' => false,
                      'content' => Auth::user(),
                      'status_code' => 200
                  ));
        }
      else {
        return response()->json(array(
                'error' => true,
                'content' => null,
                'status_code' => 404
            ));
      }
    }

    /**
     * Validate user.
     *
     * @return \Illuminate\Http\Response
     */
    public function getValidateUser($email, $password)
    {
      if (Auth::attempt(['email' => $email, 'password' => $password])) {
            // Authentication passed...
            return response()->json(array(
                      'error' => false,
                      'content' => $email,
                      'status_code' => 200
                  ));
        }
      else {
        return response()->json(array(
                'error' => true,
                'content' => null,
                'status_code' => 404
            ));
      }
    }

    /**
     * Get User info by user id.
     * @var id user id
     * @return \Illuminate\Http\Response
     */
    public function getUserById($id)
    {
      return response()->json(array(
                'error' => false,
                'content' => User::find($id),
                'status_code' => 200
            ));
    }

    /**
     * Change user password.
     * @var email
     * @var currentPassword
     * @var newPassword
     * @return json response
     */
    public function changePassword(Request $request)
    {
      $email = $request->email;
      $password = $request->currentPassword;
      $newPassword = $request->newPassword;
      if (Auth::attempt(['email' => $email, 'password' => $password])) {
        $user = Auth::user();
        $user->password = bcrypt($newPassword);
        $user->save();
        return response()->json(array(
                  'error' => false,
                  'content' => 'Succeeded',
                  'status_code' => 200
              ));
      }
      else {
        return response()->json(array(
                'error' => true,
                'content' => 'Failed',
                'status_code' => 404
            ));
      }
    }


}

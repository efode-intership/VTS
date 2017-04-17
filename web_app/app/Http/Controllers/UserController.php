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
     * Validate user.
     *
     * @return \Illuminate\Http\Response
     */
    public function validateUser(Request $request)
    {
      $email = $request->email;
    	$password = $request->password;
      if (Auth::attempt(['email' => $email, 'password' => $password])) {
            $user = Auth::user();
            date_default_timezone_set("Asia/Bangkok");
            $date = date('Y-m-d G:i:s');
            $user->last_login_time = $date;
            $user->save();
            // Authentication passed...
            return response()->json(array(
                      'error' => false,
                      'content' => $user,
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
      // TODO validate newPassword
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

    /**
     * Forgot password.
     * @var email user's email.
     * @return \Illuminate\Http\Response
     */
    public function forgotPassword(Request $request)
    {
      $email = $request->email;
      $user = User::where('email',$email)->first();
      if ($user == null) {
        return response()->json(array(
                  'error' => true,
                  'content' => "This email does not exist!",
                  'status_code' => 200
              ));
      }
      // TODO generate random code and send SMS to user's phonenumber
      $user->forgot_code = "12345";
      date_default_timezone_set("Asia/Bangkok");
      $date = date('Y-m-d G:i:s');
      // $date->setTimezone(new DateTimeZone('Asia/Ho_Chi_Minh'));
      // date_timezone_set($date, timezone_open('Asia/Bangkok'));
      $user->forgot_code_created = $date;
      $user->save();
      return response()->json(array(
                'error' => false,
                'content' => "SMS has been sent!",
                'status_code' => 200
            ));
    }

    /**
     * Check reset password code.
     * @var email user's email
     * @var confirmCode forgot password code.
     * @return \Illuminate\Http\Response
     */
    public function checkResetCode(Request $request)
    {
      $email = $request->email;
      $code = $request->confirmCode;
      $user = User::where('email',$email)->first();
      if ($user == null) {
        return response()->json(array(
                  'error' => true,
                  'content' => "Wrong",
                  'status_code' => 404
              ));
      }
      $forgotCode = $user->forgot_code;
      $forgotCodeTime = $user->forgot_code_created;
      // TODO check experied time of the forgotCode
      // date_default_timezone_set("Asia/Bangkok");
      // $date = date('Y-m-d G:i:s');
      if ($forgotCode == $code) {
        $user->reset_password_token = "randomToken"; // TODO generate randomToken
        $user->forgot_code = null;
        $user->forgot_code_created = null;
        $user->save();
        return response()->json(array(
                  'error' => false,
                  'content' => array('reset_password_token' => $user->reset_password_token),
                  'status_code' => 200
              ));
      } else {
        return response()->json(array(
                  'error' => true,
                  'content' => "Wrong",
                  'status_code' => 404
              ));
      }
    }

    /**
     * Set new password for user.
     * @var email user's email
     * @var newPassword
     * @var resetPasswordToken
     * @return \Illuminate\Http\Response
     */
    public function setNewPassword(Request $request)
    {
      $email = $request->email;
      $newPassword = $request->newPassword;
      $resetPasswordToken = $request->resetPasswordToken;
      $user = User::where('email',$email)->first();
      // check if user is exist or reset password token is correct
      if ($user == null || $user->reset_password_token != $resetPasswordToken) {
        return response()->json(array(
                  'error' => true,
                  'content' => "Failed",
                  'status_code' => 404
              ));
      }
      $user->reset_password_token = null;
      // TODO validate newPassword
      $user->password = bcrypt($newPassword);
      $user->save();
      return response()->json(array(
                'error' => false,
                'content' => "Succeeded!",
                'status_code' => 200
            ));
    }


}

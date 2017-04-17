
<?php
/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
})->middleware('auth.basic');

Route::get('/map', function () {
    return view('map');
});

Auth::routes();

Route::get('/home', 'HomeController@index')->middleware('cors');

Route::get('/home1' , 'ApiController@index');

// Sample api
Route::get('/api/v1/hello/{name?}', function($name = null) {
  return Response::json(array(
            'error' => false,
            'content' => array('name'=>$name == null ? "1" : $name),
            'status_code' => 200
        ));
});

// APIs version 1.0


// User group
/**
 * Check authentication API.
 * url: ../api/v1/validateUser
 * @var email
 * @var password
 * @return json data
 */
Route::post('/api/v1/user/validate', ['as' => 'validateUser', 'uses' => 'UserController@validateUser']);

/**
 * Get User info by user id
 * url: ../api/v1/user
 * @var userId
 * @return json data
 */
 Route::get('api/v1/user/view/{id}', 'UserController@getUserById');

 /**
  * Change user password
  * url: ../api/v1/user/changePassword
  * @var email
  * @var currentPassword
  * @var newPassword
  * @return json data
  */
  Route::post('api/v1/user/changePassword', 'UserController@changePassword');

  /**
   * Forgot password
   * url: ../api/v1/user/forgotPassword
   * @var email
   * @return json data
   */
   Route::post('api/v1/user/forgotPassword', 'UserController@forgotPassword');

   /**
    * Confirm forgot password code.
    * url: ../api/v1/user/checkResetCode
    * @var email
    * @var confirmCode
    * @return json data
    */
    Route::post('api/v1/user/checkResetCode', 'UserController@checkResetCode');

    /**
     * Set new password for user.
     * url: ../api/v1/user/newPassword
     * @var email
     * @var newPassword
     * @var resetPasswordToken
     * @return json data
     */
     Route::post('api/v1/user/newPassword', 'UserController@setNewPassword');


// Warning group
/**
 * Create warning.
 * url: ../api/v1/warning/create
 * @var userId
 * @var warningTypeId
 * @var locationLat
 * @var locationLong
 * @var description
 * @var startTime
 * @var endTime
 * @return json data
 */
 Route::post('api/v1/warning/create', 'WarningController@createWarning');

 /**
  * Get warning type list.
  * url: ../api/v1/warningTypes
  * @return json data
  */
  Route::get('api/v1/warningTypes', 'WarningTypeController@getWarningTypeList');

  /**
   * Find nearby location by distance.
   * @var locationLat
   * @var locationLong
   * @var distance
   * @return \Illuminate\Http\Response
   */
  Route::get('api/v1/warning/search/{locationLat}/{locationLong}/{distance}', 'WarningController@getWarningByRadius');












// Schedule group
/**
 * Get schedule list by Schedule id
 * url: ../api/v1/schedule
 * @var userId
 * @return json data
 */
 Route::get('api/v1/schedule/{id}', 'ScheduleController@getScheduleById');

/**
 * Get schedule list by User id
 * url: ../api/v1/schedule/user
 * @var userId
 * @return json data
 */
 Route::get('api/v1/schedule/user/{userId}', 'ScheduleController@getScheduleByUserId');


 /**
  * Start schedule.
  * url: ../api/v1/schedule/start
  * @var scheduleId
  * @var deviceId
  * @return json data
  */
  Route::post('api/v1/schedule/start', 'ScheduleController@startSchedule');

  /**
   * Complete schedule.
   * url: ../api/v1/schedule/complete
   * @var scheduleId
   * @var deviceId
   * @return json data
   */
   Route::post('api/v1/schedule/complete', 'ScheduleController@completeSchedule');

   /**
    * Cancel started schedule.
    * url: ../api/v1/schedule/start
    * @var scheduleId
    * @var deviceId
    * @return json data
    */
    Route::post('api/v1/schedule/cancel', 'ScheduleController@cancelSchedule');

  /**
   * Get incoming schedule by User id
   * url: ../api/v1/schedule/user
   * @var userId
   * @return json data
   */
   Route::get('api/v1/schedule/incoming/user/{userId}', 'ScheduleController@getIncomingSchedule');

 // Token group
 /**
 * Mapping token - user.
 * @var deviceId
 * @var userId
 */
 Route::post('api/v1/token/save', 'TokenController@saveToken');

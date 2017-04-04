
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
 Route::get('api/v1/user/{id}', 'UserController@getUserById');

 /**
  * Change user password
  * url: ../api/v1/user
  * @var email
  * @var currentPassword
  * @var newPassword
  * @return json data
  */
  Route::post('api/v1/user/changePassword', 'UserController@changePassword');


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

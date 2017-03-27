package vn.efode.vts.model;

/**
 * Created by Tri on 3/21/17.
 * User model class.
 */

public class UserModel {

    /**
     * User's name.
     */
    private String username;

    /**
     * Set username.
     * @param username User's name.
     * @return user's name.
     */
    public void setUsername(String username, String number) throws Exception{
        try {
            int intNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new Exception();
        }

        this.username = username;
    }

}

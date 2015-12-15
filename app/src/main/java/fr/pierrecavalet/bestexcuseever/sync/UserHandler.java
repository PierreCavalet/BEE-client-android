package fr.pierrecavalet.bestexcuseever.sync;


/**
 * Created by Pierre on 28/10/2015.
 */
public class UserHandler {

    private static String username = null;

    public static synchronized String getUsername() {
        return username;
    }

    public static synchronized void setUsername(String username) {
        UserHandler.username = username;
    }
}

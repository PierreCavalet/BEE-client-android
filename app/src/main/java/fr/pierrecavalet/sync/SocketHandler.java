package fr.pierrecavalet.sync;

import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by Pierre on 26/10/2015.
 */
public class SocketHandler {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}

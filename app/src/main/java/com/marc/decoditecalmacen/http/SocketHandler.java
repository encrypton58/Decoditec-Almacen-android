package com.marc.decoditecalmacen.http;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    private static Socket socket;
    private static SocketHandler handler;

    private SocketHandler(){
        setSocket();
    }

    private synchronized void setSocket(){
        try{
            socket = IO.socket(Rutes.host);
        }catch (URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }

    public static synchronized Socket getSocket(){
        if(handler == null){
            handler = new SocketHandler();
        }
        return socket;
    }


}

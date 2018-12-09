package com.access;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.Future;
import org.apache.tomcat.util.codec.binary.Base64;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class EventClient
{
    WebSocketClient client = new WebSocketClient();
    EventSocket socket = new EventSocket();
    ClientUpgradeRequest request = new ClientUpgradeRequest();
    Session aSession;
    
    public void start(URI uri,String user, String pass) throws Exception
    {
         String userpass = user + ":" + pass;

        String authorization = "Basic " + new String(new Base64().encode(userpass.getBytes())).trim();

        request.setHeader("Authorization", authorization);
        
      // The socket that receives events
        client.start();               
        // Attempt Connect
        Future<Session> fut = client.connect(socket,uri,request);

        // Wait for Connect
        Session session = fut.get();
        System.out.println(session.getUpgradeRequest().toString());
        this.aSession = session;
    }
    
    public void sendMessage(String message) throws IOException{
        // Send a message
        aSession.getRemote().sendString(message);
    }
    
    public void close() throws Exception{
        aSession.close();
        client.stop();
        this.cleanResponse();
    }
    
    @OnWebSocketMessage
    public String getResponse(){
       return socket.getTest();
    }
    
    private void cleanResponse(){
       socket.setText(null);
    }
    
    public static boolean isOnline(URI uri) {
     try {
         System.out.println(InetAddress.getByName(uri.getHost()));
         return InetAddress.getByName(uri.getHost()).isReachable(2000); //Replace with your name
        } catch (Exception e)
        {
        return false;
        }
    }
}
package com.access;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter
{
    String text;
    
    public void setText(String paTest){
        this.text = paTest;
    }
    
    public String getTest(){
        return this.text;
    }
    
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected");
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        this.setText(message);
        System.out.println("Received TEXT message: " + message);
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
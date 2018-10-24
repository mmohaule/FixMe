package com.mmohaule.router.controller;

import com.mmohaule.router.model.Attachment;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Hashtable;


public class RequestHandler {

    private static Hashtable<String, Attachment> brokerTable = new Hashtable<String, Attachment>();
    private static Hashtable<String, Attachment> marketTable = new Hashtable<String, Attachment>();

    public static Attachment processRequest(Attachment attachment) {

        int     limits;
        byte[]  bytes;
        String  msg;

        attachment.getBuffer().flip();
        limits = attachment.getBuffer().limit();
        bytes = new byte[limits];
        attachment.getBuffer().get(bytes, 0, limits);
        msg = new String(bytes);
        System.out.println(attachment.getClientAddress() + ": " + msg);
        //attachment.setRead(false);
        attachment.getBuffer().flip();

        if (msg.equals("0")) {
            Charset cs = Charset.forName("UTF-8");
            attachment.getBuffer().clear();
            System.out.println("processRequest() - line 85: Clearing buffer...");
            byte[] data = attachment.getID().getBytes(cs);
            attachment.getBuffer().put(data);
            attachment.getBuffer().flip();
            //addToRouteTable(attachment);
        }
        else
            return marketTable.get("2");
        return null;
    }

    public static void addToRouteTable(Attachment attachment) {

        if (attachment.getPort() == 5000)
            brokerTable.put(attachment.getID(), attachment);
        else if (attachment.getPort() == 5001)
            marketTable.put(attachment.getID(), attachment);
    }
}

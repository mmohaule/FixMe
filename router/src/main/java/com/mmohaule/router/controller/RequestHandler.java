package com.mmohaule.router.controller;

import com.mmohaule.router.model.Attachment;
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
        attachment.getBuffer().flip();

    
            String ID = ResponseGenerator.extractSenderCompId(msg);
            if (ID == null)
            	ID = ResponseGenerator.extractCompId(msg);
            return getChannelByID(attachment, ID);
    }

    public static void addToRouteTable(Attachment attachment) {

        if (attachment.getPort() == 5000)
            brokerTable.put(attachment.getID(), attachment);
        else if (attachment.getPort() == 5001)
            marketTable.put(attachment.getID(), attachment);
    }

    private static Attachment getChannelByID(Attachment attachment, String ID) {
        if (attachment.getPort() == 5000)
            return marketTable.get(ID);
        else if (attachment.getPort() == 5001)
            return brokerTable.get(ID);
        else
            return null;
    }
}

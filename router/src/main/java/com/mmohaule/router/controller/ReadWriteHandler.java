package com.mmohaule.router.controller;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.router.model.Attachment;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	
    public void completed(Integer result, Attachment attachment) {

    	System.out.println("\nEkucaleni komhlaba");
        if (result == -1) {
            try {
                attachment.getClientChannel().close();
                System.out.println("Stopped listening to the client: " + attachment.getClientAddress());
                if (attachment.getPort() == 5000) {
        	        RequestHandler.brokerTable.remove(attachment.getID());
                }
                else if (attachment.getPort() == 5001) {
        	        RequestHandler.marketTable.remove(attachment.getID());
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return ;
        }

        
        int     limits;
        byte[]  bytes;
        String  msg;

        
        
        if (attachment.isRead()) {
        	Attachment channel = RequestHandler.processRequest(attachment);
            attachment.setRead(false);
        	
        	if (channel != null) {

                limits = attachment.getBuffer().limit();
                bytes = new byte[limits];
                attachment.getBuffer().get(bytes, 0, limits);
                msg = new String(bytes);
                
                System.out.println("Buffer" + ": " + msg);
                System.out.println("Writting to : " + channel.getID());


                attachment.getBuffer().clear();

                Charset cs = Charset.forName("UTF-8");
                byte[] data = msg.getBytes(cs);

                channel.getBuffer().put(data);
                channel.getBuffer().flip();
                channel.setRead(false);
                channel.setMustRead(true);

                channel.getClientChannel().write(channel.getBuffer(), channel, this );

        	}
        	else {
        	    System.out.println("writing back to client");


                RequestHandler.getMarkets(attachment);
                attachment.getClientChannel().write(attachment.getBuffer(), attachment, this);
            }
        }
        else {
            attachment.getBuffer().clear();
            attachment.setRead(true);
            
            System.out.println("Listening...");
            try {
            	attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
            }
            catch(Exception e) {
            }
            
            /*if (attachment.isMustRead()) {
                attachment.setRead(true);
                System.out.println("Listening...");
                try {
                	attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
                }
                catch(Exception e) {
                }
            }
            else {
                attachment.setRead(false);
                System.out.println("Not listening");
            }*/
        }

    }

    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }

}
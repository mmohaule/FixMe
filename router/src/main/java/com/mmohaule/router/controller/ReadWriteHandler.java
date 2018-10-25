package com.mmohaule.router.controller;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.router.model.Attachment;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	
    public void completed(Integer result, Attachment attachment) {

        if (result == -1) {
            try {
                attachment.getClientChannel().close();
                System.out.println("Stopped listening to the client: " + attachment.getClientAddress());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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
        	    System.out.println("Market is null");
        	    if (attachment.getPort() == 5001) {
        	        attachment.setMustRead(false);
                }
                attachment.getClientChannel().write(attachment.getBuffer(), attachment, this);
            }
        }
        else {
            attachment.getBuffer().clear();
            if (attachment.isMustRead()) {
                attachment.setRead(true);
                System.out.println("completed() - line 75: Clearing buffer...");
                attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
            }
            else {
                attachment.setRead(false);
                System.out.println("Not listening");
            }
        }

    }

    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }

}
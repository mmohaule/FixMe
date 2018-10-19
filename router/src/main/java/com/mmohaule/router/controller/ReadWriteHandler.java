package com.mmohaule.router.controller;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.router.model.Attachment;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
	
	private static int clientID = 0;
	

	ReadWriteHandler() {
		clientID++;
	}
	
    public void completed(Integer result, Attachment attachment) {

        if (result == -1) {
            try {
                attachment.getClient().close();
                System.out.println("Stopped listening to the client: " + attachment.getClientAddress());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (attachment.isRead()) {
        	processRequest(attachment);
            attachment.getClient().write(attachment.getBuffer(), attachment, this);
        }
        else {
            attachment.setRead(true);
            attachment.getBuffer().clear();
            attachment.getClient().read(attachment.getBuffer(), attachment, this);
        }

    }

    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }
    
    private static void processRequest(Attachment attachment) {
    	
    	int     limits;
        byte[]  bytes;
        String  msg;

        attachment.getBuffer().flip();
        limits = attachment.getBuffer().limit();
        bytes = new byte[limits];
        attachment.getBuffer().get(bytes, 0, limits);
        msg = new String(bytes);
        System.out.println(attachment.getClientAddress() + ": " + msg);
        attachment.setRead(false);
        attachment.getBuffer().flip();
        
    	if (msg.equals("0")) {
    		Charset cs = Charset.forName("UTF-8");
    		attachment.getBuffer().clear();
    		byte[] data = getID().getBytes(cs);
			attachment.getBuffer().put(data);
			attachment.getBuffer().flip();
    	}
    	
    }
    
    private static String getID() {
		return Integer.toString(clientID);
	}

}
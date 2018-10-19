package com.mmohaule.router.controller;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Hashtable;

import com.mmohaule.router.model.Attachment;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
	
	private static int clientID = 0;
	private static Hashtable<String, AsynchronousSocketChannel> brokerTable = new Hashtable<String, AsynchronousSocketChannel>();
	private static Hashtable<String, AsynchronousSocketChannel> marketTable = new Hashtable<String, AsynchronousSocketChannel>();
	

	ReadWriteHandler() {
		clientID++;
	}
	
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
        	AsynchronousSocketChannel channel = processRequest(attachment);
        
        	
        	if (channel != null) {
        		
        		
        		//attachment.getBuffer().flip();
                limits = attachment.getBuffer().limit();
                bytes = new byte[limits];
                attachment.getBuffer().get(bytes, 0, limits);
                msg = new String(bytes);
                
                
                System.out.println("Buffer" + ": " + msg);
        		System.out.println("Market found: ");
        		channel.write(attachment.getBuffer(), attachment, this);
        	}
        	else
        		attachment.getClientChannel().write(attachment.getBuffer(), attachment, this);
        }
        else {
            attachment.setRead(true);
            attachment.getBuffer().clear();
            System.out.println("complted() - line 57: Clearing buffer...");
            attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
            
            //attachment.getBuffer().flip();
            limits = attachment.getBuffer().limit();
            bytes = new byte[limits];
            attachment.getBuffer().get(bytes, 0, limits);
            msg = new String(bytes);
            
            System.out.println("Buffer (read)" + ": " + msg);
        }

    }

    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }
    
    private static AsynchronousSocketChannel processRequest(Attachment attachment) {
    	
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
    		System.out.println("processRequest() - line 85: Clearing buffer...");
    		byte[] data = getID().getBytes(cs);
			attachment.getBuffer().put(data);
			attachment.getBuffer().flip();
			addToRouteTable(attachment);
    	}
    	else
    		return marketTable.get("2");
    	return null;
    }
    
/*    static AsynchronousSocketChannel getReciptient(String msg) {
    	 if (msg.equals("0")) {
 			
         	//System.out.println("Market Channel: " + channel);
 		}
 		else if (attachment.getPort() == 5001)
 			marketTable.put(getID(), attachment.getClientChannel());
		return null;
    }*/
    
    private static String getID() {
		return Integer.toString(clientID);
	}
    
	private static void addToRouteTable(Attachment attachment) {
		
		if (attachment.getPort() == 5000)
			brokerTable.put(getID(), attachment.getClientChannel());
		else if (attachment.getPort() == 5001)
			marketTable.put(getID(), attachment.getClientChannel());	
    }

}
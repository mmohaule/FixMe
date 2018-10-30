package com.mmohaule.router.controller;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.router.model.Attachment;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {

	private static int ID = 100000;

	public void completed(AsynchronousSocketChannel clientChannel, Attachment attachment) {
		try {
			SocketAddress clientAddress = clientChannel.getRemoteAddress();
			System.out.format("%nAccepted a  connection from  %s%n", clientAddress);
			attachment.getServerChannel().accept(attachment, this);
			
			ReadWriteHandler readWriteHandler = new ReadWriteHandler();
			Attachment newAttachment = new Attachment();
			
			newAttachment.setServerChannel(attachment.getServerChannel());
			newAttachment.setPort(attachment.getPort());
			newAttachment.setClientChannel(clientChannel);
			newAttachment.setBuffer(ByteBuffer.allocate(2048));
			newAttachment.setRead(false);
			newAttachment.setClientAddr(clientAddress);
			newAttachment.setID(ID++);
			RequestHandler.addToRouteTable(newAttachment);
			
			Charset cs = Charset.forName("UTF-8");
            byte[] data = newAttachment.getID().getBytes(cs);

            newAttachment.getBuffer().put(data);
            newAttachment.getBuffer().flip();
			
			clientChannel.write(newAttachment.getBuffer(), newAttachment, readWriteHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable e, Attachment attach) {
		System.out.println("Failed to accept a  connection.");
		e.printStackTrace();
	}
}

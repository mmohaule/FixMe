package com.mmohaule.router.controller;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.mmohaule.router.model.Attachment;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {
	public void completed(AsynchronousSocketChannel clientChannel, Attachment attachment) {
		try {
			SocketAddress clientAddress = clientChannel.getRemoteAddress();
			System.out.format("Accepted a  connection from  %s%n", clientAddress);
			attachment.getServerChannel().accept(attachment, this);
			
			ReadWriteHandler readWriteHandler = new ReadWriteHandler();
			Attachment newAttachment = new Attachment();
			
			newAttachment.setServerChannel(attachment.getServerChannel());
			newAttachment.setPort(attachment.getPort());
			newAttachment.setClientChannel(clientChannel);
			newAttachment.setBuffer(ByteBuffer.allocate(2048));
			newAttachment.setRead(true);
			newAttachment.setClientAddr(clientAddress);
			
			clientChannel.read(newAttachment.getBuffer(), newAttachment, readWriteHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable e, Attachment attach) {
		System.out.println("Failed to accept a  connection.");
		e.printStackTrace();
	}
}

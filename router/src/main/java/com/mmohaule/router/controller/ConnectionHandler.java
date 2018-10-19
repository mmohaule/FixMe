package com.mmohaule.router.controller;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.mmohaule.router.model.Attachment;

public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {
	public void completed(AsynchronousSocketChannel client, Attachment attach) {
		try {
			SocketAddress clientAddr = client.getRemoteAddress();
			System.out.format("Accepted a  connection from  %s%n", clientAddr);
			attach.getServer().accept(attach, this);
			ReadWriteHandler rwHandler = new ReadWriteHandler();
			Attachment newAttach = new Attachment();
			newAttach.setServer(attach.getServer());
			newAttach.setClient(client);
			newAttach.setBuffer(ByteBuffer.allocate(2048));
			newAttach.setRead(true);
			newAttach.setClientAddr(clientAddr);
			client.read(newAttach.getBuffer(), newAttach, rwHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable e, Attachment attach) {
		System.out.println("Failed to accept a  connection.");
		e.printStackTrace();
	}
}

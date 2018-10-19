package com.mmohaule.market.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.mmohaule.market.model.Attachment;


public class Connection {
	String host;
	int port;

	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() {
		
		try {
			AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
			SocketAddress serverAddr = new InetSocketAddress(host, port);
			Future<Void> result = channel.connect(serverAddr);
			result.get();
			System.out.println("Connected");

			Attachment attach = new Attachment();
			attach.setChannel(channel);
			attach.setBuffer(ByteBuffer.allocate(2048));
			attach.setRead(false);
			attach.setMainThread(Thread.currentThread());

			Charset cs = Charset.forName("UTF-8");
			String msg = "0";
			byte[] data = msg.getBytes(cs);
			attach.getBuffer().put(data);
			attach.getBuffer().flip();

			ReadWriteHandler readWriteHandler = new ReadWriteHandler();
			channel.write(attach.getBuffer(), attach, readWriteHandler);
			attach.getMainThread().join();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}

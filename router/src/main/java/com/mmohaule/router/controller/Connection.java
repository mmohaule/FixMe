package com.mmohaule.router.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import com.mmohaule.router.model.Attachment;

public class Connection implements Runnable {
	String host;
	int port;
	
	public Connection(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		try {
			AsynchronousServerSocketChannel server;
			server = AsynchronousServerSocketChannel.open();

			InetSocketAddress sAddr = new InetSocketAddress(host, port);
			//System.out.println("Before Bind");
			server.bind(sAddr);
			System.out.format("Server is listening at %s%n", sAddr);
			Attachment attach = new Attachment();
			attach.setServer(server);
			server.accept(attach, new ConnectionHandler());
			Thread.currentThread().join();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

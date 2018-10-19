package com.mmohaule.router.model;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {

	private AsynchronousServerSocketChannel serverChannel;
	private AsynchronousSocketChannel clientChannel;
	private ByteBuffer buffer;
	private SocketAddress clientAddr;
	private int port;
	private boolean isRead;

	public AsynchronousSocketChannel getClientChannel() {
		return clientChannel;
	}

	public void setClientChannel(AsynchronousSocketChannel client) {
		this.clientChannel = client;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public SocketAddress getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(SocketAddress clientAddr) {
		this.clientAddr = clientAddr;
	}

	public SocketAddress getClientAddress() {
		return clientAddr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int serverPort) {
		port = serverPort;
	}

	public AsynchronousServerSocketChannel getServerChannel() {
		return serverChannel;
	}

	public void setServerChannel(AsynchronousServerSocketChannel serverChannel) {
		this.serverChannel = serverChannel;
	}
}

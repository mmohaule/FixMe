package com.mmohaule.broker.model;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class Attachment {

	private AsynchronousSocketChannel channel;
	private Thread mainThread;
	private ByteBuffer buffer;
	private boolean isRead;

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

	public AsynchronousSocketChannel getChannel() {
		return channel;
	}

	public void setChannel(AsynchronousSocketChannel channel) {
		this.channel = channel;
	}

	public Thread getMainThread() {
		return mainThread;
	}

	public void setMainThread(Thread mainThread) {
		this.mainThread = mainThread;
	}
}

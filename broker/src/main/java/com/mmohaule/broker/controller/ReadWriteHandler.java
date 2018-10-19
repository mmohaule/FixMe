package com.mmohaule.broker.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.broker.model.Attachment;

class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	public void completed(Integer result, Attachment attachment) {

		if (attachment.isRead()) {

			attachment.getBuffer().flip();
			Charset cs = Charset.forName("UTF-8");
			int limits = attachment.getBuffer().limit();
			byte bytes[] = new byte[limits];
			attachment.getBuffer().get(bytes, 0, limits);
			String msg = new String(bytes, cs);
			System.out.println("Server: " + msg);

			try {
				msg = this.getTextFromUser();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (msg.equalsIgnoreCase("bye")) {
				attachment.getMainThread().interrupt();
				return;
			}

			attachment.getBuffer().clear();
			byte[] data = msg.getBytes(cs);
			attachment.getBuffer().put(data);
			attachment.getBuffer().flip();
			attachment.setRead(false);
			attachment.getChannel().write(attachment.getBuffer(), attachment, this);
		} else {
			attachment.setRead(true);
			attachment.getBuffer().clear();
			attachment.getChannel().read(attachment.getBuffer(), attachment, this);
		}
	}

	public void failed(Throwable e, Attachment attach) {
		e.printStackTrace();
	}

	private String getTextFromUser() throws Exception {
		System.out.print("Me: ");
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		String msg = consoleReader.readLine();
		return msg;
	}

}
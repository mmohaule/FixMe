package com.mmohaule.market.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import com.mmohaule.market.model.Attachment;


class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	public void completed(Integer result, Attachment attachment) {

		int     limits = 0;
		byte[]  bytes;
		String  msg;
		Charset cs = Charset.forName("UTF-8");
		System.out.println("Market called");

		if (attachment.isRead()) {

			attachment.getBuffer().flip();
			limits = attachment.getBuffer().limit();
			bytes = new byte[limits];
			attachment.getBuffer().get(bytes, 0, limits);
			msg = new String(bytes, cs);
			System.out.println("Server: " + msg);
			attachment.setRead(false);
			attachment.getChannel().read(attachment.getBuffer(), attachment, this);
			/*try {
				msg = this.getTextFromUser();
			} catch (Exception e) {
				e.printStackTrace();
			}

			attachment.getBuffer().clear();
			byte[] data = msg.getBytes(cs);
			attachment.getBuffer().put(data);
			attachment.getBuffer().flip();
			attachment.setRead(false);
			attachment.getChannel().write(attachment.getBuffer(), attachment, this);*/
		} else {

			attachment.setRead(true);
			attachment.getBuffer().clear();
			System.out.println("is listening");
			attachment.getChannel().read(attachment.getBuffer(), attachment, this);

//			attachment.getBuffer().flip();
//			limits = attachment.getBuffer().limit();
//			bytes = new byte[limits];
//			attachment.getBuffer().get(bytes, 0, limits);
//			msg = new String(bytes);
			//System.out.println("After reading: ");
		}
	}

	public void failed(Throwable e, Attachment attach) {
		e.printStackTrace();
	}

	private String getTextFromUser() throws Exception {
//		System.out.print("Me: ");
//		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
//		String msg = consoleReader.readLine();
		return "hello";
	}

}
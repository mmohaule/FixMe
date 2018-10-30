package com.mmohaule.router.controller;

import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

import com.mmohaule.router.model.Attachment;

public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

	public void completed(Integer result, Attachment attachment) {

		if (result == -1) {
			try {
				attachment.getClientChannel().close();
				System.out.println("\nStopped listening to the client: " + attachment.getClientAddress());
				if (attachment.getPort() == 5000) {
					RequestHandler.brokerTable.remove(attachment.getID());
				} else if (attachment.getPort() == 5001) {
					RequestHandler.marketTable.remove(attachment.getID());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String message = null;

		if (attachment.isRead()) {
			Attachment channel = RequestHandler.processRequest(attachment);
			attachment.setRead(false);

			message = RequestHandler.buffToStr(attachment);
			
			if (channel != null) {

				stringToBuff(message, channel);
				channel.setRead(false);
				channel.setMustRead(true);
				channel.getClientChannel().write(channel.getBuffer(), channel, this);

			} else {

				if (message.equals("markets")) {
					RequestHandler.getMarkets(attachment);
				} else
					stringToBuff(message + " - No such market!", attachment);
				attachment.getClientChannel().write(attachment.getBuffer(), attachment, this);
			}
		} else {
			attachment.getBuffer().clear();
			attachment.setRead(true);

			try {
				attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
			} catch (Exception e) {
			}
		}

	}

	public void stringToBuff(String str, Attachment attachment) {
		Charset cs = Charset.forName("UTF-8");
		byte[] data = str.getBytes(cs);

		attachment.getBuffer().clear();
		attachment.getBuffer().put(data);
		attachment.getBuffer().flip();
	}

	public void failed(Throwable exc, Attachment attachment) {
		exc.printStackTrace();
	}

}
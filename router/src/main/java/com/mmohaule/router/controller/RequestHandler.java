package com.mmohaule.router.controller;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mmohaule.router.model.Attachment;

public class RequestHandler {

	static Hashtable<String, Attachment> brokerTable = new Hashtable<String, Attachment>();
	static Hashtable<String, Attachment> marketTable = new Hashtable<String, Attachment>();

	public static Attachment processRequest(Attachment attachment) {

		byte[] bytes;

		String ID;
		String message = buffToStr(attachment);

		if (RequestValidation.isCommand(message))
			ID = ResponseGenerator.extractCompId(message);
		else
			ID = ResponseGenerator.extractTargetCompId(message);

		return getChannelByID(attachment, ID);

	}

	public static void addToRouteTable(Attachment attachment) {

		if (attachment.getPort() == 5000)
			brokerTable.put(attachment.getID(), attachment);
		else if (attachment.getPort() == 5001)
			marketTable.put(attachment.getID(), attachment);
		System.out.println("Added to Table");
	}

	private static Attachment getChannelByID(Attachment attachment, String ID) {
		if (attachment.getPort() == 5000)
			return marketTable.get(ID);
		else if (attachment.getPort() == 5001)
			return brokerTable.get(ID);
		else
			return null;
	}

	public static String buffToStr(Attachment attachment) {
		int limit;
		byte[] bytes;
		String message;

		attachment.getBuffer().flip();
		limit = attachment.getBuffer().limit();
		bytes = new byte[limit];
		attachment.getBuffer().get(bytes, 0, limit);
		message = new String(bytes);
		System.out.println(attachment.getClientAddress() + ": " + message);
		attachment.getBuffer().flip();

		return message;
	}

	public static void getMarkets(Attachment attach) {
		String str = "";
		String key = null;
		byte[] bytes = null;

		Enumeration<String> e = marketTable.keys();

		while (e.hasMoreElements()) {
			key = (String) e.nextElement();
			System.out.println("Market: " + marketTable.get(key).getClientChannel().isOpen());

			if (!marketTable.get(key).getClientChannel().isOpen())
				marketTable.remove(key);
			else
				str += System.lineSeparator() + key;

		}

		bytes = str.getBytes();
		attach.getBuffer().clear();
		attach.getBuffer().put(bytes);
		attach.getBuffer().flip();
	}
}

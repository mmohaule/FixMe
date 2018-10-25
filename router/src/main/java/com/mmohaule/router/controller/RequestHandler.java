package com.mmohaule.router.controller;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mmohaule.router.model.Attachment;

public class RequestHandler {

	private static Hashtable<String, Attachment> brokerTable = new Hashtable<String, Attachment>();
	public static Hashtable<String, Attachment> marketTable = new Hashtable<String, Attachment>();

	public static Attachment processRequest(Attachment attachment) {

		int limits;
		byte[] bytes;
		String msg;

		attachment.getBuffer().flip();
		limits = attachment.getBuffer().limit();
		bytes = new byte[limits];
		attachment.getBuffer().get(bytes, 0, limits);
		msg = new String(bytes);
		System.out.println(attachment.getClientAddress() + ": " + msg);
		attachment.getBuffer().flip();

		String ID;

		if (RequestValidation.isCommand(msg))
			ID = ResponseGenerator.extractCompId(msg);
		else
			ID = ResponseGenerator.extractTargetCompId(msg);

		return getChannelByID(attachment, ID);

	}

	public static void addToRouteTable(Attachment attachment) {

		if (attachment.getPort() == 5000)
			brokerTable.put(attachment.getID(), attachment);
		else if (attachment.getPort() == 5001)
			marketTable.put(attachment.getID(), attachment);
	}

	private static Attachment getChannelByID(Attachment attachment, String ID) {
		if (attachment.getPort() == 5000)
			return marketTable.get(ID);
		else if (attachment.getPort() == 5001)
			return brokerTable.get(ID);
		else
			return null;
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

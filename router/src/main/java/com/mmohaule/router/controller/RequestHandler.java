package com.mmohaule.router.controller;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mmohaule.router.model.Attachment;

public class RequestHandler {

	static Hashtable<String, Attachment> brokerTable = new Hashtable<String, Attachment>();
	static Hashtable<String, Attachment> marketTable = new Hashtable<String, Attachment>();

	public static Attachment processRequest(Attachment attachment) {

		String ID;
		String message = buffToStr(attachment);
		byte[] bytes = null;

		System.out.println(attachment.getClientAddress() + ": " + message);

		if (RequestValidation.isCommand(message))
			ID = ResponseGenerator.extractCmdTargetID(message);
		else
			ID = ResponseGenerator.extractFixTargetID(message);

		if (isInTable(marketTable, ID) || attachment.getPort() == 5001)
			bytes = message.getBytes();
		else
			bytes = ID.getBytes();

		attachment.getBuffer().clear();
		attachment.getBuffer().put(bytes);

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

	public static String buffToStr(Attachment attachment) {
		int limit;
		byte[] bytes;
		String message;

		attachment.getBuffer().flip();
		limit = attachment.getBuffer().limit();
		bytes = new byte[limit];
		attachment.getBuffer().get(bytes, 0, limit);
		message = new String(bytes);
		attachment.getBuffer().flip();

		return message;
	}

	public static void getMarkets(Attachment attach) {
		String str = "";
		String key = null;
		byte[] bytes = null;
		int count = 0;

		Enumeration<String> e = marketTable.keys();

		while (e.hasMoreElements()) {
			key = (String) e.nextElement();

			if (count++ == 0)
				str += key;
			else
				str += System.lineSeparator() + key;
		}

		if (marketTable.isEmpty())
			str = "No markets found!";

		bytes = str.getBytes();
		attach.getBuffer().clear();
		attach.getBuffer().put(bytes);
		attach.getBuffer().flip();
	}

	public static boolean isInTable(Hashtable<String, Attachment> table, String ID) {
		if (table.get(ID) != null)
			return true;
		return false;
	}
}

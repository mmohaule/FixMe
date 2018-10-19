package com.mmohaule.broker;

import com.mmohaule.broker.controller.Connection;

public class Broker {
	final static String HOST = "localhost";

	public static void main(String[] args) {
		int port = 5000;

		Connection con = new Connection(HOST, port);
		con.start();

	}

}

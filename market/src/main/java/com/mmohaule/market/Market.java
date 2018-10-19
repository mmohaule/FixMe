package com.mmohaule.market;

import com.mmohaule.market.controller.Connection;

public class Market {
	final static String HOST = "localhost";

	public static void main(String[] args) {
		int port = 5001;

		Connection con = new Connection(HOST, port);
		con.start();

	}

}

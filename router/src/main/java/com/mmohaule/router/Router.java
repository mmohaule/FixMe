package com.mmohaule.router;

import com.mmohaule.router.controller.Connection;

public class Router {
	final static String HOST = "localhost";

	public static void main(String[] args) {
		
		int port = 5000;
		
		Connection brokerConnection = new Connection(HOST, port);
		
		
		port = 5001;
		Connection markertConnection = new Connection(HOST, port);
		
		Thread brokerThread = new Thread(brokerConnection);
		Thread marketThread = new Thread(markertConnection);
		
		brokerThread.start();
		marketThread.start();
		
	}
}

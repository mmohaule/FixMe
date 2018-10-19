package com.mmohaule.router;

import com.mmohaule.router.controller.Connection;

public class Router {
	final static String HOST = "localhost";

	public static void main(String[] args) {
		
		int port = 5000;
		
		Connection con1 = new Connection(HOST, port);
		
		
		port = 5001;
		Connection con2 = new Connection(HOST, port);
		
		Thread t1 = new Thread(con1);
		Thread t2 = new Thread(con2);
		
		t1.start();
		t2.start();
		
	}
}

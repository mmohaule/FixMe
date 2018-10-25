package com.mmohaule.broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.mmohaule.broker.model.Attachment;

public class Broker {

	public static void main(String[] args) {

		try {
			AsynchronousSocketChannel clientChannel;
			Future<Void> result;
			Attachment attachment;

			clientChannel = AsynchronousSocketChannel.open();
			result = clientChannel.connect(new InetSocketAddress("localhost", 5000));
			result.get();
			System.out.println("Connected to router");
			attachment = new Attachment();
			attachment.setClientChannel(clientChannel);
			attachment.setBuffer(ByteBuffer.allocate(2048));
			attachment.setRead(true);
			attachment.setBrokerIdSet(false);
			attachment.setMainThread(Thread.currentThread());
			clientChannel.read(attachment.getBuffer(), attachment, new ReadWriteHandler());

			attachment.getMainThread().join();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

	}

}

//   Test Values
// msg = "100200 list markets";
// msg = "100200 list 111222";
// msg =
// "8=FIX.4.0|9=86|35=D|49=100000|56=100000|34=1|52=20181022-07:09:54|11=1|21=1|55=NVDA|54=1|38=777|40=1|10=019|";
// msg =
// "8=FIX.4.0|9=85|35=D|49=777890|56=980123|34=1|52=20181022-09:20:28|11=1|21=1|55=AAPL|54=2|38=13|40=1|10=254|";
// msg = "unknown request";

// 8=FIX.4.0|9=86|35=D|49=100000|56=100002|34=1|52=20181022-07:09:54|11=1|21=1|55=NVDA|54=1|38=777|40=1|10=019|

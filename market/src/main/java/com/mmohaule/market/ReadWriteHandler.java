package com.mmohaule.market;


import java.nio.channels.CompletionHandler;

import com.mmohaule.market.request_process_chain.RequestProcessChain;


public class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {

    public void completed(Integer result, Attachment attachment) {

        if (attachment.isRead()) {
            int                     limits;
            byte[]                  routerRequestBytes;
            String                  routerRequest;
            Request                 request;
            RequestProcessChain     requestProcessChain;

            attachment.getBuffer().flip();
            limits = attachment.getBuffer().limit();
            routerRequestBytes = new byte[limits];
            attachment.getBuffer().get(routerRequestBytes, 0, limits);
            routerRequest = new String(routerRequestBytes);
            System.out.println("request received: " + routerRequest);

            request = new Request(attachment, routerRequest);
            requestProcessChain = new RequestProcessChain();
            requestProcessChain.getProcessChain1().execute(request);
        }
        else {
            attachment.setRead(true);
            attachment.getBuffer().clear();
            attachment.getClientChannel().read(attachment.getBuffer(), attachment, this);
        }

    }

    public void failed(Throwable exc, Attachment attachment) {
        exc.printStackTrace();
    }

}

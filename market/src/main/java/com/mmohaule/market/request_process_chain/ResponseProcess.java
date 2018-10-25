package com.mmohaule.market.request_process_chain;

import com.mmohaule.market.Attachment;
import com.mmohaule.market.ReadWriteHandler;
import com.mmohaule.market.Request;

public class ResponseProcess extends ProcessChain {

    @Override
    public void execute(Request request) {
        Attachment  attachment;
        String      marketResponse;
        byte[]      byteMarketResponse;

        attachment = request.getAttachment();
        marketResponse = request.getResponse();
        attachment.getBuffer().clear();
        byteMarketResponse = marketResponse.getBytes();
        attachment.getBuffer().put(byteMarketResponse);
        attachment.getBuffer().flip();
        attachment.setRead(false);
        attachment.getClientChannel().write(attachment.getBuffer(), attachment, new ReadWriteHandler());
        if (this.getNextProcess() != null)
            this.getNextProcess().execute(request);
    }

}

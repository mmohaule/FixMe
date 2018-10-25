package com.mmohaule.market;

import com.mmohaule.core.FixMsg;
import com.mmohaule.core.FixMsgExecute;
import com.mmohaule.core.FixMsgFactory;
import com.mmohaule.core.FixMsgReject;
import com.mmohaule.core.FixMsgType;

public class ResponseGenerator {

    public static String listMarkets(String request) {
        String[]    tokens;

        tokens = request.split(" ");
        return (tokens[0] + " " + MarketData.getMarketId());
    }

    public static String listMarketId(String request) {
        String[]    tokens;
        MarketData  marketData;

        tokens = request.split(" ");
        marketData = MarketData.getInstance();
        return (marketData.toString() + " " + tokens[0]);
    }

    public static String extractSenderCompId(String request) {
        int         startIndex;
        int         endIndex;
        String      senderCompIdTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.SENDER_COMP_ID_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        senderCompIdTagVal = request.substring(startIndex, endIndex);
        tokens = senderCompIdTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }

    public static String extractRefSeqNum(String request) {
        int         startIndex;
        int         endIndex;
        String      refSeqNumTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.MSG_SEQ_NUM_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        refSeqNumTagVal = request.substring(startIndex, endIndex);
        tokens = refSeqNumTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }

    public static String extractTickerSymbol(String request) {
        int         startIndex;
        int         endIndex;
        String      tickerSymbolTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.SYMBOL_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        tickerSymbolTagVal = request.substring(startIndex, endIndex);
        tokens = tickerSymbolTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }

    public static String extractSide(String request) {
        int         startIndex;
        int         endIndex;
        String      sideTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.SIDE_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        sideTagVal = request.substring(startIndex, endIndex);
        tokens = sideTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }

    public static String extractOrdQty(String request) {
        int         startIndex;
        int         endIndex;
        String      ordQtyTagVal;
        String[]    tokens;

        startIndex = request.indexOf(FixMsg.ORDER_QTY_TAG + FixMsg.TAG_VAL_LINK);
        endIndex = request.indexOf(FixMsg.TAG_VAL_SEPARATOR, startIndex);
        ordQtyTagVal = request.substring(startIndex, endIndex);
        tokens = ordQtyTagVal.split(FixMsg.TAG_VAL_LINK);
        return (tokens[1]);
    }

    public static String execute(String request) {
        FixMsgExecute   fixMsgExecute;
        MarketData      marketData;

        marketData = MarketData.getInstance();
        fixMsgExecute = FixMsgFactory.newExecMsg(
                            MarketData.getMarketId(),
                            extractSenderCompId(request),
                            Integer.toString(FixMsgType.getOrderId()),
                            extractTickerSymbol(request),
                            extractSide(request),
                            Integer.parseInt(extractOrdQty(request)),
                            Integer.parseInt(extractOrdQty(request)),
                            marketData.getStocks().get(extractTickerSymbol(request)).getPrice()
                        );
        fixMsgExecute.setFixMsgExec();
        return (fixMsgExecute.getFixMsgExec());
    }

    public static String reject(String request) {
        FixMsgReject    fixMsgReject;

        fixMsgReject = FixMsgFactory.newRejectMsg(MarketData.getMarketId(), extractSenderCompId(request), extractRefSeqNum(request));
        fixMsgReject.setFixMsgReject();
        return (fixMsgReject.getFixMsgReject());
    }

}

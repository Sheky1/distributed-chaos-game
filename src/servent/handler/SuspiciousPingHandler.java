package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.SuspiciousPongMessage;
import servent.message.util.MessageUtil;

public class SuspiciousPingHandler implements MessageHandler{

    private final Message clientMessage;

    public SuspiciousPingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SUSPICIOUS_PING) {
            MessageUtil.sendMessage(new SuspiciousPongMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort()));
        } else {
            AppConfig.timestampedErrorPrint("PING handler got something that is not a start drawing message.");
        }

    }
}

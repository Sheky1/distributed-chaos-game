package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class SuspiciousPongHandler implements MessageHandler {

    private final Message clientMessage;

    public SuspiciousPongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SUSPICIOUS_PONG) {
            AppConfig.timestampedStandardPrint("Got a sus pong message");
            AppConfig.suspiciousPingsSent.remove(Integer.valueOf(clientMessage.getSenderPort()));
        } else {
            AppConfig.timestampedErrorPrint("PING handler got something that is not a start drawing message.");
        }

    }
}

package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class PongHandler implements MessageHandler {

    private final Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.PONG) {
            AppConfig.timestampedStandardPrint("Got a pong message");
            AppConfig.pongResponses.add(clientMessage.getSenderPort());
            if(AppConfig.suspiciousPorts.contains(clientMessage.getSenderPort())) AppConfig.suspiciousPorts.remove(Integer.valueOf(clientMessage.getSenderPort()));
        } else {
            AppConfig.timestampedErrorPrint("PING handler got something that is not a start drawing message.");
        }
    }
}

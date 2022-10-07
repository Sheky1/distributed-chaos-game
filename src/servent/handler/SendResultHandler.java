package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.SendResultMessage;

public class SendResultHandler implements MessageHandler {

    private final Message clientMessage;

    public SendResultHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SEND_RESULT) {
            SendResultMessage sendResultMessage = (SendResultMessage) clientMessage;
            AppConfig.allDots.addAll(sendResultMessage.getDots());
            AppConfig.resultsGot += 1;
            AppConfig.timestampedStandardPrint("Received " + sendResultMessage.getDots().size());
        } else {
            AppConfig.timestampedErrorPrint("START DRAWING handler got something that is not a start drawing message.");
        }
    }
}

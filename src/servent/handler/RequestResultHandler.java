package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.SendResultMessage;
import servent.message.util.MessageUtil;

public class RequestResultHandler implements MessageHandler{

    private final Message clientMessage;

    public RequestResultHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.REQUEST_RESULT) {
            MessageUtil.sendMessage(new SendResultMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), AppConfig.myDots));
        } else {
            AppConfig.timestampedErrorPrint("START DRAWING handler got something that is not a start drawing message.");
        }
    }
}

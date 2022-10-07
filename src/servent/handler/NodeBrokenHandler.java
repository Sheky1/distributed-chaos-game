package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;

public class NodeBrokenHandler implements MessageHandler {

    private final Message clientMessage;

    public NodeBrokenHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.NODE_BROKEN) {
            if (!AppConfig.brokenNodes.contains(Integer.parseInt(clientMessage.getMessageText()))) {
                AppConfig.brokenNodes.add(Integer.parseInt(clientMessage.getMessageText()));
                if (AppConfig.suspiciousPorts.contains(Integer.parseInt(clientMessage.getMessageText())))
                    AppConfig.suspiciousPorts.remove(Integer.valueOf(Integer.parseInt(clientMessage.getMessageText())));
                AppConfig.serventInfoList.remove(new ServentInfo("127.0.0.1", Integer.parseInt(clientMessage.getMessageText())));
            }
        } else {
            AppConfig.timestampedErrorPrint("WELCOME handler got something that is not a welcome message.");
        }
    }
}

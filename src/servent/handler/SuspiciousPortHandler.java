package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.SuspiciousPingMessage;
import servent.message.SuspiciousPortCheckedMessage;
import servent.message.util.MessageUtil;

public class SuspiciousPortHandler implements MessageHandler {

    private final Message clientMessage;

    public SuspiciousPortHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SUSPICIOUS_PORT) {
            MessageUtil.sendMessage(new SuspiciousPingMessage(AppConfig.myServentInfo.getListenerPort(), Integer.parseInt(clientMessage.getMessageText())));
            AppConfig.suspiciousPingsSent.add(Integer.parseInt(clientMessage.getMessageText()));

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(AppConfig.suspiciousPingsSent.contains(Integer.parseInt(clientMessage.getMessageText()))) {
                MessageUtil.sendMessage(new SuspiciousPortCheckedMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), clientMessage.getMessageText(), true));
            } else {
                MessageUtil.sendMessage(new SuspiciousPortCheckedMessage(AppConfig.myServentInfo.getListenerPort(), clientMessage.getSenderPort(), clientMessage.getMessageText(), false));
            }

        } else {
            AppConfig.timestampedErrorPrint("WELCOME handler got something that is not a welcome message.");
        }
    }
}

package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.WelcomeMessage;

public class WelcomeHandler implements MessageHandler{

    private final Message clientMessage;

    public WelcomeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.WELCOME) {
            WelcomeMessage welcomeMessage = (WelcomeMessage) clientMessage;
            AppConfig.serventInfoList.add(new ServentInfo(welcomeMessage.getIpAddress(), welcomeMessage.getSenderPort()));
            if(!welcomeMessage.getPictureParts().isEmpty()) {
                AppConfig.myColleagues = welcomeMessage.getPictureParts();
                AppConfig.myFrame = welcomeMessage.getPictureParts().get(AppConfig.myServentInfo.getListenerPort());
            }
            System.out.println("Added a neighbor that welcomed me with port: " + welcomeMessage.getSenderPort() + ". I currently have " + AppConfig.serventInfoList + " as neighbors.");
        } else {
            AppConfig.timestampedErrorPrint("WELCOME handler got something that is not a welcome message.");
        }
    }
}

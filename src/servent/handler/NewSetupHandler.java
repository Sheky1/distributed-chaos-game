package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NewSetupMessage;

public class NewSetupHandler implements MessageHandler{

    private final Message clientMessage;

    public NewSetupHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.NEW_SETUP) {
            NewSetupMessage  newSetupMessage = (NewSetupMessage) clientMessage;
            AppConfig.myDots.clear();
            AppConfig.myColleagues = newSetupMessage.getColleagues();
            AppConfig.colleaguePorts = newSetupMessage.getColleagues().keySet().stream().toList();
            AppConfig.myFrame = newSetupMessage.getColleagues().get(AppConfig.myServentInfo.getListenerPort());
        } else {
            AppConfig.timestampedErrorPrint("PING handler got something that is not a start drawing message.");
        }
    }
}

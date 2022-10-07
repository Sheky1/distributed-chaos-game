package servent.handler;

import app.AppConfig;
import servent.message.BackupMessage;
import servent.message.Message;
import servent.message.MessageType;

public class BackupHandler implements MessageHandler{

    private final Message clientMessage;

    public BackupHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.BACKUP) {
            BackupMessage backupMessage = (BackupMessage)  clientMessage;
            AppConfig.backups.put(clientMessage.getSenderPort(), backupMessage.getDots());
        } else {
            AppConfig.timestampedErrorPrint("PING handler got something that is not a start drawing message.");
        }
    }
}

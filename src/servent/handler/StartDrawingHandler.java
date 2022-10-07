package servent.handler;

import app.AppConfig;
import app.PictureDrawWorker;
import servent.message.Message;
import servent.message.MessageType;

public class StartDrawingHandler implements MessageHandler{

    private final Message clientMessage;
    private final PictureDrawWorker pictureDrawWorker;

    public StartDrawingHandler(Message clientMessage, PictureDrawWorker pictureDrawWorker) {
        this.clientMessage = clientMessage;
        this.pictureDrawWorker = pictureDrawWorker;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.START_DRAWING) {
            pictureDrawWorker.startDrawing();
        } else {
            AppConfig.timestampedErrorPrint("START DRAWING handler got something that is not a start drawing message.");
        }
    }
}

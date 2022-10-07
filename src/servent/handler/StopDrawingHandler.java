package servent.handler;

import app.AppConfig;
import app.PictureDrawWorker;
import servent.message.Message;
import servent.message.MessageType;

public class StopDrawingHandler implements MessageHandler{

    private final Message clientMessage;
    private final PictureDrawWorker pictureDrawWorker;

    public StopDrawingHandler(Message clientMessage, PictureDrawWorker pictureDrawWorker) {
        this.clientMessage = clientMessage;
        this.pictureDrawWorker = pictureDrawWorker;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.STOP_DRAWING) {
            pictureDrawWorker.stopDrawing();
        } else {
            AppConfig.timestampedErrorPrint("START DRAWING handler got something that is not a start drawing message.");
        }
    }
}

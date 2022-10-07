package servent.message;

public class StopDrawingMessage extends BasicMessage {
    public StopDrawingMessage(int senderPort, int receiverPort) {
        super(MessageType.STOP_DRAWING, senderPort, receiverPort);
    }
}

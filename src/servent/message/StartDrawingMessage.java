package servent.message;

public class StartDrawingMessage extends BasicMessage{
    public StartDrawingMessage(int senderPort, int receiverPort) {
        super(MessageType.START_DRAWING, senderPort, receiverPort);
    }
}

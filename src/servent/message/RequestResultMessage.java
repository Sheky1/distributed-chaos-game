package servent.message;

public class RequestResultMessage extends BasicMessage{
    public RequestResultMessage(int senderPort, int receiverPort) {
        super(MessageType.REQUEST_RESULT, senderPort, receiverPort);
    }
}

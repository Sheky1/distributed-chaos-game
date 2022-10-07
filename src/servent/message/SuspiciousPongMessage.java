package servent.message;

public class SuspiciousPongMessage extends BasicMessage{
    public SuspiciousPongMessage(int senderPort, int receiverPort) {
        super(MessageType.SUSPICIOUS_PONG, senderPort, receiverPort);
    }
}

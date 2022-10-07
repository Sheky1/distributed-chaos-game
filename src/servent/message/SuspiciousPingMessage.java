package servent.message;

public class SuspiciousPingMessage extends BasicMessage{
    public SuspiciousPingMessage(int senderPort, int receiverPort) {
        super(MessageType.SUSPICIOUS_PING, senderPort, receiverPort);
    }
}

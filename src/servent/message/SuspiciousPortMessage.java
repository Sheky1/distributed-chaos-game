package servent.message;

public class SuspiciousPortMessage extends BasicMessage{
    public SuspiciousPortMessage(int senderPort, int receiverPort, String port) {
        super(MessageType.SUSPICIOUS_PORT, senderPort, receiverPort, port);
    }
}

package servent.message;

public class SuspiciousPortCheckedMessage extends BasicMessage{
    boolean stillSuspicious;

    public SuspiciousPortCheckedMessage(int senderPort, int receiverPort, String port, boolean stillSuspicious) {
        super(MessageType.SUSPICIOUS_PORT_CHECKED, senderPort, receiverPort, port);
        this.stillSuspicious = stillSuspicious;
    }

    public boolean isStillSuspicious() {
        return stillSuspicious;
    }
}

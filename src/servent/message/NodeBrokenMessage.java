package servent.message;

public class NodeBrokenMessage extends BasicMessage{
    public NodeBrokenMessage(int senderPort, int receiverPort, String port) {
        super(MessageType.NODE_BROKEN, senderPort, receiverPort, port);
    }
}

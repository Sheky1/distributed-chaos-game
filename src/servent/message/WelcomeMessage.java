package servent.message;

import app.Dot;

import java.util.List;
import java.util.Map;

public class WelcomeMessage extends BasicMessage {

    private final String ipAddress;
    private final Map<Integer, List<Dot>> pictureParts;

    public WelcomeMessage(int senderPort, int receiverPort, String ipAddress, Map<Integer, List<Dot>> pictureParts) {
        super(MessageType.WELCOME, senderPort, receiverPort);
        this.ipAddress = ipAddress;
        this.pictureParts = pictureParts;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Map<Integer, List<Dot>> getPictureParts() {
        return pictureParts;
    }
}

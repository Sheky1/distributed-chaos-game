package servent.message;

import app.Dot;

import java.util.List;
import java.util.Map;

public class NewNodeMessage extends BasicMessage{
    private final Map<Integer, List<Dot>> pictureParts;

    public NewNodeMessage(int originalSenderInfo, int receiverInfo, String messageText, Map<Integer, List<Dot>> pictureParts) {
        super(MessageType.NEW_NODE, originalSenderInfo, receiverInfo, messageText);
        this.pictureParts = pictureParts;
    }

    public Map<Integer, List<Dot>> getPictureParts() {
        return pictureParts;
    }
}

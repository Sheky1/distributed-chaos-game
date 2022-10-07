package servent.message;

import app.Dot;

import java.util.List;
import java.util.Map;

public class NewSetupMessage extends BasicMessage{
    private final Map<Integer, List<Dot>> colleagues;


    public NewSetupMessage(int senderPort, int receiverPort,Map<Integer, List<Dot>> colleagues) {
        super(MessageType.NEW_SETUP, senderPort, receiverPort);
        this.colleagues = colleagues;
    }

    public Map<Integer, List<Dot>> getColleagues() {
        return colleagues;
    }
}

package servent.message;

import app.Dot;

import java.util.ArrayList;
import java.util.List;

public class SendResultMessage extends BasicMessage {

    private final List<Dot> dots;

    public SendResultMessage(int senderPort, int receiverPort, List<Dot> currentDots) {
        super(MessageType.SEND_RESULT, senderPort, receiverPort);
//        this.dots = currentDots;
        this.dots = new ArrayList<>();
        int end = Math.min(10000, currentDots.size());
        for (int i = 0; i < end; i++){
            this.dots.add(new Dot(currentDots.get(i).getX(), currentDots.get(i).getY()));
        }
//        for(int i = currentDots.size(); i > (currentDots.size() - 10000); i--) {
//            this.dots.add(new Dot(currentDots.get(i).getX(), currentDots.get(i).getY()));
//        }
    }

    public List<Dot> getDots() {
        return dots;
    }
}

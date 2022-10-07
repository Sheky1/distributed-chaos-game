package servent.message;

import app.AppConfig;
import app.Dot;

import java.util.ArrayList;
import java.util.List;

public class BackupMessage extends BasicMessage {
    private final List<Dot> dots;

    public BackupMessage(int senderPort, int receiverPort, List<Dot> dots) {
        super(MessageType.BACKUP, senderPort, receiverPort);
        AppConfig.timestampedStandardPrint("Dot number: " + dots.size());
//        this.dots = dots;
        this.dots = new ArrayList<>();
        int end = Math.min(dots.size(), 10000);
        for (int i = 0; i < end; i++) {
            this.dots.add(new Dot(dots.get(i).getX(), dots.get(i).getY()));
        }
    }

    public List<Dot> getDots() {
        return dots;
    }
}

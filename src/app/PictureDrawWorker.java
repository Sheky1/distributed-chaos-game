package app;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class PictureDrawWorker implements Runnable, Cancellable {

    private volatile boolean working = true;
    private final AtomicBoolean drawing = new AtomicBoolean(false);

    @Override
    public void run() {
        while (working) {
            while (!drawing.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!working) {
                    return;
                }
            }
            Random random = new Random();
            Picture myPic = AppConfig.getMyPicture();

            if (AppConfig.myDots.isEmpty()) {
                AppConfig.myDots.add(new Dot(random.nextInt(myPic.getWidth()), random.nextInt(myPic.getHeight())));
                AppConfig.timestampedStandardPrint("My frame is: " + AppConfig.myFrame);
                AppConfig.timestampedStandardPrint("Started drawing a new picture. Added a random first point at " + AppConfig.myDots.get(0) + ".");
            }


            AppConfig.dotCounter++;

            if(AppConfig.dotCounter % 100 == 0) {
                Dot edgeDot = AppConfig.myFrame.get(random.nextInt(AppConfig.myFrame.size()));
                AppConfig.myDots.add(new Dot((int) ((edgeDot.getX() + AppConfig.myDots.get(AppConfig.myDots.size() - 1).getX()) * myPic.getProportion()), (int) ((edgeDot.getY() + AppConfig.myDots.get(AppConfig.myDots.size() - 1).getY()) * myPic.getProportion())));
            }

//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
    }

    @Override
    public void stop() {
        working = false;
    }

    public void startDrawing() {
        boolean oldValue = this.drawing.getAndSet(true);

        if (oldValue) {
            AppConfig.timestampedErrorPrint("Tried to start drawing while already drawing.");
        }
    }

    public void stopDrawing() {
        this.drawing.set(false);
        AppConfig.timestampedStandardPrint("Stopped drawing.");
    }
}

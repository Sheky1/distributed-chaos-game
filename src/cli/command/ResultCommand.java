package cli.command;

import app.AppConfig;
import app.Dot;
import app.ServentInfo;
import servent.message.RequestResultMessage;
import servent.message.StopDrawingMessage;
import servent.message.util.MessageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResultCommand implements CLICommand {
    @Override
    public String commandName() {
        return "result";
    }

    @Override
    public void execute(String args) {
        try {
            for (Integer colleague : AppConfig.myColleagues.keySet()) {
                if (colleague != AppConfig.myServentInfo.getListenerPort())
                    MessageUtil.sendMessage(new RequestResultMessage(AppConfig.myServentInfo.getListenerPort(), colleague));
            }
            while (AppConfig.myColleagues.size() != (AppConfig.resultsGot + 1)) {
                AppConfig.timestampedStandardPrint("Waiting: results got - " + AppConfig.resultsGot + ", my colleagues: " + AppConfig.myColleagues);
                Thread.sleep(1000);
            }
            if (!AppConfig.brokenNodes.isEmpty()) {
                for (Integer brokenPort : AppConfig.brokenNodes) {
                    if (AppConfig.backups.containsKey(brokenPort)) {
                        AppConfig.timestampedStandardPrint("Adding backup messages from broken port: " + brokenPort + ". Amount of dots: " + AppConfig.backups.get(brokenPort).size());
                        AppConfig.allDots.addAll(AppConfig.backups.get(brokenPort));
                    }
                }
            }
            AppConfig.timestampedStandardPrint("Drawing the result.");
//            AppConfig.allDots.addAll(AppConfig.myDots);

            int end = Math.min(10000, AppConfig.myDots.size());
            for (int i = 0; i < end; i++){
                AppConfig.allDots.add(new Dot(AppConfig.myDots.get(i).getX(), AppConfig.myDots.get(i).getY()));
            }
            AppConfig.timestampedStandardPrint("Colleagues " + AppConfig.myColleagues);
            AppConfig.timestampedStandardPrint("NUMB " + AppConfig.allDots.size());

            final BufferedImage image = new BufferedImage(AppConfig.getMyPicture().getWidth(), AppConfig.getMyPicture().getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D graphics2D = image.createGraphics();
            graphics2D.setPaint(Color.WHITE);
            for (Dot dot : AppConfig.allDots) {
                graphics2D.drawLine(dot.getX(), dot.getY(), dot.getX(), dot.getY());
            }
            graphics2D.dispose();
            ImageIO.write(image, "png", new File(AppConfig.myPictureName + ".png"));
        } catch (Exception e) {
            AppConfig.timestampedErrorPrint("Invalid args for getting results for a job.");
        }
    }
}

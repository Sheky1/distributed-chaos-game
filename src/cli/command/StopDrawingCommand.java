package cli.command;

import app.AppConfig;
import app.PictureDrawWorker;
import servent.message.StopDrawingMessage;
import servent.message.util.MessageUtil;

public class StopDrawingCommand implements CLICommand{

    private final PictureDrawWorker pictureDrawWorker;

    public StopDrawingCommand(PictureDrawWorker pictureDrawWorker) {
        this.pictureDrawWorker = pictureDrawWorker;
    }

    @Override
    public String commandName() {
        return "stop_drawing";
    }

    @Override
    public void execute(String args) {
        try {
            if (!AppConfig.myPictureName.equals(args)) {
                AppConfig.timestampedErrorPrint("Picture name not recognized.");
                return;
            }
            for (Integer colleague : AppConfig.myColleagues.keySet()) {
                if(colleague != AppConfig.myServentInfo.getListenerPort())  MessageUtil.sendMessage(new StopDrawingMessage(AppConfig.myServentInfo.getListenerPort(), colleague));
            }
            pictureDrawWorker.stopDrawing();
        } catch (Exception e) {
            AppConfig.timestampedErrorPrint("Invalid args for stopping a job.");
        }
    }
}

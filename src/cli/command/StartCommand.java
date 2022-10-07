package cli.command;

import app.AppConfig;
import app.PictureDrawWorker;
import app.ServentInfo;
import servent.message.RequestResultMessage;
import servent.message.StartDrawingMessage;
import servent.message.util.MessageUtil;

public class StartCommand implements CLICommand {

    private final PictureDrawWorker pictureDrawWorker;

    public StartCommand(PictureDrawWorker pictureDrawWorker) {
        this.pictureDrawWorker = pictureDrawWorker;
    }

    @Override
    public String commandName() {
        return "start";
    }

    @Override
    public void execute(String args) {
        try {
            if (!AppConfig.myPictureName.equals(args)) {
                AppConfig.timestampedErrorPrint("Picture name not recognized.");
                return;
            }
            for (Integer colleague : AppConfig.myColleagues.keySet()) {
                if(colleague != AppConfig.myServentInfo.getListenerPort()) MessageUtil.sendMessage(new StartDrawingMessage(AppConfig.myServentInfo.getListenerPort(), colleague));
            }
            pictureDrawWorker.startDrawing();
        } catch (Exception e) {
            AppConfig.timestampedErrorPrint("Invalid args for starting a job.");
        }
    }
}

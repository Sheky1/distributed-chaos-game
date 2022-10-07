package cli.command;

import app.AppConfig;
import app.PictureDrawWorker;
import app.PingPongWorker;
import cli.CLIParser;
import servent.SimpleServentListener;

public class StopCommand implements CLICommand {

	private final CLIParser parser;
	private final SimpleServentListener listener;
	private final PictureDrawWorker pictureDrawWorker;
	private final PingPongWorker pingPongWorker;
	
	public StopCommand(CLIParser parser, SimpleServentListener listener, PictureDrawWorker pictureDrawWorker, PingPongWorker pingPongWorker) {
		this.parser = parser;
		this.listener = listener;
		this.pictureDrawWorker = pictureDrawWorker;
		this.pingPongWorker = pingPongWorker;
	}
	
	@Override
	public String commandName() {
		return "stop";
	}

	@Override
	public void execute(String args) {
		AppConfig.timestampedStandardPrint("Stopping...");
		parser.stop();
		listener.stop();
		pictureDrawWorker.stop();
		pingPongWorker.stop();
	}

}

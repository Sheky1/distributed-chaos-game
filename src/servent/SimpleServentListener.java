package servent;

import app.AppConfig;
import app.Cancellable;
import app.PictureDrawWorker;
import servent.handler.*;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable, Cancellable {

	private volatile boolean working = true;
	private final PictureDrawWorker pictureDrawWorker;

	public SimpleServentListener(PictureDrawWorker pictureDrawWorker) {
		this.pictureDrawWorker = pictureDrawWorker;
	}

	/*
	 * Thread pool for executing the handlers. Each client will get it's own handler thread.
	 */
	private final ExecutorService threadPool = Executors.newWorkStealingPool();

	@Override
	public void run() {
		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
			/*
			 * If there is no connection after 1s, wake up and see if we should terminate.
			 */
			listenerSocket.setSoTimeout(1000);
		} catch (IOException e) {
			AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
			System.exit(0);
		}


		while (working) {
			try {
				Message clientMessage;

				Socket clientSocket = listenerSocket.accept();

				//GOT A MESSAGE! <3
				clientMessage = MessageUtil.readMessage(clientSocket);

				MessageHandler messageHandler = new NullHandler(clientMessage);

				/*
				 * Each message type has it's own handler.
				 * If we can get away with stateless handlers, we will,
				 * because that way is much simpler and less error prone.
				 */
				switch (clientMessage.getMessageType()) {
					case NEW_NODE -> messageHandler = new NewNodeHandler(clientMessage);
					case WELCOME -> messageHandler = new WelcomeHandler(clientMessage);
					case START_DRAWING -> messageHandler = new StartDrawingHandler(clientMessage, pictureDrawWorker);
					case STOP_DRAWING -> messageHandler = new StopDrawingHandler(clientMessage, pictureDrawWorker);
					case REQUEST_RESULT -> messageHandler = new RequestResultHandler(clientMessage);
					case SEND_RESULT -> messageHandler = new SendResultHandler(clientMessage);
					case PING -> messageHandler = new PingHandler(clientMessage);
					case PONG -> messageHandler = new PongHandler(clientMessage);
					case SUSPICIOUS_PING -> messageHandler = new SuspiciousPingHandler(clientMessage);
					case SUSPICIOUS_PONG -> messageHandler = new SuspiciousPongHandler(clientMessage);
					case SUSPICIOUS_PORT -> messageHandler = new SuspiciousPortHandler(clientMessage);
					case SUSPICIOUS_PORT_CHECKED -> messageHandler = new SuspiciousPortCheckedHandler(clientMessage, pictureDrawWorker);
					case NODE_BROKEN -> messageHandler = new NodeBrokenHandler(clientMessage);
					case NEW_SETUP -> messageHandler = new NewSetupHandler(clientMessage);
					case BACKUP -> messageHandler = new BackupHandler(clientMessage);
				}

				threadPool.submit(messageHandler);
			} catch (SocketTimeoutException timeoutEx) {
				//Uncomment the next line to see that we are waking up every second.
//				AppConfig.timedStandardPrint("Waiting...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.working = false;
	}

}

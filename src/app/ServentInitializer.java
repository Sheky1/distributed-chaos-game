package app;

import servent.message.NewNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ServentInitializer implements Runnable {

    private int getSomeServentPort() {
        int bsPort = AppConfig.BOOTSTRAP_PORT;

        int retVal = -2;

        try {
            Socket bsSocket = new Socket("localhost", bsPort);

            PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
            bsWriter.write("Hail\n" + AppConfig.myServentInfo.getListenerPort() + "\n");
            bsWriter.flush();

            Scanner bsScanner = new Scanner(bsSocket.getInputStream());
            retVal = bsScanner.nextInt();

            bsSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public void run() {
        int someServentPort = getSomeServentPort();

        if (someServentPort == -2) {
            AppConfig.timestampedErrorPrint("Error in contacting bootstrap. Exiting...");
            System.exit(0);
        }
        if (someServentPort == -1) { //bootstrap gave us -1 -> we are first
            AppConfig.timestampedStandardPrint("First node in the system.");
            AppConfig.myPictureName = AppConfig.pictures.get(0).getName();
            AppConfig.fractalId = "0";
            AppConfig.myFrame = AppConfig.getMyPicture().getStartingDots();
        } else { //bootstrap gave us something else - let that node tell our successor that we are here
            NewNodeMessage nnm = new NewNodeMessage(AppConfig.myServentInfo.getListenerPort(), someServentPort, String.valueOf(AppConfig.myServentInfo.getListenerPort()), new HashMap<>());
            MessageUtil.sendMessage(nnm);
        }
    }

}

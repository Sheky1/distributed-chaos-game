package app;

import servent.message.BackupMessage;
import servent.message.PingMessage;
import servent.message.SuspiciousPortMessage;
import servent.message.util.MessageUtil;

import java.util.List;

public class PingPongWorker implements Runnable, Cancellable {

    private volatile boolean working = true;

    @Override
    public void run() {
        while (working) {

            List<Integer> allPorts = AppConfig.serventInfoList.stream().map(ServentInfo::getListenerPort).filter(listenerPort -> (!AppConfig.suspiciousPorts.contains(listenerPort)) && (AppConfig.myServentInfo.getListenerPort() != listenerPort) && (!AppConfig.brokenNodes.contains(listenerPort))).toList();
            AppConfig.timestampedStandardPrint("Sending ping to all unsuspicious ports, the suspicious are:  " + AppConfig.suspiciousPorts);
            for (Integer port : allPorts) {
                MessageUtil.sendMessage(new PingMessage(AppConfig.myServentInfo.getListenerPort(), port));
            }
            AppConfig.pingCounter++;
            if(AppConfig.pingCounter == 1) {
                AppConfig.pingCounter = 0;
                if(AppConfig.myDots.size() > 10000) {
                    for (Integer port : allPorts) {
                        MessageUtil.sendMessage(new BackupMessage(AppConfig.myServentInfo.getListenerPort(), port, AppConfig.myDots));
                    }
                }
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AppConfig.timestampedStandardPrint("Expected unsuspicious ports: " + allPorts + " and I got pongs from: " + AppConfig.pongResponses);

            for (Integer port : allPorts) {
                if (!AppConfig.pongResponses.contains(port)) AppConfig.suspiciousPorts.add(port);
            }

            int unsuspiciousPort = allPorts.stream().filter(port -> !AppConfig.suspiciousPorts.contains(port)).findFirst().orElse(-1);

            if(unsuspiciousPort != -1) {
                AppConfig.timestampedStandardPrint("Sending SuspiciousPortMessages to " + unsuspiciousPort + ", the suspicious ports are: " + AppConfig.suspiciousPorts);
                for (Integer suspiciousPort : AppConfig.suspiciousPorts) {
                    MessageUtil.sendMessage(new SuspiciousPortMessage(AppConfig.myServentInfo.getListenerPort(), unsuspiciousPort, String.valueOf(suspiciousPort)));
                }
            }

            AppConfig.pongResponses.clear();

        }
    }

    @Override
    public void stop() {
        working = false;
    }

}

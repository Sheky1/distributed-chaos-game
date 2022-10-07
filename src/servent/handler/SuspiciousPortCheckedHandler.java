package servent.handler;

import app.AppConfig;
import app.Dot;
import app.PictureDrawWorker;
import app.ServentInfo;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.*;

public class SuspiciousPortCheckedHandler implements MessageHandler {

    private final Message clientMessage;
    private final PictureDrawWorker pictureDrawWorker;

    public SuspiciousPortCheckedHandler(Message clientMessage, PictureDrawWorker pictureDrawWorker) {
        this.clientMessage = clientMessage;
        this.pictureDrawWorker = pictureDrawWorker;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SUSPICIOUS_PORT_CHECKED) {
            SuspiciousPortCheckedMessage suspiciousPortCheckedMessage = (SuspiciousPortCheckedMessage) clientMessage;
            if (suspiciousPortCheckedMessage.isStillSuspicious() && AppConfig.myServentInfo.getListenerPort() == 1100) {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (AppConfig.suspiciousPorts.contains(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText()))) {
                    AppConfig.timestampedStandardPrint("Node with port: " + suspiciousPortCheckedMessage.getMessageText() + " is broken.");
                    List<Integer> allPorts = AppConfig.serventInfoList.stream().map(ServentInfo::getListenerPort).filter(listenerPort -> (!AppConfig.suspiciousPorts.contains(listenerPort)) && (AppConfig.myServentInfo.getListenerPort() != listenerPort)).toList();
                    for (Integer port : allPorts) {
                        MessageUtil.sendMessage(new NodeBrokenMessage(AppConfig.myServentInfo.getListenerPort(), port, suspiciousPortCheckedMessage.getMessageText()));
                    }
                    if (!AppConfig.brokenNodes.contains(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText()))) {
                        AppConfig.brokenNodes.add(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText()));
                        AppConfig.suspiciousPorts.remove(Integer.valueOf(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText())));
                        AppConfig.serventInfoList.remove(new ServentInfo("127.0.0.1", Integer.parseInt(suspiciousPortCheckedMessage.getMessageText())));
                        if ((AppConfig.myServentInfo.getListenerPort() == 1100)) {

                            try {
                                // Reogranizuj sistem
                                AppConfig.timestampedStandardPrint("Stopping the system.");
                                for (Integer colleague : AppConfig.myColleagues.keySet()) {
                                    if (colleague != AppConfig.myServentInfo.getListenerPort() && !AppConfig.brokenNodes.contains(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText()))) {
                                        MessageUtil.sendMessage(new StopDrawingMessage(AppConfig.myServentInfo.getListenerPort(), colleague));
                                    }
                                }
                                pictureDrawWorker.stopDrawing();

                                AppConfig.timestampedStandardPrint("My colleagues before: " + AppConfig.myColleagues);
                                AppConfig.timestampedStandardPrint("My frame before: " + AppConfig.myFrame);
                                AppConfig.myColleagues.clear();
                                AppConfig.colleaguePorts.clear();
                                AppConfig.myFrame.clear();
                                AppConfig.nextColleagueToSplit = 0;
                                for (int servent = 0; servent < AppConfig.serventInfoList.size(); servent++) {
                                    Map<Integer, List<Dot>> pictureParts = new HashMap<>();
                                    if (servent == AppConfig.pictures.get(0).getStartingDots().size()) {
                                        for (int i = 0; i < servent; i++) {
                                            List<Dot> picturePart = new ArrayList<>();
                                            List<Dot> sDots = AppConfig.getMyPicture().getStartingDots();
                                            double prop = AppConfig.getMyPicture().getProportion();
                                            for (int j = 0; j < servent; j++) {
                                                if (i == j)
                                                    picturePart.add(new Dot(sDots.get(i).getX(), sDots.get(i).getY()));
                                                else
                                                    picturePart.add(new Dot(sDots.get(i).getX() == sDots.get(j).getX() ?
                                                            sDots.get(i).getX() :
                                                            (int) ((sDots.get(i).getX() + sDots.get(j).getX()) * prop),
                                                            sDots.get(i).getY() == sDots.get(j).getY() ?
                                                                    sDots.get(i).getY() :
                                                                    (int) ((sDots.get(i).getY() + sDots.get(j).getY()) * prop)));
                                            }
                                            pictureParts.put(AppConfig.serventInfoList.get(i).getListenerPort(), picturePart);
                                        }
                                    } else if ((servent > AppConfig.pictures.get(0).getStartingDots().size()) &&
                                            ((servent - AppConfig.pictures.get(0).getStartingDots().size()) % (AppConfig.pictures.get(0).getStartingDots().size() - 1)) == 0) {
                                        List<Integer> updatingColleagues = new ArrayList<>();
                                        updatingColleagues.add(AppConfig.colleaguePorts.get(AppConfig.nextColleagueToSplit));
                                        AppConfig.nextColleagueToSplit += 1;
                                        for (int i = (servent - AppConfig.pictures.get(0).getStartingDots().size()) + 1; i < servent; i++) {
                                            updatingColleagues.add(AppConfig.serventInfoList.get(i).getListenerPort());
                                        }
                                        List<Dot> sDots = AppConfig.myColleagues.get(updatingColleagues.get(0));
                                        double prop = AppConfig.getMyPicture().getProportion();
                                        for (int i = 0; i < sDots.size(); i++) {
                                            List<Dot> picturePart = new ArrayList<>();
                                            for (int j = 0; j < sDots.size(); j++) {
                                                if (i == j)
                                                    picturePart.add(new Dot(sDots.get(i).getX(), sDots.get(i).getY()));
                                                else
                                                    picturePart.add(new Dot(sDots.get(i).getX() == sDots.get(j).getX() ?
                                                            sDots.get(i).getX() :
                                                            (int) ((sDots.get(i).getX() + sDots.get(j).getX()) * prop),
                                                            sDots.get(i).getY() == sDots.get(j).getY() ?
                                                                    sDots.get(i).getY() :
                                                                    (int) ((sDots.get(i).getY() + sDots.get(j).getY()) * prop)));
                                            }
                                            pictureParts.put(updatingColleagues.get(i), picturePart);
                                        }
                                    }
                                    AppConfig.timestampedStandardPrint("All picture parts are :" + pictureParts);
                                    if (!pictureParts.isEmpty()) {
                                        AppConfig.myColleagues.putAll(pictureParts);
                                        AppConfig.colleaguePorts.addAll(pictureParts.keySet());
                                        if (pictureParts.containsKey(AppConfig.myServentInfo.getListenerPort()))
                                            AppConfig.myFrame = pictureParts.get(AppConfig.myServentInfo.getListenerPort());
                                    }
                                }
                                AppConfig.timestampedStandardPrint("My colleagues after: " + AppConfig.myColleagues);
                                AppConfig.timestampedStandardPrint("My frame after: " + AppConfig.myFrame);
                                for (Integer port : AppConfig.myColleagues.keySet()) {
                                    MessageUtil.sendMessage(new NewSetupMessage(AppConfig.myServentInfo.getListenerPort(), port, AppConfig.myColleagues));
                                }
                                Thread.sleep(1500);
                                for (Integer port : AppConfig.myColleagues.keySet()) {
                                    MessageUtil.sendMessage(new StartDrawingMessage(AppConfig.myServentInfo.getListenerPort(), port));
                                }
                            } catch (Exception e) {
                                AppConfig.timestampedErrorPrint("error" + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    if (AppConfig.brokenNodes.contains(Integer.parseInt(suspiciousPortCheckedMessage.getMessageText())))
                        AppConfig.timestampedStandardPrint("Node with port: " + suspiciousPortCheckedMessage.getMessageText() + " answered after being suspicious.");
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("WELCOME handler got something that is not a welcome message.");
        }
    }
}

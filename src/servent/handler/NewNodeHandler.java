package servent.handler;

import app.AppConfig;
import app.Dot;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NewNodeMessage;
import servent.message.WelcomeMessage;
import servent.message.util.MessageUtil;

import java.util.*;

public class NewNodeHandler implements MessageHandler {

    private final Message clientMessage;

    public NewNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.NEW_NODE) {
            int newNodePort = Integer.parseInt(clientMessage.getMessageText());
            ServentInfo newNodeInfo = new ServentInfo("127.0.0.1", newNodePort);
            Map<Integer, List<Dot>> pictureParts = new HashMap<>();
            AppConfig.serventInfoList.add(newNodeInfo);

            if (clientMessage.getSenderPort() == Integer.parseInt(clientMessage.getMessageText())) {
                if (AppConfig.serventInfoList.size() == AppConfig.pictures.get(0).getStartingDots().size()) {
//                    for (int j = 0; j < AppConfig.serventInfoList.size(); j++) {
//                        if (i == j)
//                            picturePart.add(new Dot(AppConfig.getMyPicture().getStartingDots().get(i).getX(), AppConfig.getMyPicture().getStartingDots().get(i).getY()));
//                        else
//                            picturePart.add(new Dot((AppConfig.getMyPicture().getStartingDots().get(i).getX() + AppConfig.getMyPicture().getStartingDots().get(j).getX()) / 2,
//                                    (AppConfig.getMyPicture().getStartingDots().get(i).getY() + AppConfig.getMyPicture().getStartingDots().get(j).getY()) / 2));
//                    }
                    List<Dot> sDots = AppConfig.getMyPicture().getStartingDots();
                    double prop = AppConfig.getMyPicture().getProportion();
                    for (int i = 0; i < AppConfig.serventInfoList.size(); i++) {
                        List<Dot> picturePart = new ArrayList<>();
                        for (int j = 0; j < AppConfig.serventInfoList.size(); j++) {
                            if (i == j)
                                picturePart.add(new Dot(sDots.get(i).getX(), sDots.get(i).getY()));
                            else
                                if(sDots.size() > 3) {
                                    picturePart.add(new Dot(sDots.get(i).getX() == sDots.get(j).getX() ?
                                            sDots.get(i).getX() :
                                            Math.abs((int) (sDots.get(i).getX() - (sDots.get(i).getX() + sDots.get(j).getX()) * prop)),
                                            sDots.get(i).getY() == sDots.get(j).getY() ?
                                                    sDots.get(i).getY() :
                                                    Math.abs((int) (sDots.get(i).getY() -((sDots.get(i).getY() + sDots.get(j).getY()) * prop)))));
                                } else {
                                    picturePart.add(new Dot(sDots.get(i).getX() == sDots.get(j).getX() ?
                                            sDots.get(i).getX() :
                                            (int) ((sDots.get(i).getX() + sDots.get(j).getX()) * prop),
                                            sDots.get(i).getY() == sDots.get(j).getY() ?
                                                    sDots.get(i).getY() :
                                                    (int) ((sDots.get(i).getY() + sDots.get(j).getY()) * prop)));
                                }
                        }
                        pictureParts.put(AppConfig.serventInfoList.get(i).getListenerPort(), picturePart);
                    }
                } else if ((AppConfig.serventInfoList.size() > AppConfig.pictures.get(0).getStartingDots().size()) &&
                        ((AppConfig.serventInfoList.size() - AppConfig.pictures.get(0).getStartingDots().size()) % (AppConfig.pictures.get(0).getStartingDots().size() - 1)) == 0) {
                    List<Integer> updatingColleagues = new ArrayList<>();
                    updatingColleagues.add(AppConfig.colleaguePorts.get(AppConfig.nextColleagueToSplit));
                    AppConfig.nextColleagueToSplit += 1;
                    for (int i = (AppConfig.serventInfoList.size() - AppConfig.pictures.get(0).getStartingDots().size()) + 1; i < AppConfig.serventInfoList.size(); i++) {
                        updatingColleagues.add(AppConfig.serventInfoList.get(i).getListenerPort());
                    }
                    List<Dot> sDots = AppConfig.myColleagues.get(updatingColleagues.get(0));
                    double prop = AppConfig.getMyPicture().getProportion();
                    for (int i = 0; i < sDots.size(); i++) {
                        List<Dot> picturePart = new ArrayList<>();
                        for (int j = 0; j < sDots.size(); j++) {
                            if (i == j)
                                picturePart.add(new Dot(sDots.get(i).getX(), sDots.get(i).getY()));
                            else {
                                picturePart.add(new Dot(sDots.get(i).getX() == sDots.get(j).getX() ?
                                        sDots.get(i).getX() :
                                        (int) ((sDots.get(i).getX() + sDots.get(j).getX()) * prop),
                                        sDots.get(i).getY() == sDots.get(j).getY() ?
                                                sDots.get(i).getY() :
                                                (int) ((sDots.get(i).getY() + sDots.get(j).getY()) * prop)));
                            }
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
                    AppConfig.timestampedStandardPrint("Current collegak: " + AppConfig.myColleagues);
                }

                for (ServentInfo serventInfo : AppConfig.serventInfoList)
                    if (serventInfo.getListenerPort() != AppConfig.myServentInfo.getListenerPort())
                        MessageUtil.sendMessage(new NewNodeMessage(AppConfig.myServentInfo.getListenerPort(), serventInfo.getListenerPort(), clientMessage.getMessageText(), pictureParts));
            } else {
                NewNodeMessage newNodeMessage = (NewNodeMessage) clientMessage;
                if (!newNodeMessage.getPictureParts().isEmpty()) {
                    AppConfig.myPictureName = AppConfig.pictures.get(0).getName();
                    AppConfig.myColleagues.putAll(newNodeMessage.getPictureParts());
                    AppConfig.colleaguePorts.addAll(newNodeMessage.getPictureParts().keySet());
                    if (newNodeMessage.getPictureParts().containsKey(AppConfig.myServentInfo.getListenerPort()))
                        AppConfig.myFrame = newNodeMessage.getPictureParts().get(AppConfig.myServentInfo.getListenerPort());
                }
            }

            System.out.println("Added new node with port: " + clientMessage.getMessageText() + ". I currently have " + AppConfig.serventInfoList + " as neighbors.");
            MessageUtil.sendMessage(new WelcomeMessage(AppConfig.myServentInfo.getListenerPort(), Integer.parseInt(clientMessage.getMessageText()), AppConfig.myServentInfo.getIpAddress(), pictureParts));
        } else {
            AppConfig.timestampedErrorPrint("NEW_NODE handler got something that is not new node message.");
        }

    }

}

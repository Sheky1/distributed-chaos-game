package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class contains all the global application configuration stuff.
 * @author bmilojkovic
 *
 */

//Ovo je prazan projekat, koji moze da vam koristi kao dobra pocetna tacka za projekat :D
public class AppConfig {

	/**
	 * Convenience access for this servent's information
	 */
	public static ServentInfo myServentInfo;
	
	public static final List<ServentInfo> serventInfoList = new ArrayList<>();

	/**
	 * If this is true, the system is a clique - all nodes are each other's
	 * neighbors.
	 */
	public static int BOOTSTRAP_PORT;
	public static int SERVENT_COUNT;
	public static List<Picture> pictures = new ArrayList<>();
	public static int pictureCount = -1;
	public static String myPictureName = "";
	public static int resultsGot = 0;
	public static List<Dot> myDots = new ArrayList<>();
	public static List<Dot> allDots = new ArrayList<>();
	public static List<Dot> myFrame = new ArrayList<>();
	public static String fractalId = "";
	public static Map<Integer, List<Dot>> myColleagues = new HashMap<>();
	public static int nextColleagueToSplit = 0;
	public static List<Integer> colleaguePorts = new ArrayList<>();
	public static List<Integer> pongResponses = new ArrayList<>();
	public static List<Integer> suspiciousPorts = new ArrayList<>();
	public static List<Integer> suspiciousPingsSent = new ArrayList<>();
	public static List<Integer> brokenNodes = new ArrayList<>();
	public static int pingCounter = 0;
	public static Map<Integer, List<Dot>> backups = new HashMap<>();
	public static int dotCounter = 0;

	/**
	 * Print a message to stdout with a timestamp
	 * @param message message to print
	 */
	public static void timestampedStandardPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.out.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Print a message to stderr with a timestamp
	 * @param message message to print
	 */
	public static void timestampedErrorPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.err.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Reads a config file. Should be called once at start of app.
	 * The config file should be of the following format:
	 * <br/>
	 * <code><br/>
	 * servent_count=3 			- number of servents in the system <br/>
	 * clique=false 			- is it a clique or not <br/>
	 * fifo=false				- should sending be fifo
	 * servent0.port=1100 		- listener ports for each servent <br/>
	 * servent1.port=1200 <br/>
	 * servent2.port=1300 <br/>
	 * servent0.neighbors=1,2 	- if not a clique, who are the neighbors <br/>
	 * servent1.neighbors=0 <br/>
	 * servent2.neighbors=0 <br/>
	 * 
	 * </code>
	 * <br/>
	 * So in this case, we would have three servents, listening on ports:
	 * 1100, 1200, and 1300. This is not a clique, and:<br/>
	 * servent 0 sees servent 1 and 2<br/>
	 * servent 1 sees servent 0<br/>
	 * servent 2 sees servent 0<br/>
	 * 
	 * @param configName name of configuration file
	 */
	public static void readConfig(String configName, int serventId){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configName)));
			
		} catch (IOException e) {
			timestampedErrorPrint("Couldn't open properties file. Exiting...");
			System.exit(0);
		}

		try {
			BOOTSTRAP_PORT = Integer.parseInt(properties.getProperty("bs.port"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading bootstrap_port. Exiting...");
			System.exit(0);
		}

		try {
			SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading servent_count. Exiting...");
			System.exit(0);
		}

		String portProperty = "servent"+serventId+".port";

		int serventPort = -1;

		try {
			serventPort = Integer.parseInt(properties.getProperty(portProperty));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading " + portProperty + ". Exiting...");
			System.exit(0);
		}

		try {
			pictureCount = Integer.parseInt(properties.getProperty("picture_count"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading picture_count. Exiting...");
			System.exit(0);
		}

		for(int i = 0; i < pictureCount; i++) {
			String generalString = "picture" + i + ".";
			String name = properties.getProperty(generalString + "name");
			int dotCount = Integer.parseInt(properties.getProperty(generalString + "dot_count"));
			double proportion = Double.parseDouble(properties.getProperty(generalString + "proportion"));
			int width = Integer.parseInt(properties.getProperty(generalString + "width"));
			int height = Integer.parseInt(properties.getProperty(generalString + "height"));
			String[] startingDots = properties.getProperty(generalString + "startingDots").split(",");
			List<Dot> startingDotsList = new ArrayList<>();
			for(int j = 0; j < startingDots.length; j+=2) {
				startingDotsList.add(new Dot(Integer.parseInt(startingDots[j]), Integer.parseInt(startingDots[j+1])));
			}
			pictures.add(new Picture(name, dotCount, proportion, width, height, startingDotsList));
		}
		timestampedStandardPrint("Added all of the pictures to draw. " + pictures);

		myServentInfo = new ServentInfo("127.0.0.1", serventPort);
		serventInfoList.add(myServentInfo);
	}
	
	/**
	 * Get info for a servent selected by a given id.
	 * @param id id of servent to get info for
	 * @return {@link ServentInfo} object for this id
	 */
	public static ServentInfo getInfoById(int id) {
		if (id >= getServentCount()) {
			throw new IllegalArgumentException(
					"Trying to get info for servent " + id + " when there are " + getServentCount() + " servents.");
		}
		return serventInfoList.get(id);
	}
	
	/**
	 * Get number of servents in this system.
	 */
	public static int getServentCount() {
		return serventInfoList.size();
	}

	public static Picture getMyPicture() {
		return pictures.get(pictures.indexOf(new Picture(myPictureName, 0,0,0,0,new ArrayList<>())));
	}
	
}

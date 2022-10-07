package app;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is an immutable class that holds all the information for a servent.
 *
 * @author bmilojkovic
 */
public class ServentInfo implements Serializable {

	private static final long serialVersionUID = 5304170042791281555L;
	private final String ipAddress;
	private final int listenerPort;

	public ServentInfo(String ipAddress, int listenerPort) {
		this.ipAddress = ipAddress;
		this.listenerPort = listenerPort;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getListenerPort() {
		return listenerPort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServentInfo that = (ServentInfo) o;
		return listenerPort == that.listenerPort && ipAddress.equals(that.ipAddress);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, listenerPort);
	}

	@Override
	public String toString() {
		return "ServentInfo{" +
				"ipAddress='" + ipAddress + '\'' +
				", listenerPort=" + listenerPort +
				'}';
	}
}

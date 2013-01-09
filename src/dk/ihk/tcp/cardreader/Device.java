package dk.ihk.tcp.cardreader;

import dk.ihk.tcp.util.ShortBuffer;

/**
 * Interface representing a device.
 * 
 * @author Jacob Pedersen
 */
public interface Device
{
	/**
	 * Connects to the device with the given serial number
	 * 
	 * @param serialNumber A serial number
	 * @throws DeviceException If connection fails
	 */
	void connect(String serialNumber) throws DeviceException;

	/**
	 * Disconnect from the device
	 */
	void disconnect();

	/**
	 * @return True if this device is connected
	 */
	boolean isConnected();

	/**
	 * Waits for an interrupt from the device. This method blocks until data is
	 * ready.
	 * 
	 * @param buffer The buffer to place the received data in
	 * @return Number of bytes received
	 * @throws DeviceException If connection fails
	 */
	int waitForInterrupt(ShortBuffer buffer) throws DeviceException;

	/**
	 * Sends the given command to the device including the given data.
	 * 
	 * @param command The command number
	 * @param buffer The buffer containing the data
	 * @return Number of bytes sent
	 * @throws DeviceException If connection fails
	 */
	int send(int command, ShortBuffer buffer) throws DeviceException;

}

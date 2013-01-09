package dk.ihk.tcp.cardreader.usb;

/**
 * @author Jacob Pedersen
 */
public class TCPUSB
{
	public native int init();

	public native int connect(String serialNumber);

	public native int disconnect();

	public native int send(int command, short[] data);

	public native int interrupt(short[] data);

	static
	{
		System.loadLibrary("TCPUSB");
	}
}

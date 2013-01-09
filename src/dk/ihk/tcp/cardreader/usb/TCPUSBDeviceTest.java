package dk.ihk.tcp.cardreader.usb;

import dk.ihk.tcp.cardreader.Command;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.cardreader.KeepAlivePinger;
import dk.ihk.tcp.util.ShortBuffer;

public class TCPUSBDeviceTest
{
	public static void main(String[] args) throws DeviceException, InterruptedException
	{
		int failures = 0;
		TCPUSBDevice device = new TCPUSBDevice();
		KeepAlivePinger pinger = new KeepAlivePinger();

		Thread t = new Thread(pinger);
		t.start();

		device.connect("1");

		if (device.isConnected())
		{
			for (int i = 0; i < 100; i++)
			{
				System.out.println("Round " + (i + 1));

				ShortBuffer send = new ShortBuffer();
				ShortBuffer recv = new ShortBuffer();

				send.setInt8(0, 0x00);
				send.setInt8(1, 0x11);
				send.setInt8(2, 0x22);
				send.setInt8(3, 0x33);
				send.setInt8(4, 0x44);
				send.setInt8(5, 0x55);
				send.setInt8(6, 0x66);
				send.setInt8(7, 0x77);

				device.send(Command.ECHO.ordinal(), send);
				System.out.println("  Sent");

				device.waitForInterrupt(recv);
				System.out.println("  Received");

				if (recv.getInt8(0) != 0x00 && recv.getInt8(1) != 0x11 && recv.getInt8(2) != 0x22 && recv.getInt8(3) != 0x33
						&& recv.getInt8(4) != 0x44 && recv.getInt8(5) != 0x55 && recv.getInt8(6) != 0x66 && recv.getInt8(7) != 0x77)
				{
					System.out.println("   FAILED!");
					failures++;
				}
			}

			System.out.println("DONE - Echoed 100 x 8 byte - " + failures + " failures out of 100");
		}

		Thread.sleep(5000);
		pinger.stop();
		device.disconnect();
	}
}

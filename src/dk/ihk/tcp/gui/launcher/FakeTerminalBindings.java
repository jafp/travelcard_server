package dk.ihk.tcp.gui.launcher;

import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.terminal.DefaultBindings;

public class FakeTerminalBindings extends DefaultBindings
{
	@Override
	public void configure() throws Exception
	{
		super.configure();
		bind(Device.class).to(new FakeDevice());
	}
}

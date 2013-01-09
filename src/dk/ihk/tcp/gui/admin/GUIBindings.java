package dk.ihk.tcp.gui.admin;

import dk.ihk.tcp.util.inject.Bindings;

public class GUIBindings extends Bindings
{
	@Override
	public void configure() throws Exception
	{
		bind(CustomerListModel.class).to(CustomerListModel.class);
		bind(JourneyTableModel.class).to(JourneyTableModel.class);
	}
}

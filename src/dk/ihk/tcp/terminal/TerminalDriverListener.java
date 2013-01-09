package dk.ihk.tcp.terminal;

import dk.ihk.tcp.terminal.TerminalDriver.Mode;
import dk.ihk.tcp.terminal.TerminalDriver.State;

public interface TerminalDriverListener
{
	void onStateChange(State newState);

	void onModeChange(Mode newMode);

	void onCardScanned(long id);
}

package dk.ihk.tcp.zones;

public interface ZoneListener {
	void onChange(int newZone, int oldZone);
}

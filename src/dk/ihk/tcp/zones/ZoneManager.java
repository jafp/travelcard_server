package dk.ihk.tcp.zones;

public interface ZoneManager
{
	int getZone();

	ZoneMap getZoneMap();

	int getPrice(int start, int end);

	int getLength(int start, int end);

	void addListener(ZoneListener listener);

	void removeListener(ZoneListener listener);
}

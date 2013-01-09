package dk.ihk.tcp.zones;

public class ZonePrice {

	public static int getPrice(int lengthInZones) {
		int[] prices = { 0, 14, 14, 19, 24, 29, 34, 40, 45, 47 };
		return prices[lengthInZones];
	}
	
}

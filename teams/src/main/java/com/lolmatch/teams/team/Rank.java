package com.lolmatch.teams.team;

import lombok.Getter;

@Getter
public enum Rank {
	IRON_IV("Iron IV", 1),
	IRON_III("Iron III", 2),
	IRON_II("Iron II", 3),
	IRON_I("Iron I", 4),
	BRONZE_IV("Bronze IV", 5),
	BRONZE_III("Bronze III", 6),
	BRONZE_II("Bronze II", 7),
	BRONZE_I("Bronze I", 8),
	SILVER_IV("Silver IV", 9),
	SILVER_III("Silver III", 10),
	SILVER_II("Silver II", 11),
	SILVER_I("Silver I", 12),
	GOLD_IV("Gold IV", 13),
	GOLD_III("Gold III", 14),
	GOLD_II("Gold II", 15),
	GOLD_I("Gold I", 16),
	PLATINUM_IV("Platinum IV", 17),
	PLATINUM_III("Platinum III", 18),
	PLATINUM_II("Platinum II", 19),
	PLATINUM_I("Platinum I", 20),
	EMERALD_IV("Emerald IV", 21),
	EMERALD_III("Emerald III", 22),
	EMERALD_II("Emerald II", 23),
	EMERALD_I("Emerald I", 24),
	DIAMOND_IV("Diamond IV", 25),
	DIAMOND_III("Diamond III", 26),
	DIAMOND_II("Diamond II", 27),
	DIAMOND_I("Diamond I", 28),
	MASTER("Master", 29),
	CHALLENGER("Challenger", 30);
	
	private final String displayName;
	
	private final int number;
	
	Rank(String displayName, int i) {
		this.displayName = displayName;
		number = i;
	}
	
}


import core.Vector;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;

public class FlagUmfeld {
	//Constants
	private Vector longestDist = new Vector (Float.MAX_VALUE,Float.MAX_VALUE);
	//Nearest Flag Vector
	public Vector getNearestFlag(AiMapInfo map, AiPlayerInfo ownPlayer){
		Vector nextFlag = new Vector(0,0);
		Vector tempFlag = longestDist;
		for(int i = 0; i < map.getNumFlags(); i++){
			if(!(map.getFlags()[i].isOwner(ownPlayer))){
				nextFlag = map.getFlags()[i].getPosition().sub(ownPlayer.getPosition());
				if(nextFlag.length() < tempFlag.length()){
					tempFlag = nextFlag;
				}
			}
		}
		return tempFlag;
	}

	public int getConqueredFlags(AiMapInfo map, AiPlayerInfo ownPlayer){
		int flagsOwned = 0;
		for(int i = 0; i < map.getFlags().length; i++){
			if((map.getFlags()[i].isOwner(ownPlayer))){
				flagsOwned += 1;
			}
		}
		return flagsOwned;
	}
}

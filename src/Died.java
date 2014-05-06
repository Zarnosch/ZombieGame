import core.Vector;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;
import core.elements.Zombie;
import core.map.ZombieMap;

public class Died{
	public boolean died(AiMapInfo map, AiPlayerInfo ownPlayer){
		 float playerdistance = ownPlayer.getPosition().length();
		 if(playerdistance >= 94.5){
			 return true;
		 }
		 else return false;
	}
}

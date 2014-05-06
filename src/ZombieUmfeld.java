
import java.awt.List;

import core.Vector;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;

public class ZombieUmfeld{
	//Constants
	private static final Vector longestDist = new Vector (Float.MAX_VALUE,Float.MAX_VALUE);
	//Nearest Zombie Vector
	public Vector[] getNearestZombie(AiMapInfo map, AiPlayerInfo ownPlayer){
		Vector nextZombie = new Vector(0,0);
		Vector[] tempZombie = {longestDist,new Vector(0,0)};

		for(int i = 0; i < map.getZombies().length; i++){
			nextZombie = map.getZombies()[i].getPosition().sub(ownPlayer.getPosition());
			if(nextZombie.length() < tempZombie[0].length()){
				tempZombie[1] = tempZombie[0];
				tempZombie[0] = nextZombie;
			}
		}
		return tempZombie;
	}
}

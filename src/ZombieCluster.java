import core.Vector;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;


public class ZombieCluster {
	//Initialize
	Vector ZombieCluster = new Vector(0,0);
	
	public Vector getZombieCluster(AiMapInfo map, AiPlayerInfo ownPlayer){
		for(int i = 0; i < map.getZombies().length; i++){
			ZombieCluster = ZombieCluster.add(map.getZombies()[i].getPosition());
		}
		ZombieCluster = ZombieCluster.div(map.getZombies().length);
		return ZombieCluster;
	}
}

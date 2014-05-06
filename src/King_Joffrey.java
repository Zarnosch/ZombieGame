import core.Vector;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;
import core.player.PlayerController;
import core.map.*;

public class King_Joffrey extends PlayerController{
private int stepsAlive = 1;
	@Override
	public String getName() {
		return "King Joffrey";
	}
	
	@Override
	public String getAuthor() {
		return "Janos Zimmermann";
	}
	// Think batman
	@Override
	public Vector think(AiMapInfo map, AiPlayerInfo ownPlayer) {
		//Steps alive
		stepsAlive += 1;
		//Died
		boolean died = new Died().died(map, ownPlayer);
		// Own Position
		Vector ownPosition = ownPlayer.getPosition();
		// Next Flag search
		int conqueredFlags = new FlagUmfeld().getConqueredFlags(map, ownPlayer);
		Vector nearestFlag = new Vector(0,0);
		if(conqueredFlags < map.getNumFlags()){
			nearestFlag = new FlagUmfeld().getNearestFlag(map, ownPlayer);
		}
		else nearestFlag = Vector.ZERO().add(ownPosition);
		// Set standart movement
		Vector movement = (Vector.ZERO().add(nearestFlag));
		// Nearest Zombie search
		Vector[] nearestZombies = new ZombieUmfeld().getNearestZombie(map, ownPlayer);
		// Posision of the CombieCluster
//		Vector zombieCluster = new ZombieCluster().getZombieCluster(map, ownPlayer);
//		System.out.println("ZombieCluster movement: " + movement);
//		movement = (Vector.ZERO().sub((zombieCluster).div(7)).add(nearestFlag).mult(700));
		// Movement reaction to near Zombies
		if((map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+3).length) > 0){
			movement = (Vector.ZERO().sub(nearestZombies[0])).add((nearestFlag).div(3));
		}
		// Movement if Zombies got triggered
		if((map.getZombiesInRadius(ownPlayer.getPosition(), ownPlayer.getCurrentNoiseRadius()-1).length) > 0){
			movement = (Vector.ZERO().sub(nearestZombies[0])).add((nearestFlag).div(4));
		}
//		if((map.getZombiesInRadius(ownPlayer.getPosition(), ownPlayer.getCurrentNoiseRadius()+3).length) > 1)
//			movement = (nearestZombies[0].sub(nearestZombies[1]));
//			movement = new Vector(movement.y,movement.x);
		if(died){
			stepsAlive = 1;
		}
		
		// Bottle-throw reaction to near Zombies (Deaths)
		if(map.getPassedSteps() == 1){
			throwBottle((nearestFlag).mult(0.1f));
		}
		if(((stepsAlive * ownPlayer.getNumBottles() > 500) && 
				(map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+2).length) > 0) || 
				((map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+2).length) > 1) &&
				(stepsAlive * ownPlayer.getNumBottles() > 400 )){
			throwBottle((nearestZombies[0].mult(1.3f)).add(ownPosition.mult(1f)));
		}
		movement.normalize();
		movement.mult(10);
		movement.normalize();
		return movement;
	}
}

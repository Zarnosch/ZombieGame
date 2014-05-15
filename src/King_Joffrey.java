import core.Vector;
import core.ai.AiFlagInfo;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;
import core.player.PlayerController;
import core.map.*;
import debug.DebugFlags;

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
		long time1 = System.nanoTime();
		debugInfo.clearCircles();
		//Steps alive
		stepsAlive += 1;
		//Died
		boolean died = new Died().died(map, ownPlayer);
		// Nearest 2 Zombie search
		Vector[] nearestZombies = new ZombieUmfeld().getNearestZombie(map, ownPlayer);
		// Own Position
		Vector ownPosition = ownPlayer.getPosition();
		int conqueredFlags = new FlagUmfeld().getConqueredFlags(map, ownPlayer);
		// Next 2 Flags search
		Vector[] nearestFlags = {Vector.ZERO().add(ownPosition), Vector.ZERO()};
//		Vector[] allFlags = new FlagUmfeld().getFlagArray(map);
		AiFlagInfo[] nearestFlags_ = new FlagUmfeld().sortAllFlags(map, ownPlayer); // Sort Flaggs
		nearestFlags = new FlagUmfeld().AiFlagInfoToVector(nearestFlags_); //Parse Flaginfo to Array
		if(conqueredFlags < map.getNumFlags() ){
//			nearestFlags = new FlagUmfeld().getNearestFlag(map, ownPlayer); //Old
			nearestFlags[0] = nearestFlags_[0].getPosition();               //New
			nearestFlags[1] = nearestFlags_[1].getPosition();
			nearestFlags[2] = nearestFlags_[2].getPosition();
			if(map.getZombiesInRadius(nearestFlags[1], 15f).length -1 > map.getZombiesInRadius(nearestFlags[2], 15f).length ){
				Vector temp = nearestFlags[2];
				nearestFlags[1] = temp;
			}
			if(map.getZombiesInRadius(nearestFlags[0], 10f).length  > map.getZombiesInRadius(nearestFlags[1], 10f).length ){
				Vector temp = nearestFlags[1];
				nearestFlags[0] = temp;
			}
		}

		// Set standart movement
		Vector movement = (new Vector().ZERO().add(nearestFlags[0].sub(ownPosition)));
		// Movement reaction to near Zombies
		if((map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+3).length) > 0){
			movement = (Vector.ZERO().sub(nearestZombies[0])).add((nearestFlags[0]).div(3));
		}
		// Movement if Zombies got triggered
		if((map.getZombiesInRadius(ownPlayer.getPosition(), ownPlayer.getCurrentNoiseRadius()).length) > 0){
			movement = (Vector.ZERO().sub(nearestZombies[0])).add((nearestFlags[0]).div(5));
		}
		if((map.getZombiesInRadius(ownPlayer.getPosition(), ownPlayer.getCurrentNoiseRadius()+2).length) > 1){
			movement = (nearestZombies[0].sub(nearestZombies[1]));
			movement.normalize();
			movement = new Vector(movement.y,-movement.x); // Normale 1
			if(movement.mult(1.2f).sub(nearestZombies[0]).length() < movement.mult(-1.2f).sub(nearestZombies[0]).length()){
				movement = new Vector(-movement.y,movement.x); // Normale 2
			}
		}
		if(map.getNumFlags() == conqueredFlags && map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+3).length > 0){
			movement = (Vector.ZERO().sub(nearestZombies[0].x, -nearestZombies[0].y));
		}
	

		//Bottle-throw reaction to near Zombies
		if(map.getPassedSteps() == 25){
			throwBottle((nearestFlags[0].mult(1.5f)).add(ownPosition));
		}
		if(((stepsAlive * ownPlayer.getNumBottles() > 600) && 
				(map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+2).length) > 0) || 
				((map.getZombiesInRadius(ownPosition, ownPlayer.getCurrentNoiseRadius()+2).length) > 1) &&
				(stepsAlive * ownPlayer.getNumBottles() > 500 )){
			throwBottle((nearestZombies[0].mult(1.8f)).add(ownPosition));
		}
		if(died){
			stepsAlive = 1;
		}
//		if(map.getPassedSteps() <= 7){
//			movement = new Vector(0,1);
//		}
		//Debug an NanoTime Measurement
		debugInfo.addDebugCircle(ownPosition, ownPlayer.getCurrentNoiseRadius());
		debugInfo.getDebugCircles().add(new DebugCircle(nearestFlags[0], 5));
		debugInfo.getDebugCircles().add(new DebugCircle(nearestFlags[1], 5));
//		System.out.println(System.nanoTime()- time1);
		movement.normalize();
		if(ownPlayer.getNoiseRadius(movement) < nearestZombies[0].euclideanDistance(ownPosition)){
			movement.div(5);
		}
		//test
		return movement;
	}
}

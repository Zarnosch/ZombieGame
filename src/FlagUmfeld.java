
import core.Vector;
import core.ai.AiFlagInfo;
import core.ai.AiMapInfo;
import core.ai.AiPlayerInfo;
import core.elements.Flag;

public class FlagUmfeld{
	//Constants
	private Vector longestDist = new Vector (Float.MAX_VALUE,Float.MAX_VALUE);
	private Flag flagge1 = new Flag(new Vector(1000f, 1000f));
	private AiFlagInfo psydoFlag = new AiFlagInfo(flagge1);
	//Nearest Flag Vector (Half InsertionSort) "Wächter"-Element = longestDist Aufwnd: O(n) bzw O(2n) = 20 Vergleiche bei 10 Flaggen
	public Vector[] getNearestFlag(AiMapInfo map, AiPlayerInfo ownPlayer){
		Vector nextFlag = new Vector(0,0);
		Vector[] tempFlags = {longestDist, new Vector(0,0)};
		for(int i = 0; i < map.getNumFlags(); i++){
			if(!(map.getFlags()[i].isOwner(ownPlayer))){
				nextFlag = map.getFlags()[i].getPosition().sub(ownPlayer.getPosition());
				if(!(nextFlag.length() >= tempFlags[0].length())){
					tempFlags[1] = tempFlags[0];
					tempFlags[0] = nextFlag;
				}
			}
		}
		return tempFlags;
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

	public Vector[] getFlagArray(AiMapInfo map){
		Vector[] allFlags = new Vector[map.getNumFlags()];
		for(int i = 0; i < map.getNumFlags(); i++){
			allFlags[i] = map.getFlags()[i].getPosition();
		}
		return allFlags;
	}
	//InsertionSort O(n²/2) O(n) with already sorted Arrays, stable!
	public AiFlagInfo[] sortAllFlags(AiMapInfo map , AiPlayerInfo ownPlayer){
		AiFlagInfo[] flags = new AiFlagInfo[map.getNumFlags()];
		
		for(int k = 0; k < flags.length ; k++){
			if(!(map.getFlags()[k].isOwner(ownPlayer))){
				flags[k] = map.getFlags()[k];
			}
			else flags[k] = psydoFlag;
		}
		
		Vector ownPlayer_ = ownPlayer.getPosition();
		
		for(int i = 1; i < flags.length; i++){
			AiFlagInfo x = flags[i];
			int j;				
			for(j = i; j > 0 && flags[j-1].getPosition().sub(ownPlayer_).length() >= x.getPosition().sub(ownPlayer_).length() ;--j){
				flags[j] = flags[j-1];
			}
			flags[j] = x;
		}
		return flags;
	}
	public Vector[] AiFlagInfoToVector(AiFlagInfo flags[]){
		Vector[] tempVec = new Vector[flags.length];
		for(int i = 0; i < tempVec.length; i++){
			tempVec[i] = flags[i].getPosition();
		}
		return tempVec;
	}
}

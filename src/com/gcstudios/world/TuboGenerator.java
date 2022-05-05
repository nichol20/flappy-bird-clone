package com.gcstudios.world;

import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Tubo;
import com.gcstudios.main.Game;

public class TuboGenerator {
	
	public int targetTime = 60*3;
	public int time = targetTime-30;
	public int limit = 400;
	
	
	public void tick() {
		time++;
		if(time == targetTime) {
			int altura = - (Entity.rand.nextInt(200));
			
			Tubo tubo1 = new Tubo(Game.WIDTH, altura, 52, 270, 1, Game.spritesheet.getSprite(604, 0, 52, 270));
			Tubo tubo2 = new Tubo(Game.WIDTH, altura + limit, 52, 242, 1, Game.spritesheet.getSprite(660, 0, 52, 242));
			
			Game.entities.add(tubo1);
			Game.entities.add(tubo2);
			time = 0;
		}
	}
}

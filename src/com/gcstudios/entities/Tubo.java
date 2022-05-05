package com.gcstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.gcstudios.main.Game;

public class Tubo extends Entity{

	private int score = 0;
	
	public Tubo(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

	}

	public void tick() {
		x-=2;
		if(x+width <= Game.player.getX()) {
			score++;
			if(score == 1)
			Game.score += 0.5;

		}
		
		if(isColidding(this, Game.player)) {
			Game.game_state = "GAME_OVER";
		}
		
		if(x+width <= 0) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		if(sprite != null) {
			g.drawImage(sprite, this.getX(), this.getY(), width, height, null);
		}else {
			g.setColor(Color.green);
			g.fillRect(getX(), getY(), width, height);
		}
	}
}

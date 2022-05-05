package com.gcstudios.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.gcstudios.main.Game;



public class Player extends Entity{
	
	
	public boolean isPressed = false;

	public int up = 0;
	public int timeSprite = 0;
	public int maxTimeSprite = 5;
	public int changeSprite = 0;
	public BufferedImage[] player;
	
	private double gravity = 0.3;
	private double vspd = 0;
	
	
	public Player(int x, int y, int width, int height,double speed,BufferedImage sprite) {
		super(x, y, width, height,speed,sprite);
		
		player = new BufferedImage[3];
		player[0] = Game.spritesheet.getSprite(528, 128, 34, 24);
		player[1] = Game.spritesheet.getSprite(528, 180, 34, 24);
		player[2] = Game.spritesheet.getSprite(446, 248, 34, 24);
	}
	
	public void tick(){
		depth = 2;
		if(Game.game_state == "NORMAL") {
			vspd += gravity;
			if(isPressed) {
				vspd = -6;
				isPressed = false;
			}
			y += vspd;
			
			if(y < -2) {
				y = -2;
			}
			if(y > 425) {
				Game.game_state = "GAME_OVER";
			}
			
			for(int i = 0; i < Game.entities.size(); i++) {
				Entity e = Game.entities.get(i);
				if(e != this) {
					if(Entity.isColidding(this, e)) {
						
					}
				}
			}
			
			timeSprite++;
			if(timeSprite == maxTimeSprite) {
				timeSprite = 0;
				changeSprite++;
				if(changeSprite > 2) {
					changeSprite = 0;	
				}
				sprite = player[changeSprite];
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(Game.game_state == "NORMAL") {
			if(!isPressed) {
				up+=2;
				if(up > -10) y+=0.3;
				if(up > 60) {
					up = 60;
					y++;
				}
		
				g2.rotate(Math.toRadians(up), getX() + width / 2, getY() + height / 2);
				g2.drawImage(sprite, getX(), getY(), null);
				
				g2.rotate(Math.toRadians(-up), getX() + width / 2, getY() + height / 2);
			}else {
				up = -50;
				g2.rotate(Math.toRadians(up), getX() + width / 2, getY() + height / 2);
				g2.drawImage(sprite, getX(), getY(), null);  
				
				g2.rotate(Math.toRadians(-up), getX() + width / 2, getY() + height / 2);
				
			}
		}
		
	}


}

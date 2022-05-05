package com.gcstudios.graficos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;



public class Background {

	private int x,y;
	private int width,height;
	private int speed;
	private BufferedImage background;
	
	public Background(int x, int y, int width, int height,int speed, BufferedImage background) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.background = background;
	}
	
	public void tick() {
		x -= speed;
		if(x < -width) {
			x = 0;
		}
	}
	
	
	public void render(Graphics g) {
		g.drawImage(background, x, y, width, height, null);
		g.drawImage(background, x + width, y, width, height, null);
		g.drawImage(background, x + width * 2, y, width, height, null);
		g.drawImage(background, x + width * 3, y, width, height, null);
	} 
}

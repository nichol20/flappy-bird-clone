package com.gcstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;


import com.gcstudios.entities.Entity;
import com.gcstudios.entities.Player;
import com.gcstudios.graficos.Background;
import com.gcstudios.graficos.Spritesheet;
import com.gcstudios.graficos.UI;
import com.gcstudios.world.TuboGenerator;
import com.gcstudios.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 714;
	public static final int HEIGHT = 512;
	public static final int SCALE = 1;
	
	private BufferedImage image;
	

	public static List<Entity> entities;
	public static Spritesheet spritesheet;
	public static Player player;
	
	public static TuboGenerator tuboGenerator;
	public static Background background;
	public static Background grass;
	
	public static String game_state = "START";
	public boolean start = false;
	public int timerStart = 0;
	
	public UI ui;
	
	public static double score = 0;
	
	public Game(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		//Inicializando objetos.
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		background = new Background(0, 0, 288, 512, 1, spritesheet.getSprite(0, 0, 288, 512));
		grass = new Background(0, 425, 308, 112, 2, spritesheet.getSprite(292, 0, 308, 112));
		player = new Player(WIDTH/2 - 30,HEIGHT/2,34,24,2,spritesheet.getSprite(528, 128, 34, 24));
		tuboGenerator = new TuboGenerator();
		ui = new UI();
		
		entities.add(player);
		
	}
	
	public void initFrame(){
		frame = new JFrame("Flappy Bird");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick(){
		if(game_state != "GAME_OVER") {
			background.tick();
			grass.tick();
		}
		if(game_state == "NORMAL") {
			
			tuboGenerator.tick();
			
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
		}
	}
	


	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		
		g.setColor(new Color(122,102,255));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		background.render(g);

		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		grass.render(g);
		
		if(game_state == "START") {
			g.drawImage(spritesheet.getSprite(292, 346, 192, 44), WIDTH / 2 - 96, 100, null);
			g.drawImage(spritesheet.getSprite(344, 244, 78, 98), WIDTH / 2 - 39, HEIGHT / 2 - 49, null);
			if(start) {
				game_state = "ENTRADA";
			}
		}else if(game_state == "ENTRADA") {
			timerStart++;
			if(timerStart <= 60*2) {
				g.drawImage(spritesheet.getSprite(292, 442, 174, 44), WIDTH / 2 - 87, 100, null);
			}else {
				game_state = "NORMAL";
			}
		}else if(game_state == "GAME_OVER") {
			g.setColor(new Color(0,0,0,100));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.drawImage(spritesheet.getSprite(292, 398, 188, 38), WIDTH / 2 - 99, 100, null);
			g.drawImage(spritesheet.getSprite(292, 116, 226, 116), WIDTH / 2 - 113, 200, null);
			if(score < 20) {
				g.drawImage(spritesheet.getSprite(604, 274, 44, 44), 270, 242, null);
			}else if(score < 40) {
				g.drawImage(spritesheet.getSprite(532, 458, 44, 44), 270, 242, null);
			}else if(score < 60) {
				g.drawImage(spritesheet.getSprite(483, 458, 44, 44), 270, 242, null);
			}else if(score > 100) {
				g.drawImage(spritesheet.getSprite(440, 288, 44, 44), 270, 242, null);
			}
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 18));
			g.drawString(""+(int)score, 420, 250);
			g.drawString(">APERTE ESPAÇO PARA REINICIAR<", 200, 400);
		}
		
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		ui.render(g);
		bs.show();
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(game_state == "START") {
				start = true;
			}else if(game_state == "NORMAL") {
				player.isPressed = true;
			}else if(game_state == "GAME_OVER") {
				World.restartGame();
			}
		}
	
	}

	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(game_state == "NORMAL") {
				player.isPressed = false;
			}
		}
		
	}

	
	public void keyTyped(KeyEvent e) {
		
	}

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}


	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent e) {	
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent e) {
	
	}

	
}

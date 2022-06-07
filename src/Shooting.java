import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
public class Shooting {
	
	public static ShootingFrame shootingFrame;
	public static boolean loop; 
	
	
	public static void main(String[] args) {
		
		shootingFrame = new ShootingFrame();
		loop = true;
		
		Graphics gra = shootingFrame.panel.image.createGraphics(); 
		
		long startTime;
		long fpsTime = 0;
		int fps = 30;
		int FPS = 0;
		int FPSCount = 0;
		
		EnumShootingScreen screen = EnumShootingScreen.STSRT;
		
		//GAME loop functions
		int playerX=0,playerY=0;
		int bulletInterval = 0;
		int score = 0;
		
		ArrayList<Bullet> bullets_player = new ArrayList<>();
		ArrayList<Bullet> bullets_enemies = new ArrayList<>();
		ArrayList<Enemy> enemies = new ArrayList<>();
		Random random = new Random();
		
		
		while(loop) {
			
			if((System.currentTimeMillis() -fpsTime) >= 1000) {
				fpsTime = System.currentTimeMillis();
				FPS = FPSCount;
				FPSCount = 0;
			}
			
			FPSCount++;
			startTime = System.currentTimeMillis();
			gra.setColor(Color.WHITE);
			gra.fillRect(0, 0, 500, 500);
			
			switch (screen) {
				case STSRT:
					score = 0;
					gra.setColor(Color.BLACK);
					Font font = new Font("SansSerif", Font.PLAIN, 40);
					gra.setFont(font);
					FontMetrics metrics = gra.getFontMetrics(font);
					gra.drawString("Shooting", 250 - (metrics.stringWidth("Shooting") / 2), 100);
					
					font = new Font("SansSerif", Font.PLAIN, 20);
					gra.setFont(font);
					metrics = gra.getFontMetrics(font);
					gra.drawString("Press SPACE to START", 250 - (metrics.stringWidth("Press SPACE to START") / 2), 130);
					
					if(Keyboard.isKeyPressed(KeyEvent.VK_SPACE)) {
						screen = EnumShootingScreen.GAME;
						bullets_player = new ArrayList<>();
						enemies = new ArrayList<>();
						
						playerX = 235;
						playerY = 430;
					}
					break;
				
				case GAME:
					gra.setColor(Color.BLUE);
					gra.fillRect(playerX+10, playerY, 10, 10);
					gra.fillRect(playerX, playerY+10, 30, 10);
					
					gra.setColor(Color.BLUE);
					for(int i = 0; i < bullets_player.size();i++){
						Bullet bullet = bullets_player.get(i);
						gra.fillRect(bullet.x, bullet.y, 5, 5);
						bullet.y -= 10;
						if (bullet.y < 0) bullets_player.remove(i);
						
						for (int l = 0; l < enemies.size(); l++) {
							Enemy enemy = enemies.get(l);
							if(bullet.x>=enemy.x&&bullet.x<=enemy.x+30&&bullet.y>=enemy.y&&bullet.y<=enemy.y+20) {
								enemies.remove(l);
								score += 10;
							}
						}
						
					}
					gra.setColor(Color.RED);
					for(int i = 0; i < bullets_enemies.size();i++){
						Bullet bullet = bullets_enemies.get(i);
						gra.fillRect(bullet.x, bullet.y, 5, 5);
						bullet.y += 30;
						if (bullet.y > 500) bullets_enemies.remove(i);
						if(bullet.x>=playerX&&bullet.x<=playerX+30&&bullet.y>=playerY&&bullet.y<=playerY+20) {
							screen = EnumShootingScreen.GAME_OVER;
						}
						
						
					}
					
				for (int i = 0; i < enemies.size(); i++) {
					Enemy enemy = enemies.get(i);
					gra.fillRect(enemy.x, enemy.y, 30, 10);
					gra.fillRect(enemy.x+10, enemy.y+10	, 10, 10);
					
					enemy.y += 3;
					if(enemy.y > 500) enemies.remove(i);
					if(random.nextInt(80)==1) bullets_enemies.add(new Bullet(enemy.x, enemy.y));
					if(enemy.x>=playerX&&enemy.x<=playerX+30&&enemy.y>=playerY&&enemy.y<=playerY+20) {
						screen = EnumShootingScreen.GAME_OVER;
					}
				}
				if(random.nextInt(100)==1) enemies.add(new Enemy(random.nextInt(470), 0));
					
					if(Keyboard.isKeyPressed(KeyEvent.VK_LEFT) && playerX > 0) playerX-=8;
					if(Keyboard.isKeyPressed(KeyEvent.VK_RIGHT) && playerX < 470) playerX+=8;
					if(Keyboard.isKeyPressed(KeyEvent.VK_UP) && playerY>0) playerY-=8;
					if(Keyboard.isKeyPressed(KeyEvent.VK_DOWN) && playerY<470) playerY+=8;
					
					if(Keyboard.isKeyPressed(KeyEvent.VK_SPACE) && bulletInterval==0) {
						bullets_player.add(new Bullet(playerX+12,playerY));
						bulletInterval = 5;
						
					}
					if(bulletInterval>0) bulletInterval--;
					gra.setColor(Color.BLACK);
					font = new Font("SansSerif", Font.PLAIN, 20);
					metrics = gra.getFontMetrics(font);
					gra.setFont(font);
					gra.drawString("SCORE:"+score, 470-metrics.stringWidth("SCORE:"+score), 430);
					
					break;
					
				case GAME_OVER:
					
					gra.setColor(Color.BLACK);
					font = new Font("SansSerif", Font.PLAIN, 40);
					gra.setFont(font);
					metrics = gra.getFontMetrics(font);
					gra.drawString("GAME OVER", 250 - (metrics.stringWidth("GAME OVER") / 2), 100);
					font = new Font("SansSerif", Font.PLAIN, 20);
					gra.setFont(font);
					metrics = gra.getFontMetrics(font);
					gra.drawString("SCORE:"+score, 250 - (metrics.stringWidth("SCORE:"+score) / 2), 150);
					gra.drawString("Press ESC to Return Start Screen", 250 - (metrics.stringWidth("Press ESC to Return Start Screen") / 2), 180);
					if(Keyboard.isKeyPressed(KeyEvent.VK_ESCAPE)) {
						screen = EnumShootingScreen.STSRT;
					}
					break;	
			}
		
			gra.setColor(Color.BLACK);
			gra.setFont(new Font("SansSerif", Font.PLAIN, 10));
			gra.drawString(FPS + "FPS", 0, 470);
			
			shootingFrame.panel.draw();
			
			try {
				long runTime = System.currentTimeMillis() - startTime;
				if(runTime <= (1000/fps)) {
					Thread.sleep((1000 / fps) - runTime);
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}

package platform;

import java.awt.Rectangle;

public class MovingPlatform {
	private int xCoord;
	private int yCoord;
	private int width;
	private int height;
	private Rectangle platformHitbox;
	private int xscroll;
	private int yscroll;
	private int xvel;
	private int yvel;
	public MovingPlatform(){
		xCoord=0;
		yCoord=0;
		width=0;
		height=0;
		platformHitbox=null;
		
	}
	public MovingPlatform(int x, int y, int w, int h,int xvel, int yvel){
		this.xvel=xvel;
		this.yvel=yvel;
		xscroll=0;
		yscroll=0;
		xCoord = x;
		yCoord = y;
		width = w;
		height = h;
		platformHitbox = new Rectangle(xCoord,yCoord,width,height);
	}
	public Rectangle getHitbox(){
		return platformHitbox;
	}
	public int getTop(){
		return platformHitbox.y;
	}
	public int getLeft(){
		return platformHitbox.x;
	}
	public int getRight(){
		return platformHitbox.x+width;
	}
	public int getBottom(){
		return platformHitbox.y+height;
	}
	public void setScroll(int dx,int dy){
		xscroll=dx;
		yscroll=dy;
	}
}

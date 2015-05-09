package level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import platform.Platform;
import tile.Tile;
import tile.TileSet;

public class TileMap {
	public static final int TILE_SIZE= 20;
	private TileSet ts;
	private ArrayList<Tile> alltiles;
	private Tile[][] background;
	private Tile[][] stationary;
	private Tile[][] foreground;
	private File tmxfile;
	private ArrayList<Platform> plats;
	private int numRows;
	private int numCols;
	private DocumentBuilderFactory factory ;
	private DocumentBuilder builder;
	private XPathFactory xpfactory;
	private XPath path;
	private Document doc;
	public TileMap(File tmx){
		plats=new ArrayList<Platform>();
		tmxfile=tmx;
		
		try{
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xpfactory = XPathFactory.newInstance();
	      	path = xpfactory.newXPath();
	      	doc = builder.parse(tmxfile);
			ts = new TileSet(TILE_SIZE,TILE_SIZE,tmx);
			alltiles=ts.getTiles();
			numRows=Integer.parseInt(path.evaluate("/map/@height",doc));
			numCols=Integer.parseInt(path.evaluate("/map/@width",doc));
		}catch(Exception e){e.printStackTrace();}
		System.out.println(numRows);
		System.out.println(numCols);
		background = new Tile[numRows][numCols];
		stationary = new Tile[numRows][numCols];
		foreground = new Tile[numRows][numCols];
		try {
			loadMap();
		} catch (XPathExpressionException | SAXException
				| ParserConfigurationException | IOException e) {e.printStackTrace();}
	}
	public BufferedImage drawMap(){
		BufferedImage bfImage=new BufferedImage(numCols*TILE_SIZE,numRows*TILE_SIZE,BufferedImage.TYPE_INT_RGB);
		Graphics g = bfImage.getGraphics();
		for(int r=0;r<numRows;r++){
			for(int c=0;c<numCols;c++){
				if(background[r][c]!=null)
				g.drawImage(background[r][c].getImage(), c*TILE_SIZE, r*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
			}
		}
		for(int r=0;r<numRows;r++){
			for(int c=0;c<numCols;c++){
				if(stationary[r][c]!=null)
				g.drawImage(stationary[r][c].getImage(), c*TILE_SIZE, r*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
			}
		}
		for(int r=0;r<numRows;r++){
			for(int c=0;c<numCols;c++){
				if(foreground[r][c]!=null)
				g.drawImage(foreground[r][c].getImage(), c*TILE_SIZE, r*TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
			}
		}
		g.dispose();
		return bfImage;
	}
	public void loadMap() throws XPathExpressionException, SAXException, ParserConfigurationException, IOException{
		
		int numLayers = Integer.parseInt(path.evaluate("count(/map/layer)", doc));
		int r,c,gidNumber;
		for(int i=1;i<=numLayers;i++){
			String name = path.evaluate("/map/layer["+i+"]/@name",doc);
			switch(name){
			case "background":
				gidNumber=1;
				for(r=0;r<background.length;r++){
					for(c=0;c<background[r].length;c++){
						int whichTile =Integer.parseInt(path.evaluate("/map/layer["+i+"]/data[1]/tile["+gidNumber+"]/@gid",doc));
						if(whichTile>0){
							background[r][c]=alltiles.get(whichTile-1);	
						}
						gidNumber++;
					}
				}
				
			break;
			case "stationary":
				gidNumber=1;
				for(r=0;r<stationary.length;r++){
					for(c=0;c<stationary[r].length;c++){
						int whichTile =Integer.parseInt(path.evaluate("/map/layer["+i+"]/data[1]/tile["+gidNumber+"]/@gid",doc));
						if(whichTile>0){
							stationary[r][c]=alltiles.get(whichTile-1);	
						}
						gidNumber++;
					}
				}
				
			break;
			case "foreground":
				gidNumber=1;
				for(r=0;r<foreground.length;r++){
					for(c=0;c<foreground[r].length;c++){
						int whichTile =Integer.parseInt(path.evaluate("/map/layer["+i+"]/data[1]/tile["+gidNumber+"]/@gid",doc));
						if(whichTile>0){
							foreground[r][c]=alltiles.get(whichTile-1);	
						}
						gidNumber++;
					}
				}
				
			break;
			default: System.out.println("You entered an unrecognizable layer. ");
			}
		}
		int numPlats = Integer.parseInt(path.evaluate("count(/map/objectgroup[1]/object)", doc));
		for(int j=1;j<=numPlats;j++){
			int x = Integer.parseInt(path.evaluate("/map/objectgroup[1]/object["+j+"]/@x", doc));
			int y= Integer.parseInt(path.evaluate("/map/objectgroup[1]/object["+j+"]/@y", doc));
			int w = Integer.parseInt(path.evaluate("/map/objectgroup[1]/object["+j+"]/@width", doc));
			int h= Integer.parseInt(path.evaluate("/map/objectgroup[1]/object["+j+"]/@height", doc));
			plats.add(new Platform(x,y,w,h));
		}
	}
	public ArrayList<Platform> getPlatforms(){
		return plats;
	}
	public int getNumCols(){
		return numCols;
	}
	public int getNumRows(){
		return numRows;
	}
}
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/*
 * Structure 
 * 
 *  Start ---- 2 ---- 3 ---- 4 ---- 5 ----
 *    |        |      |      |      |
 *    |        |      |      |      |
 * 	  1        2      3      4      5
 *    |        |      |      |      |
 *    |        |      |      |      |
 * 	  1        2      3      4      5
 * 
 * 
 * 
 * 
 */
public class Tile {
	
	public enum TileState{
		unrevealed, revealed, marked;
	}
	
	public enum Direction{
		north, east, south, west;
	}
	
	TileState state;
	boolean hasMine;
	Tile tileNorth, tileEast, tileSouth, tileWest;
	
	
	public Tile(){
		state = TileState.unrevealed;
		
		if (Math.random() < 0.1) {
			hasMine = true;
		}
	}
	
	public Tile(Tile anotherTile){
		
		state = anotherTile.state;
		hasMine = anotherTile.hasMine;
		tileNorth = anotherTile.tileNorth == null? null : new Tile(anotherTile.tileNorth);
		tileEast = anotherTile.tileEast == null ? null : new Tile(anotherTile.tileEast);
		tileWest = anotherTile.tileWest == null ? null : new Tile(anotherTile.tileWest);
		tileSouth = anotherTile.tileSouth == null ? null : new Tile(anotherTile.tileSouth);
		
	}
	
	/**
	 * Count the number of tiles in specified direction, excluding the receiver
	 * @param direction 
	 * @return
	 * @throws Exception
	 */
	public int numOfTilesInDirection(Direction direction) throws Exception{
		switch (direction) {
		case north:
			if(tileNorth == null){
				return 0;
			}
			return 1 + tileNorth.numOfTilesInDirection(Direction.north);
		case east:
			if(tileEast == null){
				return 0;
			}
			return 1 + tileEast.numOfTilesInDirection(Direction.east);
		case south:
			if(tileSouth == null){
				return 0;
			}
			return 1 + tileSouth.numOfTilesInDirection(Direction.south);
		case west:
			if(tileWest == null){
				return 0;
			}
			return 1 + tileWest.numOfTilesInDirection(Direction.west);
		default:
			throw new  Exception("Direction Undefined");
//			break;
		}
		
	}
	/**
	 * Gets a new tile grid
	 * @param width
	 * @param height
	 * @return
	 * @throws Exception
	 */
	public static Tile getTileGrid(int width, int height) throws Exception{
		Tile tile = getDefaultTile();
		tile.addTiles(width - 1, Direction.east);
		tile.addTilesToRows(height - 1);
		Tile.linkGrid(tile);
		return tile;
	}
	//dedicated to getTileGrid, invoked On the Root Tile, not changing the receiver
	/**
	 * This method does not change the receiver
	 * @param tileColumn
	 */
	private static void linkGrid(Tile tileColumn){
		if (tileColumn.tileEast == null) {
			return;
		}
		Tile tileColumnEast = tileColumn.tileEast;
		linkColumns(tileColumn, tileColumnEast);
		linkGrid(tileColumn.tileEast);
	}
	//dedicated to linkGrid
	private static void linkColumns(Tile tileColumnWest, Tile tileColumnEast){
		if (tileColumnWest == null || tileColumnEast == null) {
			return;
		}
		tileColumnWest.tileEast = tileColumnEast;
		tileColumnEast.tileWest = tileColumnWest;
		linkColumns(tileColumnWest.tileSouth, tileColumnEast.tileSouth);
		
	}
	
	
	//dedicated to getTileGrid
	private void addTilesToRows(int numOfTilesToSouth) throws Exception{
		addTiles(numOfTilesToSouth, Direction.south);
		if(tileEast == null){
			return;
		}
		tileEast.addTilesToRows(numOfTilesToSouth);
	}
	
	public static Tile getDefaultTile(){
		return new Tile();
	}
	
	public void addTiles(int numOfTiles, Direction direction) throws Exception{
		if (numOfTiles == 0) {
			return;
		}
		Tile targetTile = getDefaultTile();
		switch (direction) {
		case north:
			tileNorth = targetTile;
			targetTile.tileSouth = this;
			break;
		case east:
			tileEast = targetTile;
			targetTile.tileWest = this;
			break;
		case south:
			tileSouth = targetTile;
			targetTile.tileNorth = this;
			break;
		case west:
			tileWest = targetTile;
			targetTile.tileEast = this;
			break;
		default:
			throw new Exception("Direction Undefined");
//			break;
		}
		targetTile.addTiles(numOfTiles - 1, direction);
	}
	
	private static int TILE_LENGTH = 30;
	
	//graphics
	public static void paintGrid(Tile tileGrid, Graphics g, int x, int y){
		if (tileGrid == null) {
			return;
		}
		paintColumn(tileGrid, g, x, y);
		paintGrid(tileGrid.tileEast, g, x + TILE_LENGTH, y);
	}
	
	private static void paintColumn(Tile tileColumn, Graphics g, int x, int y){
		if(tileColumn == null){
			return;
		}
		paintTile(tileColumn, g, x, y);
		paintColumn(tileColumn.tileSouth, g, x, y + TILE_LENGTH);
	}
	private static final int STRING_OFFSET = TILE_LENGTH / 5 * 2;
	
	private static void paintTile(Tile tile, Graphics g, int x, int y){
		
		switch (tile.state) {
		case unrevealed:
			g.setColor(Color.BLUE);
			g.fillRect(x, y, TILE_LENGTH, TILE_LENGTH);
			
			break;
		case revealed:
			if(tile.hasMine){
				g.setColor(Color.BLACK);
				g.fillRect(x, y, TILE_LENGTH, TILE_LENGTH);
			}
			g.setColor(Color.BLUE);
			g.drawOval(x, y, TILE_LENGTH, TILE_LENGTH);
			int num = countNumberOfMinesAroundATile(tile);
			if(num != 0){
				g.setColor(Color.BLACK);
				g.setFont(new Font("Menlo", Font.PLAIN, TILE_LENGTH / 2));
				g.drawString(Integer.toString(num), x + STRING_OFFSET, y + TILE_LENGTH - STRING_OFFSET);
			}
			
			break;
		case marked:
			g.setColor(Color.BLUE);
			g.fillRect(x, y, TILE_LENGTH, TILE_LENGTH);
			g.setColor(Color.RED);
			g.fillOval(x, y, TILE_LENGTH, TILE_LENGTH);
			
			break;
			

			
		default:
			break;
		}
		g.setColor(Color.BLACK);
		g.drawRect(x, y, TILE_LENGTH, TILE_LENGTH);
	}
	
	private static int countNumberOfMinesAroundATile(Tile centerTile){
		int numOfMines = 0;
		if (centerTile.tileNorth != null) {
			numOfMines += centerTile.tileNorth.hasMine? 1:0;
			if (centerTile.tileNorth.tileWest != null) {
				numOfMines += centerTile.tileNorth.tileWest.hasMine? 1:0;
			}
			if (centerTile.tileNorth.tileEast != null) {
				numOfMines += centerTile.tileNorth.tileEast.hasMine? 1:0;
			}
		}
		if (centerTile.tileSouth != null) {
			numOfMines += centerTile.tileSouth.hasMine? 1:0;
			if (centerTile.tileSouth.tileWest != null) {
				numOfMines += centerTile.tileSouth.tileWest.hasMine? 1:0;
			}
			if (centerTile.tileSouth.tileEast != null) {
				numOfMines += centerTile.tileSouth.tileEast.hasMine? 1:0;
			}
		}
		if (centerTile.tileWest != null) {
			numOfMines += centerTile.tileWest.hasMine? 1:0;
		}
		if (centerTile.tileEast != null) {
			numOfMines += centerTile.tileEast.hasMine? 1:0;
		}
		return numOfMines;
		
	}
	
	//solving grid
	
	public static void revealGrid(Tile tileGrid, int x, int y){
		findTargetTile(tileGrid, x, y).reveal();
	}
	
	public static Tile findTargetTile(Tile tileGrid, int x, int y){
		Tile targetTile = tileGrid;
		for (int i = 0; i < x; i++) {
			targetTile = targetTile.tileEast;
		}
		for (int i = 0; i < y; i++) {
			targetTile = targetTile.tileSouth;
		}
		return targetTile;
	}
	
	public void reveal(){
		if(state != TileState.unrevealed){
			return;
		}
		state = TileState.revealed;
		if(hasMine){
			
			JOptionPane.showInputDialog("You hit a mine");
		}
		//revealling
		if (countNumberOfMinesAroundATile(this) == 0) {
			if (this.tileNorth != null) {
				 this.tileNorth.reveal();
				if (this.tileNorth.tileWest != null) {
					 this.tileNorth.tileWest.reveal();
				}
				if (this.tileNorth.tileEast != null) {
					 this.tileNorth.tileEast.reveal();
				}
			}
			if (this.tileSouth != null) {
				 this.tileSouth.reveal();
				if (this.tileSouth.tileWest != null) {
					 this.tileSouth.tileWest.reveal();
				}
				if (this.tileSouth.tileEast != null) {
					 this.tileSouth.tileEast.reveal();
				}
			}
			if (this.tileWest != null) {
				 this.tileWest.reveal();
			}
			if (this.tileEast != null) {
				 this.tileEast.reveal();
			}
		}
	}
	
	public static void markGrid(Tile tileGrid, int x, int y){
		findTargetTile(tileGrid, x, y).mark();
	}
	
	public void mark(){
		if(state == TileState.unrevealed){
			state = TileState.marked;
		} else if(state == TileState.marked){
			state = TileState.unrevealed;
		}
	}
	//mouse gameplay
	public static void clickedOn(Tile tileGrid, int xCoor, int yCoor){
		revealGrid(tileGrid, xCoor / TILE_LENGTH, yCoor / TILE_LENGTH);
	}
	
	public static void rightClickedOn(Tile tileGrid, int xCoor, int yCoor){
		markGrid(tileGrid, xCoor / TILE_LENGTH, yCoor / TILE_LENGTH);
	}
	/**
	 * find if all noneMined tiles have been revealed
	 * @param tileGrid
	 * @return
	 */
	public static boolean winGrid(Tile tileGrid){
		if(tileGrid == null){
			return true;
		}
		if(winColumn(tileGrid)){
			return winGrid(tileGrid.tileEast);
		} else {
			return false;
		}
	}
	public static boolean winColumn(Tile tileColumn){
		if(tileColumn == null){
			return true;
		}
		if(tileColumn.state == TileState.unrevealed){
			if(tileColumn.hasMine == false){
				return false;
			} else {
				return winColumn(tileColumn.tileSouth);
			}
		} else {
			return winColumn(tileColumn.tileSouth);
		}
	}
	
	//solving algorithm
	
	public static ArrayList<Tile> getSurroundingTiles(Tile centerTile){
		ArrayList<Tile> list = new ArrayList<Tile>();
		if (centerTile.tileNorth != null) {
			list.add(centerTile.tileNorth);
			if (centerTile.tileNorth.tileWest != null) {
				list.add(centerTile.tileNorth.tileWest);
			}
			if (centerTile.tileNorth.tileEast != null) {
				list.add(centerTile.tileNorth.tileEast);
			}
		}
		if (centerTile.tileSouth != null) {
			list.add(centerTile.tileSouth);
			if (centerTile.tileSouth.tileWest != null) {
				list.add(centerTile.tileSouth.tileWest);
			}
			if (centerTile.tileSouth.tileEast != null) {
				list.add(centerTile.tileSouth.tileEast);
			}
		}
		if (centerTile.tileWest != null) {
			list.add(centerTile.tileWest);
		}
		if (centerTile.tileEast != null) {
			list.add(centerTile.tileEast);
		}
		return list;
	}
	
	public static void autoSolveGrid(Tile tileGrid){
		if(tileGrid == null){
			return;
		}
		autoSolveColumn(tileGrid);
		autoSolveGrid(tileGrid.tileEast);
	}
	
	private static void autoSolveColumn(Tile columnTile){
		if(columnTile == null){
			return;
		}
		solveTile(columnTile);
		autoSolveColumn(columnTile.tileSouth);
	}
	
	private static void solveTile(Tile tile){
		if(tile.state != TileState.revealed){
			return;
		}
		ArrayList<Tile> tiles = getSurroundingTiles(tile);
		int numUnrevealed = 0;
		int numMarked = 0;
		int numRevealed = 0;
		int numOfSurroundingMines = countNumberOfMinesAroundATile(tile);
		for (int i = 0; i < tiles.size(); i++) {
			Tile curTile = tiles.get(i);
			switch (curTile.state) {
			case unrevealed:
				numUnrevealed ++;
				break;
			case revealed:
				numRevealed ++;
				break;
			case marked:
				numMarked ++;
				break;

			default:
				break;
			}
		}
		if (numMarked == numOfSurroundingMines) {
			for (Tile tile2 : tiles) {
				tile2.reveal();
			}
		}
		if (numOfSurroundingMines - numMarked == numUnrevealed) {
			//reveal unrevealed
			for (Tile tile2 : tiles) {
				if (tile2.state == TileState.unrevealed) {
					tile2.mark();
				}
			}
		}
		
	}
	
}

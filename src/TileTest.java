import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TileTest {

	public TileTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Tile tile = Tile.getTileGrid(10, 10);
		for (int i = 1;  i< 8; i++) {
			for (int j = 1; j < 8; j++) {
				Tile curTile = Tile.findTargetTile(tile, i, j);
				assertSame(curTile.tileWest, Tile.findTargetTile(tile, i - 1, j));
				assertSame(curTile.tileEast, Tile.findTargetTile(tile, i + 1, j));
				assertSame(curTile.tileNorth, Tile.findTargetTile(tile, i, j - 1));
				assertSame(curTile.tileSouth, Tile.findTargetTile(tile, i, j + 1));
			}
		}
	}

}

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainPanel extends JPanel {
	
	private Tile tileGrid;
	
	
	
	/**
	 * @return the tileGrid
	 */
	public Tile getTileGrid() {
		return tileGrid;
	}



	/**
	 * @param tileGrid the tileGrid to set
	 */
	public void setTileGrid(Tile tileGrid) {
		this.tileGrid = tileGrid;
	}

	public void clickedOn(MouseEvent e){
		if(e.isAltDown()){
			Tile.rightClickedOn(tileGrid, e.getX(), e.getY());
		} else {
			Tile.clickedOn(tileGrid, e.getX(), e.getY());
		}
		repaint();
		if(Tile.winGrid(tileGrid)){
			JOptionPane.showMessageDialog(this, "You Win");
		}
	}


	public MainPanel(Tile tileGrid) {
		super();
		this.tileGrid = tileGrid;
		setPreferredSize(new Dimension(200, 200));
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				clickedOn(e);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}



	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Tile.paintGrid(tileGrid, g, 0, 0);
	}
	
	public void autoSolve(){
		Tile.autoSolveGrid(tileGrid);
		repaint();
		if(Tile.winGrid(tileGrid)){
			JOptionPane.showMessageDialog(this, "You Win");
		}
	}

}

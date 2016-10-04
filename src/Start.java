import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public class Start {
	
	public static void main(String[] args) throws Exception{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		Tile tileGrid = Tile.getTileGrid(10, 10);
		System.out.println("H");
		
		//creating actions
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainPanel panel = new MainPanel(tileGrid);
		frame.add(panel);
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		JMenu actionMenu = new JMenu("Actions");
		menubar.add(actionMenu);
		//revealing
		JMenuItem revealAction = new JMenuItem("Reveal Tile");
		actionMenu.add(revealAction);
		revealAction.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int x = Integer.parseInt(JOptionPane.showInputDialog("Enter X:"));
				int y = Integer.parseInt(JOptionPane.showInputDialog("Enter Y:"));
				Tile.revealGrid(tileGrid, x, y);
				panel.repaint();
			}
		});
		//resetting
		JMenuItem resetAction = new JMenuItem("Reset");
		actionMenu.add(resetAction);
		resetAction.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int width = Integer.parseInt(JOptionPane.showInputDialog("Enter Width:"));
				int height = Integer.parseInt(JOptionPane.showInputDialog("Enter Height:"));
				try {
					panel.setTileGrid(Tile.getTileGrid(width, height));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panel.repaint();
			}
		});
		//auto solve
		JMenuItem solveAction = new JMenuItem("Auto Solve");
		actionMenu.add(solveAction);
		solveAction.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				panel.autoSolve();
			}
		});
		int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		solveAction.setAccelerator(KeyStroke.getKeyStroke('E', shortcutMask));
		
		frame.pack();
		frame.setVisible(true);
		System.out.println("FINISHED");
	}
	
	

}

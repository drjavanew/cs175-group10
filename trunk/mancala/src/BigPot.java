

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

public class BigPot extends Pot {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2982813259400682188L;

	public BigPot() {
		super();
		this.setPreferredSize(new Dimension(120,300));
		createListener();
		refresh();
	}
	
	private boolean ToPieMove() {
		return (!((KalahPieGameState)(getOwner().getGUI().game.gs)).secondPlayerFirstMoveMade && getOwner().getGUI().ready==2
				&& !getOwner().isMovingBeans() && getOwner().isTurn() && getOwner().getName().equalsIgnoreCase("interactive"));
		
	}
	
	@Override
	protected void createListener() {
		this.addMouseListener(new MouseAdapter() {
			
			public void mouseExited(MouseEvent e) {
				
					mouseOver = false;
					refresh();
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (ToPieMove()) {
					mouseOver = true;
					refresh();
				}
			}
					
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ToPieMove()) {
					mouseOver = false;
					getOwner().MouseMove = true;
					getOwner().valMouseMove = -1;
					refresh();
					}
				
			}
		});
		}
		
	@Override
	protected void initBeans() {
		beans = Collections.synchronizedList(new ArrayList<Bean>());
		refresh();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		if (!mouseOver) {
			g2d.setColor(new Color(92, 51, 23));
		} else {
			g2d.setColor(new Color(139, 69, 19));
		}
		g2d.setStroke(new BasicStroke(5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		g2d.drawOval((int) (getWidth() * 0.1), (int) (getHeight() * 0.1), (int) (getWidth() * 0.8), (int) (getHeight() * 0.8));
		g2d.setColor(new Color(255, 69, 0));
		g2d.setStroke(new BasicStroke(0.1F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		g.setColor(Color.white);
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(this.getOwner().getName(), g);
		int textWidth = (int) rect.getWidth();
		g.drawString(this.getOwner().getName(), (getWidth() - textWidth) / 2, 20);
	}
	
	
	

}



import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JComponent;


public class Pot extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2410561941378283911L;
	private static final int BEAN_COUNT = 4;
	private static final int NUMCOL = 6;
	protected boolean mouseOver;
	protected boolean beansInitialized;
	protected List<Bean> beans;
	private Player owner;
	protected Lock lock = new ReentrantLock();
	
	private int idx;
	private int idy;
	
	
	
	
	public Pot(int i, int j ) {
		
		idx = i;
		idy = j;
		mouseOver = false;
		beansInitialized = false;
		
		createListener();
		refresh();
	}
	
	public Pot( ) {
		
	
		mouseOver = false;
		beansInitialized = false;
		
		refresh();
	}

	protected void initBeans() {
		beans = Collections.synchronizedList(new ArrayList<Bean>());
		lock.lock();
		addBeans(BEAN_COUNT);
		lock.unlock();
	}
	
	public int getBeans() {
		int totalBeans;
		lock.lock();
		if (beans != null)
			totalBeans = beans.size();
		else
			totalBeans = 0;
		lock.unlock();
		return totalBeans;
	}
	
	
	public void addBeans(int amount) {
		Random rnd = new Random();
		int x;
		int y;
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < amount; i++) {
			x = (int) ((getWidth() - 15) * 0.25) + rnd.nextInt((int) ((getWidth() - 15) * 0.5));
			y = (int) ((getHeight() - 15) * 0.25) + rnd.nextInt((int) ((getHeight() - 15) * 0.5));
			Bean bean = new Bean(1.0 * x / getWidth(), 1.0 * y / getHeight());
			if(isSuitable(bean) || System.currentTimeMillis() - startTime > 200) {
				lock.lock();
				beans.add(bean);
				lock.unlock();
			}
			else
				i--; // redo it
		}
		refresh();
	}
	
	public void removeBeans() {
		lock.lock();
		if (beans!= null) beans.clear();
		lock.unlock();
		refresh();
	}
	
	private boolean allowedToClick() {
		return beans != null && !beans.isEmpty() && !getOwner().isMovingBeans() 
		&& getOwner().isTurn() && getOwner().getName().equalsIgnoreCase("interactive")
		&& getOwner().getGUI().ready !=0;
	}

	public void setOwner(Player player) {
		owner = player;
	}
	
	protected Player getOwner() {
		return owner;
	}

	protected void createListener() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (allowedToClick()) {
					mouseOver = true;
					refresh();
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mouseOver = false;
				refresh();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (allowedToClick()) {
					mouseOver = false;
					getOwner().MouseMove =true;
					getOwner().valMouseMove = idy;
					refresh();
					
				}
			}
		});
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
		
		
		if(!beansInitialized) {
			beansInitialized = true;
			initBeans();
		}
		lock.lock();
		for(Bean bean : beans) {
			int x = (int) (bean.getX() * getWidth());
			int y = (int) (bean.getY() * getHeight());
			GradientPaint gp = new GradientPaint(x + 2, y + 2, new Color(255, 69, 0/*255, 81, 71*/), x + 13, y + 13, new Color(139, /*37*/9, 0), false);
			g2d.setPaint(gp);
			g2d.fillOval(x, y, 15, 15);
		}
		g2d.setColor(Color.white);
		if(idx == 0) {
			g2d.drawString(beans.size() + "", (int)(getWidth() * 0.5) - 5, getHeight());
		} else {
			g2d.drawString(beans.size() + "", (int)(getWidth() * 0.5) - 5, 10);
		}
		lock.unlock();
	}
	
	private boolean isSuitable(Bean bean) {
		for(Bean b : beans) {
			if(b.distanceFrom(bean, getWidth(), getHeight()) < 15.0) {
//				System.out.println("Too close!");
				return false;
			}
		}
		return true;
	}
	
	protected void refresh() {
		this.repaint();
	}

}

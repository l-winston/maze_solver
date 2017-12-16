import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class makeMaze {
	public static int X = 500;
	public static int Y = 250;
	public static int frameX = 1000;
	public static int frameY = 500;
	public static int[][] maze = new int[Y][X];

	public static JFrame frame = new JFrame();
	public static BufferedImage image = new BufferedImage(frameX, frameY, BufferedImage.TYPE_INT_RGB);

	public static Random rand = new Random();

	public static void main(String[] args) {
		// 0 is wall, 1 is passage way
		ArrayList<Point> walls = new ArrayList<Point>();

		maze[0][0] = 1;
		walls.add(new Point(0, 1));
		walls.add(new Point(1, 0));
		while (!walls.isEmpty()) {

			Point p = walls.get(rand.nextInt(walls.size()));
			walls.remove(p);
			if (check(p)) {
				maze[p.y][p.x] = 1;
				for (Point x : getSurrounding(p)) {
					if (maze[x.y][x.x] == 0) {
						walls.add(x);
					}
				}
			}

		}
		maze[Y - 1][X - 1] = 1;

		//printMaze();
		initalizeFrame();
		displayMaze();
		
		int dfs[][] = new int[Y][X];
		int bfs[][] = new int[Y][X];

		copy(maze, dfs);
		copy(maze, bfs);
		
		depthFirstSearch(dfs);
		displayMaze();
		breadthFirstSearch(bfs);

	}

	private static void breadthFirstSearch(int[][] bfs) {
		ArrayList<Point> nodes = new ArrayList<Point>();
		nodes.add(new Point(0, 0));
		while (!nodes.isEmpty()) {
			for (Point p : nodes) {
				paint(p, Color.green);
				bfs[p.y][p.x] = 2;
			}
			
			sleep(25);
			
			ArrayList<Point> expand = expand(nodes, bfs);
			nodes = new ArrayList<Point>();
			nodes.addAll(expand);
			

		}
	}

	private static ArrayList<Point> expand(ArrayList<Point> nodes, int[][] bfs) {
		ArrayList<Point> ret = new ArrayList<Point>();
		for (Point p : nodes) {
			ArrayList<Point> surrounding = getSurrounding(p);
			for (Point x : surrounding) {
				if (!ret.contains(x)) {
					ret.add(x);
				}
			}
		}
		ArrayList<Point> remove = new ArrayList<Point>();
		for (Point p : ret) {
			if (bfs[p.y][p.x] != 1) {
				remove.add(p);
			}
		}
		
		ret.removeAll(remove);

		return ret;
	}

	private static void depthFirstSearch(int[][] dfs) {
		dive(new Point(0, 0), dfs);
	}

	private static void dive(Point p, int[][] dfs) {
		paint(p, Color.RED);
		
		sleep(1);
		
		ArrayList<Point> surrounding = getSurrounding(p);
		for (Point x : surrounding) {
			if (dfs[x.y][x.x] == 1) {
				dfs[x.y][x.x] = 2;
				dive(x, dfs);
			}
		}

	}

	private static void paint(Point p, Color c) {
		for (int i = p.y * frameY / Y; i < (p.y + 1) * frameY / Y; i++) {
			for (int j = p.x * frameX / X; j < (p.x + 1) * frameX / X; j++) {
				image.setRGB(j, i, c.getRGB());

			}
		}
		
		frame.repaint();
	}

	private static void initalizeFrame() {
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
	}

	private static void displayMaze() {
		for (int i = 0; i < frameX; i++) {
			for (int j = 0; j < frameY; j++) {
				if (maze[j / (frameY / Y)][i / (frameX / X)] == 1) {
					image.setRGB(i, j, Color.white.getRGB());
				}
			}
		}
	}

	private static boolean check(Point p) {
		ArrayList<Point> surrounding = getSurrounding(p);
		int count = 0;
		for (Point x : surrounding) {
			if (maze[x.y][x.x] == 1) {
				count++;
			}
		}
		return count == 1;
	}

	private static void printMaze() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}

	private static ArrayList<Point> getSurrounding(Point p) {
		ArrayList<Point> surrounding = new ArrayList<Point>();
		if (p.x > 0) {
			surrounding.add(new Point(p.x - 1, p.y));
		}
		if (p.y > 0) {
			surrounding.add(new Point(p.x, p.y - 1));
		}
		if (p.x < X - 1) {
			surrounding.add(new Point(p.x + 1, p.y));
		}
		if (p.y < Y - 1) {
			surrounding.add(new Point(p.x, p.y + 1));
		}
		return surrounding;
	}
	
	private static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void copy(int[][] base, int[][] copy){
		
		for(int i = 0; i < base.length; i++){
			for(int j = 0; j < base[0].length; j++){
				copy[i][j] = base[i][j];
			}
		}
		
	}
}

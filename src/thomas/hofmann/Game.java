package thomas.hofmann;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class Game implements Engine, ActionListener {
	private Window main = null;
	private GraphicsEngine ge;

	private Window controls;
	private JPanel controlPanel;

	private Rectangle obstacle;
	private int obstacleX;

	private int frameRate = 120;
	private int popSize = 5;
	private double mutationRate = 1;
	private double gravity = 0.11;

	private JTextField frameRateInput, mutationRateInput;
	private JButton start, reset;
	private JToggleButton drawing;

	private boolean draw = true;
	private boolean running = false;
	private boolean playersLeft = true;

	private ArrayList<Agent> player;
	private ArrayList<Double> output;
	private ArrayList<Double> input;

	public void start() {
		init();
	}

	private void init() {
		controls = new Window("Controls", 250, 200);
		controls.setBounds(0, 0, 250, 200);
		controlPanel = new JPanel();
		drawing = new JToggleButton("Drawing");
		drawing.setSelected(true);
		drawing.addActionListener(this);
		frameRateInput = new JTextField("120", 5);
		mutationRateInput = new JTextField("1", 5);
		start = new JButton("START");
		start.addActionListener(this);
		reset = new JButton("Reset");
		reset.addActionListener(this);
		frameRateInput.setHorizontalAlignment(JTextField.CENTER);
		frameRateInput.addActionListener(this);
		frameRateInput.setToolTipText("Framerate");
		mutationRateInput.setHorizontalAlignment(JTextField.CENTER);
		mutationRateInput.addActionListener(this);
		mutationRateInput.setToolTipText("MutationRate");
		controlPanel.add(frameRateInput);
		controlPanel.add(mutationRateInput);
		controlPanel.add(start);
		controlPanel.add(reset);
		controlPanel.add(drawing);
		controls.add(controlPanel);
		controls.validate();

		initVar();
	}

	private synchronized void initVar() {
		player = new ArrayList<>();
		obstacle = new Rectangle();
		player.clear();
		input = new ArrayList<>();
		output = new ArrayList<>();
		for (int p = 0; p < popSize; p++) {
			Agent temp = new Agent();
			temp.createBrainStructure(2, 3, 2);
			player.add(p, temp);
		}
		obstacleX = 900;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void write(Graphics2D g) {

	}

	@Override
	public synchronized void draw(Graphics2D g) {
		g.fill(obstacle);
		if (player.size() < popSize) {
			System.err.println("Drawing out of Sync!");
		} else {
			for (int p = 0; p < popSize; p++) {
				// System.out.println(p);
				if (player.get(p).enabled) {
					g.draw(player.get(p).hitbox);
				}
			}
		}
	}

	@Override
	public void loop(GraphicsEngine ge) {
		calcObstaclePos();

		input = new ArrayList<>();
		input.add(Double.valueOf(obstacleX));
		input = normalizeData(input, 0.0, (double) ge.width);
		output = new ArrayList<>();
		playersLeft = false;
		for (int p = 0; p < popSize; p++) {
			player.size();
			if (player.get(p).enabled) {
				input.add(player.get(p).verticalPos);
				output = player.get(p).process(input);
				if (output.get(0) > output.get(1)) {
					jump(p);
				}
				updateJump(p);
				player.get(p).updateHitbox();

				if (player.get(p).hitbox.intersects(obstacle)) {
					player.get(p).enabled = false;
					player.get(p).mutate(mutationRate);
				}
				playersLeft = true;
				player.get(p).traveled++;
				player.get(p).calculateFitness();
			}
		}

		if (!playersLeft) {
			// TODO: Implement Genetic Algorithm
			initVar();
		}
		ge.capFrameRate(frameRate);
		if (draw) {
			ge.redraw();
		}
	}

	private void jump(int pos) {
		if (!player.get(pos).jumping) {
			player.get(pos).jumping = true;
			player.get(pos).verticalVel = 8.0;
		}
	}

	private void updateJump(int pos) {
		player.get(pos).verticalPos = player.get(pos).verticalPos + player.get(pos).verticalVel * player.get(pos).time;
		player.get(pos).verticalVel = player.get(pos).verticalVel - gravity * player.get(pos).time;
		if (player.get(pos).verticalPos < 1) {
			player.get(pos).verticalPos = 1.0;
			player.get(pos).verticalVel = 0.0;
			player.get(pos).time = 1.0;
			player.get(pos).jumping = false;
		} else {
			player.get(pos).jumping = true;
		}
	}

	private void calcObstaclePos() {
		if (obstacleX < -100) {
			obstacleX = ge.width + 100;
		}
		obstacleX -= 2;
		obstacle.setBounds(obstacleX, 0, 100, 100);
	}

	private ArrayList<Double> normalizeData(ArrayList<Double> input, Double min, Double max) {
		double temp;
		ArrayList<Double> output = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			temp = input.get(i).doubleValue() - min;
			temp = temp / (max - min);
			output.add(temp);
		}
		return output;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (e.getSource() == frameRateInput) {
			frameRate = Integer.valueOf(frameRateInput.getText());
		}
		if(e.getSource() == mutationRateInput)
		{
			mutationRate = Double.valueOf(mutationRateInput.getText());
			System.out.println(mutationRate);
		}
		if (e.getSource() == start) {
			if (!running) {
				main = new Window("JumpAndLearn", 850, 500, true);
				ge = new GraphicsEngine(main, this);
				frameRate = Integer.valueOf(frameRateInput.getText()).intValue();
				ge.start();
				running = true;
				start.setText("Pause");
			} else {
				ge.kill();
				try {
					ge.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				main.dispose();
				start.setText("Start");
				running = false;
			}
		}
		if (e.getSource() == reset) {
			ge.kill();
			try {
				ge.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			main.dispose();
			start.setText("Start");
			running = false;
			initVar();
		}
		if (e.getSource() == drawing) {
			draw = drawing.isSelected();
		}
	}
}
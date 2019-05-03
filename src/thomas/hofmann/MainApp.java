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

public class MainApp implements Engine, ActionListener {
	private Window main = null;
	private GraphicsEngine ge;

	private Window controls;
	private JPanel controlPanel;

	private Rectangle obstacle;
	private int obstacleX;

	private int frameRate = 120;
	private int popSize = 25;
	private double gravity = 0.11;

	private JTextField frameRateInput;
	private JButton start, reset;
	private JToggleButton drawing;

	private boolean draw = true;
	private boolean running = false;
	private boolean playersLeft = true;

	private ArrayList<Agent> player;
	ArrayList<Double> output;
	ArrayList<Double> input;

	public static void main(String[] args) {
		new MainApp().init();
	}

	private void init() {
		this.controls = new Window("Controls", 250, 200);
		this.controls.setBounds(0, 0, 250, 200);
		this.controlPanel = new JPanel();
		this.drawing = new JToggleButton("Drawing");
		this.drawing.setSelected(true);
		this.drawing.addActionListener(this);
		this.frameRateInput = new JTextField("120", 5);
		this.start = new JButton("!START!");
		this.start.addActionListener(this);
		this.reset = new JButton("!Reset!");
		this.reset.addActionListener(this);
		this.frameRateInput.setHorizontalAlignment(JTextField.CENTER);
		this.frameRateInput.addActionListener(this);
		this.controlPanel.add(this.frameRateInput);
		this.controlPanel.add(this.start);
		this.controlPanel.add(this.reset);
		this.controlPanel.add(this.drawing);
		this.controls.add(this.controlPanel);
		this.controls.validate();

		this.player = new ArrayList<>();

		initVar();
	}

	private synchronized void initVar() {
		this.obstacle = new Rectangle();
		this.player.clear();
		this.input = new ArrayList<>();
		this.output = new ArrayList<>();
		for (int p = 0; p < this.popSize; p++) {
			Agent temp = new Agent(this.gravity);
			temp.createBrainStructure(2, 3, 2);
			this.player.add(p, temp);
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
	public synchronized void draw(Graphics2D g) {
		g.fill(this.obstacle);
		if (this.player.size() < this.popSize) {
			System.err.println("Drawing out of Sync!");
		} else {
			for (int p = 0; p < this.popSize; p++) {
				// System.out.println(p);
				if (this.player.get(p).isEnabled()) {
					g.draw(this.player.get(p).getHitbox());
				}
			}
		}
	}

	@Override
	public void loop(GraphicsEngine ge) {
		calcObstaclePos();

		this.input = new ArrayList<>();
		this.input.add(Double.valueOf(obstacleX));
		this.input = normalizeData(this.input, 0.0, (double) this.ge.width);
		this.output = new ArrayList<>();
		this.playersLeft = false;
		for (int p = 0; p < this.popSize; p++) {
			this.player.size();
			if (this.player.get(p).isEnabled()) {
				this.input.add(this.player.get(p).getVerticalVel());
				this.output = this.player.get(p).process(input);
				if (this.output.get(0) > this.output.get(1)) {
					jump(p);
				} else {

				}

				updateJump(p);
				this.player.get(p).updateHitbox();

				if (this.player.get(p).getHitbox().intersects(this.obstacle)) {
					this.player.get(p).setEnabled(false);
					this.player.get(p).mutate(1);
				}
			}
			if (this.player.get(p).isEnabled()) {
				this.playersLeft = true;
			}
		}

		if (!this.playersLeft) {
			//TODO: Implement Genetic Algorithm
			initVar();
		}
		ge.capFrameRate(this.frameRate);
		if (this.draw) {
			ge.redraw();
		}
	}

	private void jump(int pos) {
		if (!this.player.get(pos).getJumping()) {
			this.player.get(pos).setJumping(true);
			this.player.get(pos).setVerticalVel(8.0);
		}
	}

	private void updateJump(int pos) {
		this.player.get(pos).setVerticalPos(this.player.get(pos).getVerticalPos()
				+ this.player.get(pos).getVerticalVel() * this.player.get(pos).getTime());
		this.player.get(pos).setVerticalVel(
				(this.player.get(pos).getVerticalVel() - this.gravity * this.player.get(pos).getTime()));

		if (this.player.get(pos).getVerticalPos() < 1) {
			this.player.get(pos).setVerticalPos(1.0);
			this.player.get(pos).setVerticalVel(0.0);
			this.player.get(pos).setTime(1.0);
			this.player.get(pos).setJumping(false);
		} else {
			this.player.get(pos).setJumping(true);
		}
	}

	private void calcObstaclePos() {
		if (this.obstacleX < -100) {
			this.obstacleX = this.ge.width + 100;
		}
		this.obstacleX -= 2;

		this.obstacle.setBounds(this.obstacleX, 0, 100, 100);
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
		if (e.getSource() == this.frameRateInput) {
			this.frameRate = Integer.valueOf(this.frameRateInput.getText()).intValue();
		}
		if (e.getSource() == start) {
			if (!this.running) {
				this.main = new Window("JumpAndLearn", 850, 500, true);
				this.ge = new GraphicsEngine(this.main, this);
				this.frameRate = Integer.valueOf(this.frameRateInput.getText()).intValue();
				ge.start();
				this.running = true;
				this.start.setText("End");
			} else {
				ge.kill();
				try {
					ge.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.main.dispose();
				this.start.setText("Start");
				this.running = false;
			}
		}
		if (e.getSource() == this.reset) {
			ge.kill();
			try {
				ge.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.main.dispose();
			this.start.setText("Start");
			this.running = false;
			initVar();
		}
		if (e.getSource() == this.drawing) {
			this.draw = this.drawing.isSelected();
		}
	}
}
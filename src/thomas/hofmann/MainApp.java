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

public class MainApp implements Engine,ActionListener
{
	Window main = null;
	GraphicsEngine ge;
	
	NeuralNetwork nn;
	
	ArrayList<Double> input;
	ArrayList<Double> output;
	
	Rectangle hitbox;
	Rectangle obstacle;
	double velocityY=0,time=1,gravity=0.1;
	int obstacleX=900;
	int hitboxY=0;
	int frameRate=60;
	int deathCounter=0;
	JTextField frameRateInput;
	JButton start;
	boolean draw=true;
	boolean jumping;
	boolean running=false;
	JToggleButton drawing;
	int i;
	public static void main(String[] args)
	{
		new MainApp().init();
	}
	
	private void init()
	{
		Window controls = new Window("Controls", 250, 200);
		controls.setBounds(0, 0, 250, 200);
		JPanel controlPanel = new JPanel();
		drawing = new JToggleButton("Drawing");
		drawing.setSelected(true);
		drawing.addActionListener(this);
		frameRateInput= new JTextField("60",5);
		start = new JButton("!START!");
		start.addActionListener(this);
		frameRateInput.setHorizontalAlignment(JTextField.CENTER);
		frameRateInput.addActionListener(this);
		controlPanel.add(frameRateInput);
		controlPanel.add(start);
		controlPanel.add(drawing);
		controls.add(controlPanel);
		controls.validate();
		
		input = new ArrayList<>();
		input.add(0.0);
		nn = new NeuralNetwork();
		nn.addInputLayer(1, ActFunctions.TANH);
		nn.addHiddenLayer(4, ActFunctions.TANH);
		nn.addOutputLayer(2, ActFunctions.RELU);
		
		nn.randomizeWeights(0, 1);
		
		hitbox = new Rectangle();
		obstacle = new Rectangle();
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode()==KeyEvent.VK_SPACE)
		{
			if(!jumping)
			{
				jumping=true;
				velocityY=8;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	@Override
	public void draw(Graphics2D g)
	{
		g.fill(obstacle);
		g.draw(hitbox);
	}

	@Override
	public void loop(GraphicsEngine ge)
	{
		input.set(0, (double) obstacleX);
		output=nn.compute(input);
		if(output.get(0)>output.get(1))
		{
			if(!jumping)
			{
				jumping=true;
				velocityY=8;
			}
		}
		
		time +=0.02;
		if(obstacleX<-100)
		{
			obstacleX=ge.width+100;
		}
		obstacleX-=5;
		
		hitboxY += velocityY * time;
	    velocityY -= gravity * time;
	    
	    if(hitboxY<0)
	    {
	    	velocityY=0;
	    	hitboxY=0;
	    	time=1;
	    	jumping=false;
	    }
	    
	    hitbox.setBounds(10, hitboxY, 75, 75);
	    obstacle.setBounds(obstacleX, 0, 100, 100);
	    
		if(hitbox.intersects(obstacle))
		{
			obstacleX=900;
			time=1;
			hitboxY=0;
			velocityY=0;
			jumping=false;
			nn.mutateWeights(0.1);
			nn.mutateBias(0.1);
			if(deathCounter>250)
			{
				nn.randomizeWeights(-1, 5);
				deathCounter=0;
			}
			deathCounter++;
			if(draw)
			{
				System.out.println(deathCounter);
			}
		}
	    
		if(draw)
		{
			ge.redraw();
		}
		ge.capFrameRate(frameRate);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==frameRateInput)
		{
			frameRate=Integer.valueOf(frameRateInput.getText()).intValue();
		}
		if(e.getSource()==start)
		{
			if(!running)
			{
				main = new Window("JumpAndLearn", 1000, 600, true);
				ge = new GraphicsEngine(main, this);
				frameRate=Integer.valueOf(frameRateInput.getText()).intValue();
				ge.start();
				running=true;
				start.setText("End");
			}else
			{
				
				obstacleX=900;
				this.main.dispose();
				start.setText("Start");
				running=false;
				ge.stop();
			}
		}
		if(e.getSource()==drawing)
		{
			draw=drawing.isSelected();
		}
	}
}
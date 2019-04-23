package thomas.hofmann;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class MainApp implements Engine,ActionListener
{
	int frameRate=60;
	JTextField frameRateInput;
	JButton start;
	boolean draw=true;
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
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		
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
		g.fillRect(i, 0, 100, 100);
	}

	@Override
	public void loop(GraphicsEngine ge)
	{
		i++;
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
				Window main = new Window("JumpAndLearn", 1000, 600, true);
				GraphicsEngine ge = new GraphicsEngine(main, this);
				frameRate=Integer.valueOf(frameRateInput.getText()).intValue();
				ge.start();
				running=true;
			}
		}
		if(e.getSource()==drawing)
		{
			draw=drawing.isSelected();
		}
	}
}
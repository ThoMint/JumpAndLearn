package thomas.hofmann;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Agent
{
	private ArrayList<Double> nnInput;
	private ArrayList<Double> nnOutput;

	private NeuralNetwork brain;
	private Double fitness;
	
	private Rectangle hitbox;
	private Double verticalVel;
	private Double verticalPos;
	private Double horizontalPos;
	private Double time;
	private Double gravity;
	
	private Boolean enabled;
	private Boolean jumping;

	public Agent(double gravity)
	{
		this.nnInput = new ArrayList<>();
		this.nnOutput = new ArrayList<>();
		
		this.brain = new NeuralNetwork();
		
		this.hitbox = new Rectangle();
		this.verticalVel = new Double(0.0);
		this.verticalPos = new Double(0.0);
		this.horizontalPos = Math.random()*20;
		this.time = new Double(1.0);
		this.gravity = gravity;
		
		this.enabled = new Boolean(true);
		this.jumping = new Boolean(true);
	}
	
	public void createBrainStructure(int inputNodes, int hiddenNodes, int outputNodes)
	{
		this.brain.addInputLayer(inputNodes, ActFunctions.TANH);
		this.brain.addHiddenLayer(hiddenNodes, ActFunctions.SIGMOID);
		this.brain.addOutputLayer(outputNodes, ActFunctions.RELU);
		
		this.brain.randomizeWeights(-0.5, 0.5);
		this.brain.mutateBias(0.5);
	}
	
	public void mutate(double mutationRate)
	{
		this.brain.mutateWeights(mutationRate);
		this.brain.mutateBias(mutationRate);
	}
	
	public ArrayList<Double> process(ArrayList<Double> input)
	{
		return brain.compute(input);
	}
	
	public void updateHitbox()
	{
		this.hitbox.setFrame(horizontalPos, this.verticalPos, 75, 75);
	}

	public Double getVerticalVel()
	{
		return verticalVel;
	}

	public void setVerticalVel(Double verticalVel)
	{
		this.verticalVel = verticalVel;
	}

	public Double getGravity() {
		return gravity;
	}

	public void setGravity(Double gravity) {
		this.gravity = gravity;
	}

	public Boolean getJumping() {
		return jumping;
	}

	public void setJumping(Boolean jumping) {
		this.jumping = jumping;
	}

	public Double getVerticalPos()
	{
		return verticalPos;
	}

	public void setVerticalPos(Double verticalPos)
	{
		this.verticalPos = verticalPos;
	}

	public Double getTime()
	{
		return time;
	}

	public void setTime(Double time)
	{
		this.time = time;
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox)
	{
		this.hitbox = hitbox;
	}
	
	public Boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(Boolean enabled)
	{
		this.enabled = enabled;
	}
}

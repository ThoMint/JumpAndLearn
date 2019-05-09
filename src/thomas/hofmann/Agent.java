package thomas.hofmann;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Agent {
	public NeuralNetwork brain;
	public Double fitness;
	public Rectangle hitbox;
	public Double verticalVel;
	public Double verticalPos;
	public Double horizontalPos;
	public Double time;
	public Boolean enabled;
	public Boolean jumping;
	public Double traveled;

	public Agent() {
		brain = new NeuralNetwork();
		hitbox = new Rectangle();
		verticalVel = new Double(0.0);
		verticalPos = new Double(0.0);
		horizontalPos = Math.random() * 20;
		time = new Double(1.0);
		enabled = new Boolean(true);
		jumping = new Boolean(true);
		fitness = new Double(0.0);
		traveled = new Double(0.0);
	}

	public void createBrainStructure(int inputNodes, int hiddenNodes, int outputNodes) {
		brain.addInputLayer(inputNodes, ActFunctions.TANH);
		brain.addHiddenLayer(hiddenNodes, ActFunctions.SIGMOID);
		brain.addOutputLayer(outputNodes, ActFunctions.RELU);
		brain.randomizeWeights(-0.5, 0.5);
		brain.mutateBias(0.5);
	}

	public void mutate(double mutationRate) {
		brain.mutateWeights(mutationRate);
		brain.mutateBias(mutationRate);
	}

	public ArrayList<Double> process(ArrayList<Double> input) {
		return brain.compute(input);
	}

	public void updateHitbox() {
		hitbox.setFrame(horizontalPos, verticalPos, 75, 75);
	}

	public void calculateFitness() {
		fitness=traveled;
	}
}
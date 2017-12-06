package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class Fitness extends ViewableAtomic {
	
	double theta = 0;
	double delta_theta = 0;
	double radius = 0.5;
	double variance = 0.1;
	Population pop;
	
	public Fitness() {
		this("Fitness");
	}
	
	public Fitness(String name) {
		super(name);
		addInport("in_population");
		addInport("variance");
		addInport("delta_theta");
		addInport("stop");
		addOutport("out_population");
	}
	
	public void initialize() {
		phase = "passive";
		super.initialize();
	}
	
	public double fitness(double x, double y, double r, double theta, double var) {
		double ret, x_part, y_part, pow;
		x_part = Math.pow((x - r*Math.cos(theta) - 0.5), 2) / (2*Math.pow(var, 2));
		y_part = Math.pow((y - r*Math.sin(theta) - 0.5), 2) / (2*Math.pow(var, 2));
		pow = (-1.0) * (x_part + y_part);
		ret = Math.pow(Math.E, pow);
		ret = Math.pow(ret, pop.selection_strength)
		return ret;
	}
	
	public void evaluate_fitness() {
		double x, y;	
		for (Individual i : pop.population) {
			x = i.get_x_coord();
			y = i.get_y_coord();
			i.setFitness(fitness(x, y, radius, theta, variance));
		}
		
		theta += delta_theta; // rotate fitness function in advance of next call
	}
	
	public void deltext(double e, message x) {
		Continue(e);

		// if the message is on the delta_theta port, set delta_theta;
		// else take in a population, iterate over individuals, extract x and y, evaluate fitness, and update population
		if (messageOnPort(x, "delta_theta", 0)) {
			delta_theta = Float.parseFloat(x.getValOnPort("delta_theta", 0).toString());
		}
		
		if (messageOnPort(x, "stop", 0)) {
			phase = "stop";
		}
		
		if (!phaseIs("stop"))
		{
			if (messageOnPort(x, "in_population", 0)) {
				String temp = x.getValOnPort("in_population", 0).toString();
				Object returned = ObjectUtil.deserializeObjectFromString(temp);
				pop = new  Population();
				pop.set_values((Population) returned);		
				evaluate_fitness();
				
				// if the phase is set to stop, then cease the loop and set to passive.
				// else, trigger output
				holdIn("active", 0);
			}
		}	
	}
	
	public void deltint() {
		passivate();
	}
	
	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}
	
	public message out() {
		message m = new message();
		content con;
		String serialized = "";
		serialized = ObjectUtil.serializeObjectToString(pop);
		con = makeContent("out_population", new entity(serialized));
		m.add(con);
		return m;
	}
	
	public void showState() {
		super.showState();
	}
	
	public String getToolTipText() {
		return super.getTooltipText();
	}
	
}

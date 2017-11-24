package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;
import Math

public class Fitness extends ViewableAtomic {
	
	double theta = 0;
	double delta_theta = 0;
	double radius = 0.5;
	Population pop = new Population();
	
	public Fitness() {
		this("Fitness");
	}
	
	public Fitness(String name) {
		super(name);
		addInport("in_population");
		addInport("delta_theta");
		addOutport("out_population");
	}
	
	public void initialize() {
		phase = "passive";
		super.initialize();
	}
	
	public double fitness(double x, double y, double r, double theta) {
		double ret, x_part, y_part, pow;
		x_part = Math.pow((x - r*Math.cos(theta) - 0.5), 2) / 2;
		y_part = Math.pow((y - r*Math.cos(theta) - 0.5), 2) / 2;
		pow = (-1.0) * (x_part + y_part);
		ret = Math.pow(Math.E, pow);
		return ret;
	}
	
	public void evaluate_fitness() {
		double x, y;
		
		for (Individual i : pop.population)
		{
			x = i.get_x_coord();
			y = i.get_y_coord();
			i.setFitness(this.fitness(x, y, radius, theta));
		}
		
		theta += delta_theta; // rotate fitness fcn in advance of next call
	}
	
	public void deltext(double e, message x) {
		Continue(e);

		// if the message is on the delta_theta port, set delta_theta;
		// else take in a population, iterate over individuals, extract x and y, eval fitness, and update population
			if(messageOnPort(x, "in_population", 0)) {
				String temp = x.getValOnPort("in_population", 0);
				Object returned = ObjectUtil.deserializeObjectFromString(temp);
				Population p = (Population) returned;
				pop = p;			
				evaluate_fitness();
			}
			if (messageOnPort(x, "delta_theta", 0)) {
					delta_theta = x.getValOnPort("delta_theta", 0);
				}
			}
	}
	
	public void deltint() {
		passivate();
	}
	
	public void deltcon(double e, message x) {
		System.out.println("confluent");
		deltint();
		deltext(0, x);
	}
	
	public message out() {
		message m = new message();
		content con;		
		String serialized = ObjectUtil.serializeObjectToString(pop);
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


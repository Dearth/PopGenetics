package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class Mutator extends ViewableAtomic {
	
	double p_mutate;
	Population pop;
	
	public Mutator() {
		this("Mutator", 0.0);
	}
	
	public Mutator(String name, double p_mutate) {
		super(name);
		addInport("in_population");
		addInport("p_mutate");
		addOutport("out_population");
	}
	
	public void initialize() {
		phase = "passive";
		super.initialize();
	}
	
	public void deltext(double e, message x) {
		Continue(e);

		// if the message is on the delta_theta port, set delta_theta;
		// else take in a population, iterate over individuals, extract x and y, evaluate fitness, and update population
		if (messageOnPort(x, "p_mutate", 0)) {
			p_mutate = Float.parseFloat(x.getValOnPort("p_mutate", 0).toString());
		}
			
		if (messageOnPort(x, "in_population", 0)) {
			String temp = x.getValOnPort("in_population", 0).toString();
			Object returned = ObjectUtil.deserializeObjectFromString(temp);
			pop = (Population) returned;
			
			pop.mutation_rate = p_mutate;
			pop.mutatePopulation();
			
			holdIn("active", 0);
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

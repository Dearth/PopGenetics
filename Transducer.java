package PopGenetics;

import GenCol.*;

import model.modeling.*;

import view.modeling.ViewableAtomic;

public class Transducer extends ViewableAtomic {
	
	Population pop;
	Population storage[];
	int generation = 0;
	
	public Transducer() {
		this("Transducer", 1000);
	}
	
	public Transducer(String name, int generations) {
		super(name);
		storage = new Population[generations];
		addInport("in_population");
		addOutport("out_population");
	}
	
	public void initialize() {
		super.initialize();
	}
	
	public void deltext(double e, message x) {
		Continue(e);
		
		// if the message is on the delta_theta port, set delta_theta;
		// else take in a population, iterate over individuals, extract x and y, evaluate fitness, and update population
			
		if (messageOnPort(x, "in_population", 0)) {
			String temp = x.getValOnPort("in_population", 0).toString();
			Object returned = ObjectUtil.deserializeObjectFromString(temp);
			pop = (Population) returned;
			storage[generation] = pop;
			generation += 1;
		}
		if(generation == 999)
		{
			// write data structure to file
			ObjectMapper mapper = new ObjectMapper();
		}
	}
	
	public void deltint() {
		passivate();
		phase = "waiting";
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




























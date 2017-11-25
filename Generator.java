package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class Generator extends ViewableAtomic {

	// variable for sequencing order of internal transition states and thus outputs
	protected int count;
	protected Population p;
	protected double delta_theta;

	public Generator() { // default constructor
		this("Generator", 0);
	}

	public Generator(String name, double delta_theta) {
		super(name);
		this.delta_theta = delta_theta;
		addOutport("out_population");
		addOutport("delta_theta");
	}

	public void initialize() {
		holdIn("active", 0);

		//initial value 
		count = 0;
		p = new Population();
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
	}

	//time scheduler for producing outputs
	public void deltint() {

		if (phaseIs("active")) {

			if (count == 0)
				holdIn("active", 1);
			else if (count == 1)
				holdIn("active", 1);
			//stops scheduling of outputs
			else
				passivate();
			
			count = count + 1;
		}
	}

	public message out() {

		//creating empty message
		message m = new message();
		
		//default content is needed  
		content con = makeContent("out_population", new entity("none"));
		
		if (count == 0)
			con = makeContent("delta_theta", new entity(String.valueOf(delta_theta)));
		else if (count == 1)
			con = makeContent("out_population", new entity(ObjectUtil.serializeObjectToString(p)));

		m.add(con);

		return m;
	}

	public void showState() {
		super.showState();
	}

	public String getTooltipText() {
		return super.getTooltipText() + "\n"  + " count: " + count;
	}

}

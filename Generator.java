package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;

public class Generator extends ViewableAtomic {

	// variable for sequencing order of internal transition states and thus outputs
	protected int count;
	protected Population p = new Population();
	protected double delta_theta;
	protected double p_crossover;
	protected double selective_pressure;
	protected double p_mutate;
	protected String filename_to_save;
	protected double variance;

	public Generator() { // default constructor
		this("Generator", 0, 0.1, 1, 0.0, "default", 0.1);
	}

	public Generator(String name, double delta_theta, double p_crossover, double selective_pressure, double p_mutate, String filename_to_save, double variance) {
		super(name);
		this.delta_theta = delta_theta;
		this.p_crossover = p_crossover;
		this.selective_pressure = selective_pressure;
		this.p_mutate = p_mutate;
		this.filename_to_save = filename_to_save;
		this.variance = variance;
		addOutport("out_population");
		addOutport("delta_theta");
		addOutport("p_crossover");
		addOutport("selective_pressure");
		addOutport("p_mutate");
		addOutport("filename");
		addOutport("variance");
	}

	public void initialize() {
		holdIn("active", 0);
		//initial value 
		count = 0;
		super.initialize();
	}

	public void deltext(double e, message x) {
		Continue(e);
	}

	//time scheduler for producing outputs
	public void deltint() {

		if (phaseIs("active")) {

			if (count < 7)
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
		content con; // = makeContent("out_population", new entity("none"));
		
		if (count == 0)
			con = makeContent("delta_theta", new entity(String.valueOf(delta_theta)));
		else if (count == 1)
			con = makeContent("p_crossover", new entity(String.valueOf(p_crossover)));
		else if (count == 2)
			con = makeContent("selective_pressure", new entity(String.valueOf(selective_pressure)));
		else if (count == 3)
			con = makeContent("p_mutate", new entity(String.valueOf(p_mutate)));
		else if (count == 4)
			con = makeContent("variance", new entity(String.valueOf(variance)));
		else if (count == 5)
			con = makeContent("filename", new entity(filename_to_save));
		else // if (count == 5)
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

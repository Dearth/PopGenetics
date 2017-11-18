/**
 * DEVS-Suite Simulator
 * Arizona Center for Integrative Modeling & Simulation
 * Arizona State University, Tempe, AZ, USA
 *
 * Author(s): H.S. Sarjoughian & C. Zhang
 */

package Hw4;

import GenCol.*;

import model.modeling.*;

import view.modeling.ViewableAtomic;

public class Generator extends ViewableAtomic {

	// variable for sequencing order of internal transition states and thus outputs
	protected int count;

	public Generator() {
		this("Requester");
	}

	public Generator(String name) {
		super(name);
		addInport("in");
		addOutport("outGreen");
		addOutport("outRed");
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

			if (count == 0)
				holdIn("active", 3.7);
			else if (count == 1)
				holdIn("active", 6);
			else if (count == 2)
				holdIn("active",5.3);
			else if (count == 3)
				holdIn("active", 6);
			
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
		content con = makeContent("outGreen", new entity("none"));
		
		if (count == 0)
			con = makeContent("outGreen", new entity("True"));
		else if (count == 1)
			con = makeContent("outRed", new entity("True"));
		else if (count == 2)
			con = makeContent("outRed", new entity("True"));
		else if (count == 3)
			con = makeContent("outGreen", new entity("True"));
		else if (count == 4)
			con = makeContent("outRed", new entity("True"));

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

package PopGenetics;

import GenCol.*;

import model.modeling.*;

import view.modeling.ViewableAtomic;

public class Crossover extends ViewableAtomic {
	
	public Crossover() {
		this("MSD");
	}
	
	public Crossover(String name) {
		super(name);
		addInport("inGreen");
		addInport("inRed");
		addOutport("outGreen");
		addOutport("outRed");
		addTestInput("inGreen",new entity("True"));
		addTestInput("inRed",new entity("True"));
	}
	
	public void initialize() {
		phase = "updating";
		sigma = 17.1;
		super.initialize();
	}
	
	public void deltext(double e, message x) {
		Continue(e);
		if (phaseIs("waiting")) {
			if(messageOnPort(x, "inGreen", 0)) {
				if(x.getValOnPort("inGreen", 0).eq("True")) {
					phase = "searching";
					sigma = 9.7;
					System.out.println("Waiting -> Searching");
				}
			}
			if (messageOnPort(x, "inRed", 0)) {
				if (x.getValOnPort("inRed", 0).eq("True")) {
					phase = "updating";
					sigma = 17.1;
					System.out.println("Waiting -> Updating");
				}
			}
		}
		
		if (phaseIs("updating")) {
			if(messageOnPort(x, "inGreen", 0)) {
				if(x.getValOnPort("inGreen", 0).eq("True")) {
					phase = "searching";
					sigma = 9.7;
					System.out.println("Updating -> Searching");
				}
			}
			if (messageOnPort(x, "inRed", 0)) {
				if (x.getValOnPort("inRed", 0).eq("True")) {
					System.out.println("Updating -> Updating");
				}
			}
		}
		
		if (phaseIs("searching")) {
			if(messageOnPort(x, "inGreen", 0)) {
				if(x.getValOnPort("inGreen", 0).eq("True")) {
					System.out.println("Searching -> Searching");
				}
			}
			if (messageOnPort(x, "inRed", 0)) {
				if (x.getValOnPort("inRed", 0).eq("True")) {
					System.out.println("Searching -> Searching");
				}
			}	
		}
	}
	
	public void deltint() {
		passivate();
		phase = "waiting";
	}
	
	public void deltcon(double e, message x) {
		System.out.println("confluent");
		deltint();
		deltext(0, x);
	}
	
	public message out() {
		message m = new message();
		content con;
		
		if (phaseIs("searching")) {
			con = makeContent("outGreen", new entity("GreenLight"));
		} else if (phaseIs("updating")) {
			con = makeContent("outRed", new entity("RedLight"));
		} else {
			con = makeContent("outGreen",new entity("none"));
		}
		
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




























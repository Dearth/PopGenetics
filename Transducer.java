package PopGenetics;

import GenCol.*;
import model.modeling.*;
import view.modeling.ViewableAtomic;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.PropertyAccessor;

import java.io.File;
import java.io.IOException;

public class Transducer extends ViewableAtomic {
	
	Population pop;
	Population storage[];
	int generation_limit;
	int generation = 0;
	
	public Transducer() {
		this("Transducer", 1000);
	}
	
	public Transducer(String name, int generations) {
		super(name);
		storage = new Population[generations];
		this.generation_limit = generations;
		addInport("in_population");
		addOutport("out_population");
		addOutport("stop");
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
			holdIn("active", 0);
			
			if (generation == generation_limit)
			{
				// write data structure to file
				ObjectMapper mapper = new ObjectMapper();
				mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				//Object to JSON in file
				try {
					mapper.writeValue(new File("/home/joe/Downloads/storage.json"), storage);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		if(generation == generation_limit)
		{
			// make a stop message
			con = makeContent("stop", new entity("True"));
		}
		else
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

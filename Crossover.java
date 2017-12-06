package PopGenetics;

import GenCol.*;

import model.modeling.*;

import view.modeling.ViewableAtomic;

public class Crossover extends ViewableAtomic {
	Population current;
	Population next;
	double p_crossover;
	double selective_pressure;
	
	public Crossover() {
		this("Crossover", 0.1, 1);
	}
	
	public Crossover(String name, double p_crossover, double selective_pressure) {
		super(name);
		this.p_crossover = p_crossover;
		this.selective_pressure = selective_pressure;
		addInport("p_crossover");
		addInport("in_population");
		addInport("selective_pressure");
		addOutport("out_population");
	}
	
	public void initialize() {
		phase = "passive";
		super.initialize();
	}
	
	public void deltext(double e, message x) {
		Continue(e);
		if (messageOnPort(x, "p_crossover", 0)) {
			p_crossover = Float.parseFloat(x.getValOnPort("p_crossover", 0).toString());
		}
		
		if (messageOnPort(x, "selective_pressure", 0)) {
			selective_pressure = Float.parseFloat(x.getValOnPort("selective_pressure", 0).toString());
		}
			
		if (messageOnPort(x, "in_population", 0)) {
			String temp = x.getValOnPort("in_population", 0).toString();
			Object returned = ObjectUtil.deserializeObjectFromString(temp);
			current = new  Population();
			current.set_values((Population) returned);		
			
			next = new  Population();
			next.set_values(current);		
			// until new pop is full, pick individuals to cross over w/ prob prop to fitness ^ pressure
			double values[] = new double[current.population.length];
			double sum_of_vals = 0;
			
			// build array of fitness values
			for (int i = 0; i < values.length; i++)
		    {
				values[i] = current.population[i].getFitness();
			}
			
			// apply selective pressure
			for (int i = 0; i < values.length; i++) {
				values[i] = Math.pow(values[i], selective_pressure);
				sum_of_vals += values[i];
			}
			
			// normalize
			for (int i = 0; i < values.length; i++) {
				values[i] /= sum_of_vals;
			}
			
			double total_of_values = 0.0;
			for(double d : values)
			{
//				System.out.println("debug: value is " + String.valueOf(d));
				total_of_values += d;
			}
//			System.out.println("Debug: normalized sum of values is " + String.valueOf(total_of_values));

//			System.out.println("Debug: sum normalized by is " + String.valueOf(sum_of_vals));
			
			// with prob p_crossover, cross over two individuals
			// with prob 1-p_crossover, select a single individual and pass it along
			int filled_up = 0;
			double cross;
			double selector;
			double sum;
			int selected;
			for (int i = 0; i < 100; i++)
			{
				Individual a;
				Individual b;
				selected = 0;
				cross = Math.random();
				if(cross < p_crossover)
				{
					// pick two things and cross them over
					selector = Math.random();
					sum = 0;
					for (int j = 0; j < values.length; j++) {
					    sum += values[j];
					    if (selector <= sum) {
					        selected = j;
					        break;
					    }
					}
					a = new Individual();
					a.set_values(current.population[selected]);
//					System.out.println("debug: a is " + String.valueOf(selected));
					
					
					selector = Math.random();
					sum = 0;
					for (int j = 0; j < values.length; j++) {
					    sum += values[j];
					    if (selector <= sum) {
					        selected = j;
					        break;
					    }
					}
					b = new Individual();
					b.set_values(current.population[selected]);
//					System.out.println("debug: b is " + String.valueOf(selected));

					
					next.set_individual(filled_up, a.singlePointCrossover(b));
					filled_up += 1;
					
				}
				else
				{
					// pick one and return it
					selector = Math.random();
					sum = 0;
					for (int j = 0; j < values.length; j++) {
					    sum += values[j];
					    if (selector <= sum) {
					        selected = j;
							a = new Individual();
							a.set_values(current.population[selected]);
							break;
					    }
					}
					next.set_individual(filled_up, current.population[selected]);
//					System.out.println("debug: a is " + String.valueOf(selected));
					filled_up += 1;
				}
			}
			
			// now that we are done, increment generation #
			next.incrementGeneration();
			// then output, right away
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
		serialized = ObjectUtil.serializeObjectToString(next);
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

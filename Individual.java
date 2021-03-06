package PopGenetics;

import java.util.Random;
import java.io.Serializable;

public class Individual implements Serializable, Comparable<Individual>{
	private static final long serialVersionUID = 1L;
	protected double fitness;
	protected boolean x_genome[];
	protected boolean y_genome[];
	protected double y_coord;
	protected double x_coord;
	protected Random random;
	
	public Individual(int size) {
		fitness = 0.0;
		x_genome = new boolean[size];
		y_genome = new boolean[size];
		random = new Random();
		int x_limit = random.nextInt(size);
		int y_limit = random.nextInt(size);
		
		// set a uniform random # of true per array
		for (int i = 0; i < size; i++) {
			if(i < x_limit)
				x_genome[i] = true;
			else if (i >= y_limit)
				x_genome[i] = false;
			
			if(i < y_limit)		
				y_genome[i] = true;
			else if (i >= y_limit)
				y_genome[i] = false;
		}
	}

	public Individual() {
		this(20);
	}
	
	public boolean[] get_x_genome() {
		return this.x_genome;
	}
	
	public double get_x_coord() {
		int sum = 0;
		for(boolean b : x_genome) {
		    sum += b ? 1 : 0;
		}
		this.x_coord = (double) sum / (double) x_genome.length;
		return this.x_coord;
	}
	
	public boolean[] get_y_genome() {
		return this.y_genome;
	}
	
	public double get_y_coord() {
		int sum = 0;
		for(boolean b : y_genome) {
		    sum += b ? 1 : 0;
		}
		this.y_coord = (double) sum / (double) y_genome.length;
		return this.y_coord;
	}
	
	public void set_x_genome(boolean[] a) {
		x_genome = a.clone();
	}
	
	public void set_y_genome(boolean[] a) {
		y_genome = a.clone();
	}
	
	public void set_x_coord(double x){
		x_coord = x;
	}
	
	public void set_y_coord(double y) {
		y_coord = y;
	}
	
	public void setFitness(double fit) {
		fitness = fit;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void set_values(Individual other) {
		fitness = other.fitness;
		x_genome = other.x_genome.clone();
		y_genome = other.y_genome.clone();
		y_coord = other.y_coord;
		x_coord = other.x_coord;
		random = other.random;
	}
	
	public void mutate(double mutation_rate)
	{
		// assumes that the genome length is same for x and y
		for (int i = 0; i < x_genome.length; i++) {
			if(random.nextDouble() < mutation_rate) {
				x_genome[i] = random.nextBoolean();
			}
			if(random.nextDouble() < mutation_rate) {
				y_genome[i] = random.nextBoolean();
			}
		}
	}
	
	public Individual singlePointCrossover(Individual i) {
		Individual t = new Individual();
		int index = random.nextInt(x_genome.length);
		
		for(int j = index; j < x_genome.length; j++) {
			if (j < index) {
				t.x_genome[j] = this.x_genome[j]; 
			} else {
				t.x_genome[j] = i.x_genome[j];
			}
		}
		
		index = random.nextInt(y_genome.length);
		
		for(int j = index; j < y_genome.length; j++) {
			if(j < index) {
				t.y_genome[j] = this.y_genome[j];
			} else {
				t.y_genome[j] = i.y_genome[j];
			}
		}
		
		t.fitness = 0;
		return t;
	}
	
	public Individual uniformPointCrossover(Individual i) {
		Individual t = new Individual();
		
		for(int j = 0; j < x_genome.length; j++) {
			if (random.nextBoolean()) {
				t.x_genome[j] = this.x_genome[j];
			} else {
				t.x_genome[j] = i.x_genome[j];
			}
		}
		
		for(int j = 0; j < y_genome.length; j++) {
			if(random.nextBoolean()) {
				t.y_genome[j] = this.y_genome[j];
			} else {
				t.y_genome[j] = i.y_genome[j];
			}
		}
		
		t.fitness = 0;
		return t;
	}
	
	@Override
	public int compareTo(Individual i) {
        return Double.compare(this.fitness, i.fitness);
	}
}







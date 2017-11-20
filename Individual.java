package PopGenetics;

import java.util.Random;

public class Individual implements Comparable<Individual>{
	protected int fitness;
	protected boolean x_genome[];
	protected boolean y_genome[];
	protected Random random;
	
	public Individual(int size) {
		fitness = 0;
		x_genome = new boolean[size];
		y_genome = new boolean[size];
		random = new Random();
		
		for (int i = 0; i < size; i++) {
			x_genome[i] = random.nextBoolean();
			y_genome[i] = random.nextBoolean();
		}
	}

	public Individual() {
		this(20);
	}
	
	public boolean[] get_x_genome() {
		return this.x_genome;
	}
	
	public boolean[] get_y_genome() {
		return this.y_genome;
	}
	
	public void getFitness(int fit) {
		this.fitness = fit;
	}
	
	public void mutate_x_genome() {
		int index = random.nextInt(x_genome.length);
		x_genome[index] = random.nextBoolean();
	}
	
	public void mutate_y_genome() {
		int index = random.nextInt(y_genome.length);
		y_genome[index] = random.nextBoolean();
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
		return this.fitness - i.fitness;
	}
}







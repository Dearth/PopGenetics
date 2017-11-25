package PopGenetics;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Population implements Serializable{
	protected int generation;
	protected double mutation_rate;
	protected double selection_strength;
	protected Individual population[];
	protected Random random;
	
	public Population(int pop_size, double mut_rate, double s_str) {
		generation = 0;
		population = new Individual[pop_size];
		random = new Random();
		mutation_rate = mut_rate;
		selection_strength = s_str;
	}
	
	public Population() {
		this(100, 0.1, 1.0);
	}
	
	public void sortPopulation() {
		Arrays.sort(this.population);
	}
	
	public void setGeneration(int g) {
		this.generation = g;
	}
	
	public void setMutationRate(double r) {
		this.mutation_rate = r;
	}
	
	public void setSelectionStrength(double s) {
		this.selection_strength = s;
	}
	
	public void mutatePopulation() {
		for (int i = 0; i < this.population.length; i++) {
			this.population[i].mutate(this.mutation_rate);
			}
	}
		
}

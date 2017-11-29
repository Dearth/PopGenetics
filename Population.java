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
	static final long serialVersionUID = 0;
	
	public Population(int pop_size, double mut_rate, double s_str) {
		generation = 0;
		population = new Individual[pop_size];
		for (int i = 0; i < pop_size; i++)
		{
			population[i] = new Individual();
		}
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
	
	public void incrementGeneration() {
		this.generation += 1;
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
	
	public void set_values(Population other) {
		generation = other.generation;
		mutation_rate = other.mutation_rate;
		selection_strength = other.selection_strength;
		population = other.population.clone();
		random = other.random;
	}
	
	public void set_individual(int index, Individual to_copy) {
		population[index].setFitness(to_copy.getFitness());
		population[index].set_x_coord(to_copy.get_x_coord());
		population[index].set_x_genome(to_copy.get_x_genome());
		population[index].set_y_coord(to_copy.get_y_coord());
		population[index].set_y_genome(to_copy.get_y_genome());		
	}
		
}

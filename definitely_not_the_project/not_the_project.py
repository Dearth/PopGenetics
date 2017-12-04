#!/bin/python

import numpy as np
import math
import matplotlib.pyplot as plt
import copy
import random
# import pickle


class Individual:
    def __init__(self, fit_var=0.1, fitness=0.0, mut_rate=0.0, fit_r=0.0, fit_delta=0.0, fit_theta=0.0,
                 select_pres=1.0):
        self.fitness = fitness
        self.normal_fitness = 0.0
        self.mut_rate = mut_rate
        self.fit_delta = fit_delta
        self.fit_r = fit_r
        self.fit_var = fit_var
        self.fit_theta = fit_theta
        self.select_pres = select_pres
        
        self.x_genome = np.zeros(20)
        self.y_genome = np.zeros(20)
        x_genome_ones = np.random.randint(0, high=20)
        y_genome_ones = np.random.randint(0, high=20)

        for i in range(x_genome_ones):
            self.x_genome[i] = 1

        for i in range(y_genome_ones):
            self.y_genome[i] = 1

        random.shuffle(self.x_genome)
        random.shuffle(self.y_genome)

    def mutate(self):
        for i in range(len(self.x_genome)):
            rand = np.random.random()
            if rand < self.mut_rate:
                self.x_genome[i] = np.random.choice([0, 1])

        for i in range(len(self.y_genome)):
            rand = np.random.random()
            if rand < self.mut_rate:
                self.y_genome[i] = np.random.choice([0, 1])

    def one_point_crossover(self, other):
        temp = copy.deepcopy(other)
        index = np.random.randint(0, high=len(self.x_genome))

        for i in range(len(self.x_genome)):
            if i < index:
                temp.x_genome[i] = self.x_genome[i]
            else:
                temp.x_genome[i] = other.x_genome[i]

        index = np.random.randint(0, high=len(self.y_genome))
        for i in range(len(self.y_genome)):
            if i < index:
                temp.y_genome[i] = self.y_genome[i]
            else:
                temp.y_genome[i] = other.y_genome[i]

        return temp

    def evaluate_fitness(self):
        x_coord = sum(self.x_genome) / 20.0
        y_coord = sum(self.y_genome) / 20.0

        x_offset = 0.5 + self.fit_r * math.cos(self.fit_theta)
        y_offset = 0.5 + self.fit_r * math.sin(self.fit_theta)

        x_val = math.pow((x_coord - x_offset), 2) / (2.0 * math.pow(self.fit_var, 2))
        y_val = math.pow((y_coord - y_offset), 2) / (2.0 * math.pow(self.fit_var, 2))

        self.fit_theta = self.fit_theta + self.fit_delta

        self.fitness = math.pow(math.exp(-(x_val + y_val)), self.select_pres)
        return x_offset, y_offset


class Population:
    def __init__(self, size=100, fit_var=0.1, mut_rate=0.0, co_rate=0.1, fit_r=0.0, fit_delta=0.0, select_pres=1.0):
        self.co_rate = co_rate
        self.population = []
        self.fitness_y = []
        self.fitness_x = []

        for i in range(size):
            temp = Individual(mut_rate=mut_rate, fit_r=fit_r, fit_delta=fit_delta, fit_var=fit_var,
                              select_pres=select_pres)
            self.population.append(copy.deepcopy(temp))

    def mutate_population(self):
        for i in range(len(self.population)):
            self.population[i].mutate()

    def normalize_fitness(self):
        s = 0.0
        for i in range(len(self.population)):
            s += self.population[i].fitness

        for i in range(len(self.population)):
            self.population[i].normal_fitness = self.population[i].fitness / s

    def evaluate_fitness(self):
        x = 0.0
        y = 0.0
        for i in range(len(self.population)):
            x, y = self.population[i].evaluate_fitness()
        
        self.fitness_x.append(x)
        self.fitness_y.append(y)
        self.normalize_fitness()

    def fitness_proportionate_selection(self):
        rand = np.random.random()
        s = 0.0

        for i in range(len(self.population)):
            s += self.population[i].normal_fitness

            if s >= rand:
                return i

        return len(self.population) - 1

    def select_and_crossover(self):
        temp_pop = []

        for i in range(len(self.population)):
            rand = np.random.random()

            if rand < self.co_rate:
                i1 = self.fitness_proportionate_selection()
                i2 = self.fitness_proportionate_selection()

                temp_pop.append(copy.deepcopy(self.population[i1].one_point_crossover(self.population[i2])))

            else:
                i1 = self.fitness_proportionate_selection()
                temp_pop.append(copy.deepcopy(self.population[i1]))

        self.population = copy.deepcopy(temp_pop)


def get_fitness(data, generations=1000, individuals=1000):
    fit = np.zeros(generations)

    for i in range(generations):
        s = 0.0
        for j in range(individuals):
            s += float(data[i].population[j].fitness)
        fit[i] = s / individuals

    return fit


def get_loci_entropy(data, generations=1000, individuals=1000, genome_size=20):
    x_entropy = []
    y_entropy = []

    for i in range(generations):
        x_entropy_sum = 0.0
        y_entropy_sum = 0.0
        for k in range(genome_size):
            x_ones_count = 0.0
            y_ones_count = 0.0
            for j in range(individuals):
                x_ones_count += data[i].population[j].x_genome[k]    
                y_ones_count += data[i].population[j].y_genome[k]

            if 0 < x_ones_count < individuals:
                p = x_ones_count / individuals
                x_entropy_sum += -p*math.log(p, 2) - (1-p)*math.log(1-p, 2)
            if 0 < y_ones_count < individuals:
                p = y_ones_count / individuals
                y_entropy_sum += -p*math.log(p, 2) - (1-p)*math.log(1-p, 2)

        x_entropy.append(x_entropy_sum/genome_size)
        y_entropy.append(y_entropy_sum/genome_size)

    return x_entropy, y_entropy


def get_genome_entropy(data, generations=1000, individuals=1000):
    x_entropy = []
    y_entropy = []
    x_e_sum = 0.0
    y_e_sum = 0.0

    for i in range(generations):
        pop_x_entropy = dict()
        pop_y_entropy = dict()
        for j in range(individuals):
            x = str(data[i].population[j].x_genome)
            y = str(data[i].population[j].y_genome)
    
            pop_x_entropy[x] = pop_x_entropy.get(x, 0.0) + 1.0
            pop_y_entropy[y] = pop_y_entropy.get(y, 0.0) + 1.0

        for k in pop_x_entropy:
            x_e_sum = 0.0
            x_p = pop_x_entropy.get(k, 0.0) / individuals
         
            if x_p > 0:
                x_e_sum += -x_p*math.log(x_p, 2) 
         
        for k in pop_y_entropy:
            y_e_sum = 0.0
            y_p = pop_y_entropy.get(k, 0.0) / individuals
            
            if y_p > 0:
                y_e_sum += -y_p*math.log(y_p, 2) 

        x_entropy.append(x_e_sum)
        y_entropy.append(y_e_sum)

    return x_entropy, y_entropy


def get_location_entropy(data, generations=1000, individuals=1000):
    x_entropy = []
    y_entropy = []
    x_e_sum = 0.0
    y_e_sum = 0.0

    for i in range(generations):
        pop_x_entropy = dict()
        pop_y_entropy = dict()
        for j in range(individuals):
            x = sum(data[i].population[j].x_genome)
            y = sum(data[i].population[j].y_genome)

            pop_x_entropy[x] = pop_x_entropy.get(x, 0.0) + 1.0
            pop_y_entropy[y] = pop_y_entropy.get(y, 0.0) + 1.0

        for k in pop_x_entropy:
            x_e_sum = 0.0

            x_p = pop_x_entropy.get(k, 0.0) / individuals

            if x_p > 0:
                x_e_sum += -x_p*math.log(x_p, 2)

        for k in pop_y_entropy:
            y_e_sum = 0.0
            
            y_p = pop_y_entropy.get(k, 0.0) / individuals
            
            if y_p > 0:
                y_e_sum += -y_p*math.log(y_p, 2)

        x_entropy.append(x_e_sum)
        y_entropy.append(y_e_sum)

    return x_entropy, y_entropy


def plot_fitness(filename, data, generations=1000):
    x = data[generations - 1].fitness_x
    y = data[generations - 1].fitness_y

    plt.figure(1)
    plt.scatter(x, y)
    plt.savefig("./graphs/" + filename + "_landscape.png")
    plt.clf()


def gen_graphs(filename, data):
    print("Processing " + filename)

    x = range(1000)
    e_loci_x, e_loci_y = get_loci_entropy(data)
    e_loca_x, e_loca_y = get_location_entropy(data)
    e_geno_x, e_geno_y = get_genome_entropy(data)
    f = get_fitness(data)

    plot_fitness(filename, data)

    plt.figure(1)
    plt.plot(x, e_loci_x, x, e_loci_y)
    plt.title('Genetic Loci Entropy over time')
    plt.xlabel("Generation")
    plt.ylabel("Binary Entropy")
    plt.savefig("./graphs/" + filename + "_loci_entropy.png")
    plt.clf()

    plt.figure(1)
    plt.plot(x, e_loca_x, x, e_loca_y)
    plt.title('Location Entropy over time')
    plt.xlabel("Generation")
    plt.ylabel("Binary Entropy")
    plt.savefig("./graphs/" + filename + "_location_entropy.png")
    plt.clf()
 
    plt.figure(1)
    plt.plot(x, e_geno_x, x, e_geno_y)
    plt.title('Genome Entropy over time')
    plt.xlabel("Generation")
    plt.ylabel("Binary Entropy")
    plt.savefig("./graphs/" + filename + "_genom_entropy.png")
    plt.clf()

    plt.figure(1)
    plt.plot(x, f)
    plt.title('Population Fitness over time')
    plt.xlabel("Generation")
    plt.ylabel("Average Fitness")
    plt.savefig("./graphs/" + filename + "_fitness.png")
    plt.clf()


def run_sim():
    m_rates = [0.0, 0.01, 0.1, 1.0]
    fit_deltas = [0.0, 0.0017, 0.017, 0.17]
    select_pres = [0.9, 1.0, 1.1, 2.0]
    sigma = [1.0, 0.25, 0.1]
    r = 0.0

    for m in m_rates:
        for d in fit_deltas:
            for l in select_pres:
                for s in sigma:
                    data = []
                    if d > 0.0:
                        r = 0.25
                    p = Population(size=500, mut_rate=m, select_pres=l, fit_delta=d, fit_r=r, fit_var=s)
                    print("Starting " + str(d) + "_0.1_" + str(l) + "_" + str(m) + "_" + str(s))
                    for i in range(1000):
                        data.append(copy.deepcopy(p))
    
                        p.mutate_population()
                        p.evaluate_fitness()
                        p.select_and_crossover()

                    data.append(p)
                    # with open("./data/" + str(d) + "_0.1_" + str(l) + "_" + str(m) + "_" + str(s) + ".pkl",
                    # 'w') as output:
                    #     pickle.dump(data, output, pickle.HIGHEST_PROTOCOL)
                    gen_graphs(str(d) + "_0.1_" + str(s) + "_" + str(m), data)


if __name__ == "__main__":
    run_sim()

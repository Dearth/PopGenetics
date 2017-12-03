#!/bin/python

import numpy as np
import math
import matplotlib.pyplot as plt
import copy


class Individual:
    def __init__(self, fit_var=0.1, fitness=0.0, mut_rate=0.0, fit_r=0.0, fit_delta=0.0, fit_theta=0.0, select_pres=1.0):
        self.fitness = fitness
        self.normal_fitness = 0.0
        self.mut_rate = mut_rate
        self.fit_delta = fit_delta
        self.fit_r = fit_r
        self.fit_var = fit_var
        self.fit_theta = fit_theta
        self.select_pres = select_pres
        self.x_genome = np.random.choice(a=[0, 1], size=20, p=[0.5, 0.5])
        self.y_genome = np.random.choice(a=[0, 1], size=20, p=[0.5, 0.5])

    def mutate(self):
        for i in range(len(self.x_genome)):
            rand = np.random.random()
            if rand < self.mut_rate:
                self.x_genome[i] = np.random.choice([0,1])

        for i in range(len(self.y_genome)):
            rand = np.random.random()
            if rand < self.mut_rate:
                self.y_genome[i] = np.random.choice([0,1])

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
    def __init__(self, size=100, mut_rate=0.0, co_rate=0.1, fit_r=0.0, fit_delta=0.0, select_pres=1.0):
        self.co_rate = co_rate
        self.population = []
        self.fitness_y = []
        self.fitness_x = []

        for i in range(size):
            temp = Individual(mut_rate=mut_rate, fit_r=fit_r, fit_delta=fit_delta)
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

def get_fitness(data, generations=100, individuals=100):
    fit = np.zeros(generations)

    for i in range(generations):
        s = 0
        for j in range(individuals):
            s += float(data[i].population[j].fitness)
        fit[i] = s / individuals

    return fit


def plot_fitness(filename, data, generations=100, individuals=100):
    x = data[generations - 1].fitness_x
    y = data[generations - 1].fitness_y

    plt.figure(1)
    plt.scatter(x, y)
    plt.savefig("./graphs/" + filename + "_landscape.png")
    plt.clf()


def gen_graphs(filename, data):
    print("Processing " + filename)

    x = range(100)
    # e = get_entropy(data)
    f = get_fitness(data)

    plot_fitness(filename, data)

    # plt.figure(1)
    # plt.plot(x, e)
    # plt.title('Population Entropy over time')
    # plt.xlabel("Generation")
    # plt.ylabel("Binary Entropy")
    # plt.savefig("./graphs/" + filename + "_entropy.png")
    # plt.clf()

    plt.figure(2)
    plt.plot(x, f)
    plt.title('Population Fitness over time')
    plt.xlabel("Generation")
    plt.ylabel("Average Fitness")
    plt.savefig("./graphs/" + filename + "_fitness.png")
    plt.clf()


def run_sim():
    m_rates = [0.0, 0.01, 0.1]
    fit_deltas = [0.0, 0.017, 0.17]
    select_pres = [0.9, 1.0, 1.1]
    r = 0.0

    for m in m_rates:
        for d in fit_deltas:
            for s in select_pres:
                data = []
                if d > 0.0:
                    r = 0.25
                p = Population(mut_rate=m, select_pres=s, fit_delta=d, fit_r=r)
                for i in range(100):
                    data.append(copy.deepcopy(p))

                    p.mutate_population()
                    p.evaluate_fitness()
                    p.select_and_crossover()

                data.append(p)
                gen_graphs(str(d) + "_0.1_" + str(s) + "_" + str(m), data)


if __name__ == "__main__":
    run_sim()

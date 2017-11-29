import json
import os
from pprint import pprint
from math import log

import numpy as np
import numpy.random
import matplotlib.pyplot as plt

# data[generation# (0000 to 0999)]["population"][individual# (000-099)]
# further fields: x_coord, y_coord, _x_genome, _y_genome, fitness
# print(data[0]["population"][000]["_x_coord"])


# fitness seems to be 0. what's the bug there?
# pprint(data)

def get_entropy(data, generations=100, individuals=100, genome_size=20):
    entropy = np.zeros(generations);

    for i in range(generations):
        one_count = 0
        for j in range(individuals):
            for x in range(genome_size):
                one_count += float(data[i]["population"][j]["x_genome"][x])
                one_count += float(data[i]["population"][j]["y_genome"][x])
        
        if 0 == one_count or (2*individuals*genome_size) == one_count:
            entropy[i] = 0
        else:
            p = one_count / (2*individuals*genome_size)
            entropy[i] = -p*log(p,2) - (1.0-p)* log(1.0-p, 2)

    return entropy
        

def get_fitness(data, generations=100, individuals=100):
    fit = np.zeros(generations);

    for i in range(generations):
        s = 0
        for j in range(individuals):
#           print(float(data[i]["population"][j]["fitness"]))
            s += float(data[i]["population"][j]["fitness"])
        fit[i] = s / individuals

    return fit

def gen_graphs():

    for filename in os.listdir("./data/"):
        print("Processing " + filename)

        data = json.load(open("./data/" + filename))

        x = range(100)
        e = get_entropy(data)
        f = get_fitness(data)

        plt.figure(1)
        plt.plot(x, e)
        plt.title('Population Entropy over time')
        plt.xlabel("Generation")
        plt.ylabel("Binary Entropy")
        plt.savefig("./graphs/" + filename +"_entropy.png")
        plt.clf()

        plt.figure(2)
        plt.plot(x, f)
        plt.title('Population Fitness over time')
        plt.xlabel("Generation")
        plt.ylabel("Average Fitness")
        plt.savefig("./graphs/" + filename +"_fitness.png")
        plt.clf()

if __name__ == "__main__":
    gen_graphs()

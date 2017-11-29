import json
from pprint import pprint
import numpy as np
import numpy.random
import matplotlib.pyplot as plt
from matplotlib.ticker import NullFormatter


def plot_heatmap(x, y):
    heatmap, xedges, yedges = np.histogram2d(x, y, bins=50)
    extent = [0, 1, 0, 1]

    plt.clf()

    # plt.imshow(heatmap.T, extent=extent, origin='lower')
    plt.figure(figsize=(10, 10), dpi=80, arr=heatmap.T, extent=extent, origin='lower')
    # plt.show()
    # plt.imsave(arr=heatmap.T, fname=str(index) + ".png", vmin=0, vmax=10, origin='lower')
    plt.savefig(str(index) + ".png", dpi=80)


def plot_hist(x, y, index):
    # the random data

    nullfmt = NullFormatter()  # no labels

    # definitions for the axes
    left, width = 0.1, 0.65
    bottom, height = 0.1, 0.65
    bottom_h = left_h = left + width + 0.02

    rect_scatter = [left, bottom, width, height]
    rect_histx = [left, bottom_h, width, 0.2]
    rect_histy = [left_h, bottom, 0.2, height]

    # start with a rectangular Figure
    plt.figure(1, figsize=(8, 8))

    axScatter = plt.axes(rect_scatter)
    axHistx = plt.axes(rect_histx)
    axHisty = plt.axes(rect_histy)

    # no labels
    axHistx.xaxis.set_major_formatter(nullfmt)
    axHisty.yaxis.set_major_formatter(nullfmt)

    # the scatter plot:
    axScatter.scatter(x, y)

    # now determine nice limits by hand:
    binwidth = 0.1
    xymax = np.max([np.max(np.fabs(x)), np.max(np.fabs(y))])
    lim = (int(xymax / binwidth) + 1) * binwidth

    axScatter.set_xlim((0, 1))
    axScatter.set_ylim((0, 1))

    bins = np.arange(-lim, lim + binwidth, binwidth)
    axHistx.hist(x, bins=bins)
    axHisty.hist(y, bins=bins, orientation='horizontal')

    axHistx.set_xlim(axScatter.get_xlim())
    axHisty.set_ylim(axScatter.get_ylim())

    plt.draw()
    plt.savefig("%02d" % index + ".png")
    print("done with " + str(index))
    plt.clf()
    # plt.show()


def get_coords(generations=100, individuals=100):
    coords = [[(0, 0) for a in range(individuals)] for b in range(generations)]

    for i in range(generations):
        for j in range(individuals):
            coords[i][j] = (data[i]["population"][j]["x_coord"], data[i]["population"][j]["y_coord"])
    return coords

# data = json.load(open('data/0_0.1_0.9_0.0.json'))
data = json.load(open('/home/joe/Downloads/0_0.1_1_0.0.json'))

# data[generation# (0000 to 0999)]["population"][individual# (000-099)]
# further fields: x_coord, y_coord, _x_genome, _y_genome, fitness
# print(data[0]["population"][000]["x_coord"])

# pprint(data)

coordinates = get_coords()

x = [0 for _ in range(100)]
y = [0 for z in range(100)]

for index in range(100):
    # iterate through generation i to get x and y
    for cursor in range(100):
        x[cursor] = coordinates[index][cursor][0]
        y[cursor] = coordinates[index][cursor][1]

    plot_hist(x, y, index)

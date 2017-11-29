import json
from pprint import pprint

# data = json.load(open('data/0_0.1_0.9_0.0.json'))
data = json.load(open('/home/joe/Downloads/0_0.1_1_0.0.json'))

# data[generation# (0000 to 0999)]["population"][individual# (000-099)]
# further fields: x_coord, y_coord, _x_genome, _y_genome, fitness
# print(data[0]["population"][000]["_x_coord"])


# fitness seems to be 0. what's the bug there?
# pprint(data)

def get_coords(generations=100, individuals=100):
    coords = [[(0, 0) for a in range(individuals)] for b in range(generations)]

    for i in range(generations):
        for j in range(individuals):
            coords[i][j] = (data[i]["population"][j]["_x_coord"], data[i]["population"][j]["_y_coord"])
    return coords

coordinates = get_coords()
print(coordinates)

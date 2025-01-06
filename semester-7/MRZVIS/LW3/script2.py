import json
import matplotlib.pyplot as plt
import numpy as np

# Load the JSON data from a file
with open('stat_natural.json', 'r') as file:
    data = json.load(file)

# Extract the values
values = data['stat_iteration']
categories = data['stat_error']

# Фибоначчи
# categories = [3,    4,    5,    6,    7,    8,    9,    10,    11,   12]
# values =     [0.45, 3.25, 0.67, 1.65, 0.04, 0.05, 0.04, 0.006, 0.03, 0.95]

# Натуральные числа
# categories = [3,  4, 5,   6,   7,   8,   9,   10,  11,  12]
# values =     [1.2, 0.6, 0.06, 0.06, 0.05, 0.04, 0.04, 0.04, 0.05, 0.04]

# Степенная функция
# categories = [4,   5,   6,    7,    8,    9,    10,   11,   12,   13]
# values     = [0.9, 0.6, 0.09, 0.08, 0.09, 0.09, 0.09, 0.08, 0.09, 0.08]

# Create the plot
plt.figure()
plt.scatter(categories, values, color='blue', marker='o')
plt.plot(categories, values, color='skyblue')

plt.xlabel('Размер окна')
plt.ylabel('Ошибка')

plt.tight_layout()
plt.show()

# Show the plot
plt.show()

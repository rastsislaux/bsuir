import json
import matplotlib.pyplot as plt
import numpy as np

# Load the JSON data from a file
with open('stat_power.json', 'r') as file:
    data = json.load(file)

# Extract the values
values = data['stat_iteration']
categories = data['stat_error']

filtered_categories = []
filtered_values = []

for category, value in zip(categories, values):
    if not filtered_categories or category <= filtered_categories[-1]:
        filtered_categories.append(category)
        filtered_values.append(value)

filtered_categories = np.array(filtered_categories)
filtered_values = np.array(filtered_values)

sampled_categories = []
sampled_values = []
# Step 2: Uniformly take 100 points
if len(filtered_categories) > 100:
    indices = np.linspace(0, len(filtered_categories) - 1, 100).astype(int)
    sampled_categories = filtered_categories[indices]
    sampled_values = filtered_values[indices]
else:
    sampled_categories = filtered_categories
    sampled_values = filtered_values

sampled_categories = sampled_categories[0:-70]
sampled_values =         sampled_values[0:-70]

# Create the plot
plt.figure()
plt.scatter(sampled_categories, sampled_values, color='blue', marker='o')
plt.plot(sampled_categories, sampled_values, color='skyblue')

plt.title("Для последовательности значений степенной функции")
plt.xlabel('Максимально допустимая ошибка')
plt.ylabel('Кол-во итераций')

plt.tight_layout()
plt.show()

# Show the plot
plt.show()

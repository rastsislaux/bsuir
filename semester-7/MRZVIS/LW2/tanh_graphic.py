import numpy as np
import matplotlib.pyplot as plt

# Generate x values
x = np.linspace(-5, 5, 100)

# Calculate hyperbolic tangent values
y = np.tanh(x)

# Create the plot
plt.figure(figsize=(10, 6))
plt.plot(x, y, label='tanh(x)', color='blue')

# Add title and labels
plt.title('Гиперболический тангенс')
plt.xlabel('x')
plt.ylabel('tanh(x)')

# Add grid and legend
plt.grid(True)
plt.axhline(0, color='black',linewidth=0.5, ls='--')
plt.axvline(0, color='black',linewidth=0.5, ls='--')
plt.legend()

# Show the plot
plt.show()

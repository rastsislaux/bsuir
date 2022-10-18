from operator import le
from numpy import diag, diagflat, dot, tril, triu, zeros_like, allclose, array
from scipy.linalg import solve

def jacobi(A, b, x, n, e):
    D = diag(A)
    R = A - diagflat(D)
    prev = x.copy()
    for i in range(n):
        x = (b - dot(R, x)) / D
        if abs(x[0] - prev[0]) < e and abs(x[1] - prev[1]) < e and abs(x[2] - prev[2]) < e:
            return i, x
        prev = x.copy()
    return "Solution not found"

def seidel(A, b, x, n, e):
    for it_count in range(1, n):
        x_new = zeros_like(x)
        for i in range(A.shape[0]):
            s1 = dot(A[i, :i], x_new[:i])
            s2 = dot(A[i, i + 1 :], x[i + 1 :])
            x_new[i] = (b[i] - s1 - s2) / A[i, i]
        if allclose(x, x_new, rtol=e):
            return it_count, x
        x = x_new
    return "Solution not found"
    

A = array([
    [4.2, -1.7, 1.3],
    [2.1, 3.4, 1.8],
    [1.7, 2.8, 1.9]
])
b = array([2.8, 1.1, 0.7])
x = array([0., 0., 0.])

print("1st task:")
j1 = jacobi(A, b, x, 10000, 1e-2)
j2 = jacobi(A, b, x, 10000, 1e-4)
j3 = jacobi(A, b, x, 10000, 1e-6)

print(f"Jacobi with 10^-2: {j1[1]}, iters: {j1[0]}")
print(f"Jacobi with 10^-4: {j2[1]}, iters: {j2[0]}")
print(f"Jacobi with 10^-6: {j3[1]}, iters: {j3[0]}")
print()

s1 = seidel(A, b, x, 10000, 1e-2)
s2 = seidel(A, b, x, 10000, 1e-4)
s3 = seidel(A, b, x, 10000, 1e-6)

print(f"Seidel with 10^-2: {s1[1]}, iters: {s1[0]}")
print(f"Seidel with 10^-4: {s2[1]}, iters: {s2[0]}")
print(f"Seidel with 10^-6: {s3[1]}, iters: {s3[0]}")
print()

print(f"scipy.linalg.solve: {solve(A, b)}")

print("2nd task")
for n in [10, 20, 40]:
    A = []
    for i in range(n):
        A.append([])
        for j in range(n):
            if (i != j): A[i].append(1)
            else: A[i].append(2 * n)

    B = []
    for i in range(n):
        B.append(  (2*n-1)*(i+1)+n*(n+1)/2+3*n-1  )

    x = zeros_like(B)

    #for line in A:
    #    print(line)
    #print(B)

    A = array(A)
    B = array(B)

    j = jacobi(A, B, x, 10000, 1e-4)
    s = seidel(A, B, x, 10000, 1e-4)
    print(f"Jacobi with 10^-4: {j[1]}, iters: {j[0]}")
    print(f"Seidel with 10^-4: {s[1]}, iters: {s[0]}")

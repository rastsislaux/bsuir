package main

import (
	"fmt"
	"os"
)

func set_print(s []int) {
	fmt.Print("{")
	for i := 0; i < len(s)-1; i++ {
		fmt.Printf("%d, ", s[i])
	}
	fmt.Printf("%d}\n", s[len(s)-1])
}

func check(s []int, e int) bool {
	for i := 0; i < len(s); i++ { // Возьмём первый элемент a множества А
		if s[i] == e { // Если a == i
			return true // Добавляем
		}
		// Возьмём следующий элемент a множества A
	}
	return false
}

func union(arr ...[]int) []int {
	var ne []int // Создаем новое пустое множество А
	for _, n := range arr {  // Возьмём первое введённое множество I
		// Если пустое - переходим к следующему
		for i := 0; i < len(n); i++ { // Возьмём первый элемент i множества I
			if !check(ne, n[i]) {
				// Если множество А - пустое, добавляем
				ne = append(ne, n[i]) // Если i1 = i и введённое множество последнее, добавим элемент i в множество A
			}
			// Возьмём следующий элемент i множества I
		}
		// Возьмём следующее введённое множество
	}
	return ne // Выводим на экран
}

func intersection(a, b []int) []int {
	var ne []int // Создаем новое пустое множество А
	// Возьмём первое введённое множество I1
	for i := 0; i < len(a); i++ {
		// Возьмём первый элемент i1 множества I1
		// Если пустое, выведем пустое множество А

		// Возьмём второе введённое множество I
		for j := 0; j < len(b); j++ { // Возьмём первый элемент i множества I
			if a[i] == b[j] && !check(ne, a[i]) {
				ne = append(ne, a[i]) // Если i1 равен i и введённое множество I последнее
			}
			// Возьмём следующий элемент i множества I
		}
		// Возьмём следующий элемент i1 множества I1
	}
	return ne
}

func main() {
	var c1, c2 int
	fmt.Print("Введите мощность первого множества: ")
	fmt.Fscan(os.Stdin, &c1) // Пользователь задает мощность первого множества
	fmt.Print("Введите мощность второго множества: ")
	fmt.Fscan(os.Stdin, &c2) // Пользователь задает мощность второго множества

	var first, second []int

	fmt.Print("Задайте первое множество: ")
	// Пользователь задает первое множество
	for i := 0; i < c1; i++ {
		var e int
		fmt.Fscan(os.Stdin, &e)
		first = append(first, e)
	}
	// Пользователь задает второе множество
	fmt.Print("Задайте первое множество: ")
	for i := 0; i < c2; i++ {
		var e int
		fmt.Fscan(os.Stdin, &e)
		second = append(second, e)
	}

	fmt.Print("Какую операцию вы хотите провести?\n\t0 - объединение\n\t1 - Пересечение\nВаш выбор: ")
	var mode int
	fmt.Fscan(os.Stdin, &mode) // Пользователь выбирает операцию

	fmt.Print("Результат: ")
	switch mode {
	case 0:
		set_print(union(first, second)) // Выполняем операцию объединения
	case 1:
		set_print(intersection(first, second)) // Выполняем операцию пересечения
	default:
		fmt.Println("Вы выбрали несуществующую операцию.")
	}
}

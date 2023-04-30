% Два береги реки. На одном берегу есть 3 миссионера и 3 людоеда, требуется с помощью лодки
% вмещающей не более 2 человек, переправить всех на другой берег. Число присутствующих миссионеров
% на берегу и в лодке должно быть всегда не меньше числа людоедов.

% Проверка состояния
legal(CL, ML, CR, MR) :-
    ML >= 0, CL >= 0, MR >= 0, CR >= 0, % Количество людей не может быть отрицательным
    (ML >= CL; ML = 0), % На левом берегу должно быть больше миссионеров, чем людоедов, либо не должно быть миссионеров вовсе
    (MR >= CR; MR = 0). % То же, но для правого

% Возможные перемещения:
move([CL,ML,lft,CR,MR],[CL,ML2,rgt,CR,MR2]) :-
	% Два миссионера переплывают реку слева направо
	MR2 is MR+2,
	ML2 is ML-2,
	legal(CL,ML2,CR,MR2).

move([CL,ML,lft,CR,MR],[CL2,ML,rgt,CR2,MR]) :-
	% Два каннибала переплывают реку слева направо
	CR2 is CR+2,
	CL2 is CL-2,
	legal(CL2,ML,CR2,MR).

move([CL,ML,lft,CR,MR],[CL2,ML2,rgt,CR2,MR2]) :-
	%  Один миссионер и один каннибал переплывают реку слева направо
	CR2 is CR+1,
	CL2 is CL-1,
	MR2 is MR+1,
	ML2 is ML-1,
	legal(CL2,ML2,CR2,MR2).

move([CL,ML,lft,CR,MR],[CL,ML2,rgt,CR,MR2]) :-
	% Один миссионер переплывает реку слева направо
	MR2 is MR+1,
	ML2 is ML-1,
	legal(CL,ML2,CR,MR2).

move([CL,ML,lft,CR,MR],[CL2,ML,rgt,CR2,MR]) :-
	% Один каннибал перерлывают реку слева направо
	CR2 is CR+1,
	CL2 is CL-1,
	legal(CL2,ML,CR2,MR).

move([CL,ML,rgt,CR,MR],[CL,ML2,lft,CR,MR2]) :-
	% Два миссионера переплывают реку справа налево
	MR2 is MR-2,
	ML2 is ML+2,
	legal(CL,ML2,CR,MR2).

move([CL,ML,rgt,CR,MR],[CL2,ML,lft,CR2,MR]) :-
	% Два каннибала переплывают реку справа налево
	CR2 is CR-2,
	CL2 is CL+2,
	legal(CL2,ML,CR2,MR).

move([CL,ML,rgt,CR,MR],[CL2,ML2,lft,CR2,MR2]) :-
	%  Один миссионер и один каннибал переплывают реку справа налево
	CR2 is CR-1,
	CL2 is CL+1,
	MR2 is MR-1,
	ML2 is ML+1,
	legal(CL2,ML2,CR2,MR2).

move([CL,ML,rgt,CR,MR],[CL,ML2,lft,CR,MR2]) :-
	% Один миссионер переплывает реку справа налево
	MR2 is MR-1,
	ML2 is ML+1,
	legal(CL,ML2,CR,MR2).

move([CL,ML,rgt,CR,MR],[CL2,ML,lft,CR2,MR]) :-
	% Один каннибал переплывает реку справа налево
	CR2 is CR-1,
	CL2 is CL+1,
	legal(CL2,ML,CR2,MR).

% Перемещение из состяния 1 в 2 существует, если существует такой ход из 1 в 3, что 3 ещё не был достигнут
% и существует переход из состояния 3 в состояние 2 (рекурсия)
path([CL1,ML1,B1,CR1,MR1], [CL2,ML2,B2,CR2,MR2], Explored, MovesList) :-
    move([CL1, ML1, B1, CR1, MR1], [CL3, ML3, B3, CR3, MR3]),
    not(member([CL3, ML3, B3, CR3, MR3], Explored)),
    path([CL3,ML3,B3,CR3,MR3],
         [CL2,ML2,B2,CR2,MR2],
         [[CL3,ML3,B3,CR3,MR3] | Explored],
         [ [[CL3,ML3,B3,CR3,MR3], [CL1,ML1,B1,CR1,MR1]] | MovesList ]).

path([CL, ML, B, CR, MR], [CL, ML, B, CR, MR], _, MovesList) :- output(MovesList).

output([]) :- nl.
output([[A, B] | MovesList]) :-
    output(MovesList),
    write(B), write(' -> '), write(A), nl.

find(X, Y) :- path(X, Y, [X], _). 
find       :- find([3, 3, lft, 0, 0], [0, 0, rgt, 3, 3]).

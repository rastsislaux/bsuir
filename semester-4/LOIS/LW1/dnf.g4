grammar dnf;

// Parser rules
dnf: normal_disjunct EOF;
normal_disjunct: ( conjuct | (OPB conjuct OR normal_disjunct CLB) | (OPB normal_disjunct OR conjuct CLB) | (OPB conjuct OR conjuct CLB) );
conjuct: LITERAL | (OPB LITERAL AND LITERAL CLB) | (OPB conjuct AND LITERAL CLB) | (OPB LITERAL AND conjuct CLB);

// Lexer rules
LITERAL: VAR | (OPB NOT VAR CLB);
VAR: [A-Z];
OR: '\\/';
AND: '/\\';
NOT: '!';
OPB: '(';
CLB: ')';

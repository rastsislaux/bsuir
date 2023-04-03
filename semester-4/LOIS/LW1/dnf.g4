grammar dnf;

// Parser rules
dnf: normal_disjunct EOF;
normal_disjunct: ( conjunct | (OPB conjunct OR normal_disjunct CLB) | (OPB normal_disjunct OR conjunct CLB) | (OPB conjunct OR conjunct CLB) );
conjunct: LITERAL | (OPB LITERAL AND LITERAL CLB) | (OPB conjunct AND LITERAL CLB) | (OPB LITERAL AND conjunct CLB);

// Lexer rules
LITERAL: VAR | (OPB NOT VAR CLB);
VAR: [a-zA-Z][0-9]*;
OR: '\\/';
AND: '/\\';
NOT: '!';
OPB: '(';
CLB: ')';
WS: [ \t\r\n]+ -> skip ;

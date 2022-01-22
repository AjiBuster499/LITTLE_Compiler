grammar little;

identifier: IDENT;
intliteral: INT;
floatliteral: FLOAT;
stringliteral: STRING;

IDENT:[a-zA-Z];
INT: [0-9]+;
FLOAT: INT'.'INT;
STRING:'"' [0-9a-zA-Z]* '"';
COMMENT: '--' -> skip;
WS: [ \t\r\n]+ -> skip;
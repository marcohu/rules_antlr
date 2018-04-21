lexer grammar GoauLexer;
options { language=Go; }

Hello : 'hello';
ID : [a-z]+ ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

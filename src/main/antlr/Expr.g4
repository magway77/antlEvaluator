/** Grammars always start with a grammar header. */
grammar Expr;

/*
	Expression lexems https://github.com/antlr/antlr4/blob/master/doc/getting-started.md#installation
*/
WS			    : [ \t\r]+ -> skip;
NEWLINE			: '\r'?'\n';
INT			    : [0-9]+;
DOUBLE			: [0-9]+'.'[0-9]+;
TRUE			: 'true';
FALSE			: 'false';
ID			    : [a-zA-Z_][a-zA-Z_0-9]*;
STRING			: '"' ~( '\r' | '\n' | '"' )* '"';
WILDCARD		: '?';

/*
    assignment
*/
ASSIGN		: '=';
LPAREN      : '(' ;
RPAREN      : ')' ;

/*
	arithmetic operations
*/
PLUS		: '+';
MINUS		: '-';
MULT		: '*';
DIV		    : '/';
POW		    : '**';

/*
	boolean operations
*/
GR			: '>';
GRE			: '>=';
LS			: '<';
LSE			: '<=';
EQUAL		: '==';
NEQUAL		: '!=';
AND			: '&&';
OR			: '||';
NOT			: '!';

primaryExpression : // variable or constant definition, such as
    INT                 #IntValue
    | DOUBLE            #DoubleValue
    | STRING            #StringValue
    | (TRUE | FALSE)    #BoolValue
    ;

assignmentExpression :
    ID ASSIGN expression;

expression :
    LPAREN expression RPAREN                                    #Parens
    | left=expression operator=(MULT | DIV) right=expression    #MulDiv
    | left=expression operator=(PLUS | MINUS) right=expression  #AddSub
    | primaryExpression                                         #ValueExpr
    | assignmentExpression                                      #Assignment
    ;

statement : expression (NEWLINE | EOF);

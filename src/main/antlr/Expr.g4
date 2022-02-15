/** Grammars always start with a grammar header. */
grammar Expr;

statement : expression (NEWLINE | EOF);


expression :
    LPAREN expression RPAREN                                    #Parens
    | left=expression MULT right=expression                     #Multiply
    | left=expression DIV right=expression                      #Division
    | left=expression PLUS right=expression  #Addition
    | left=expression MINUS right=expression  #Substruction
    | primaryExpression                                         #ValueExpr
    | assignmentExpression                                      #Assignment
    | variable=expression'.count()'                                      #ListCount
    | variable=expression'.filterByType('typeName=ID')'                  #ListFilterByType
    ;

primaryExpression : // variable or constant definition, such as
    INT                 #IntValue
    | DOUBLE            #DoubleValue
    | STRING            #StringValue
    | (TRUE | FALSE)    #BoolValue
    ;


assignmentExpression :
    ID ASSIGN expression;

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

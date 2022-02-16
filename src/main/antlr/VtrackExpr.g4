grammar VtrackExpr;

/*minuted, hours,days*/
HOUR                : 'h';
MINUTE              : 'm';
DAY                 : 'd';

/*compaund tokens*/
INT			        : [0-9]+;

/*words, symbols*/
WS			        : [ \t\r]+ -> skip;
NEWLINE			    : '\r'?'\n';
//DIGIT               : [0-9];
WORD                :[a-zA-Z]+;
WILDCARD		    : '?';
DOT                 : '.';
COLON               : ':';
L_PARENTHESIS       : '(';
R_PARENTHESIS       : ')';
L_SQUARE_BRACKET    : '[';
R_SQUARE_BRACKET    : ']';
COMMA               : ',';
PER                 : '/';
UDERSCORE           : '_';


/*rules*/

identificator:
    (WORD | UDERSCORE | INT | timeKind)+
;

timeKind:
   (minute=MINUTE | hour=HOUR | day=DAY);

timePeriod:
    PER periodValue=INT periodKind=(MINUTE | HOUR | DAY);

valueRange:
    leftBorderKind=(L_PARENTHESIS | L_SQUARE_BRACKET) lowValue=INT COMMA? highValue=INT? rightBorderKind=(R_PARENTHESIS | R_SQUARE_BRACKET);

expression:
    identificator COLON identificator COLON identificator valueRange COLON valueRange timePeriod
;

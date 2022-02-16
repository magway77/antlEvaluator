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
MASK_ANY            : '*';


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

qualifierRangeItem:
    (MASK_ANY | INT) COMMA?;

qualifierRangeItems:
    qualifierRangeItem+;

qualifierRange:
    L_SQUARE_BRACKET qualifierRangeValue=qualifierRangeItems R_SQUARE_BRACKET;

expression:
    alertName=identificator COLON
    sourceId=identificator COLON
    qualifierName=identificator
    qualifierValue=qualifierRange COLON
    thresholdRange=valueRange
    timeRange=timePeriod
;

statement:
    expression (NEWLINE | EOF)
;

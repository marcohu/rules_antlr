grammar Calc;

options {
    language = Cpp;
}

@lexer::header {
    #include <iostream>
    #include <map>
    #include <string>

    using namespace std;
}

@lexer::traits {
  class CalcLexer;
  class CalcParser;
  typedef antlr3::Traits<CalcLexer, CalcParser> CalcLexerTraits;

}

@parser::header {
  #include "./CalcLexer.hpp"
}

@parser::traits {
  typedef CalcLexerTraits CalcParserTraits;
}

@parser::context {
  typedef std::map<std::string, int> StringMap;
  StringMap var_map;
}

prog:   (stat)+ ;

stat:   expr NEWLINE        { cout << $expr.value << endl; }
    |   ID '=' expr NEWLINE { string id = $ID.text;
                              var_map[id] = $expr.value;
                            }
    |   NEWLINE
    ;

expr returns [int value]
    :   a=multiExpr {$value = $a.value;}
        ( '+' b=multiExpr {$value += $b.value;}
        | '-' c=multiExpr {$value -= $c.value;}
        )*
    ;

multiExpr returns [int value]
    :   a=atom {$value = $a.value;}
        ('*' b=atom {$value *= $b.value;}
        )*
    ;

atom returns [int value]
    :   INT { $value = atoi($INT.text.c_str()); }
    |   ID  { int v = var_map[$ID.text];
              $value = v;
            }
    |   '(' expr ')' {$value = $expr.value;}
    ;

ID  :   ('a'..'z')+ ;
INT :   ('0'..'9')+ ;
NEWLINE: '\r'? '\n' ;
WS  :   (' '|'\t'|'\n'|'\r')+ {skip();} ;

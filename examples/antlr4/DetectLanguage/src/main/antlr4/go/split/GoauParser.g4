parser grammar GoauParser;
options { 
	tokenVocab	= GoauLexer;
	language	= Go;
}

r  : 'hello' ID ;         // match keyword hello followed by an identifier


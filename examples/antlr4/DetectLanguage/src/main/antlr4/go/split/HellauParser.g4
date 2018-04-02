parser grammar HellauParser;
options { 
	tokenVocab	= HellauLexer;
	language	= Go;
}

r  : 'hello' ID ;         // match keyword hello followed by an identifier


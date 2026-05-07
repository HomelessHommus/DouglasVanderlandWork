#include <stdio.h>

// grouping together a list of possible token tpyes called "TokenType"
typedef enum {
    TokenKeyword,
    TokenIdentifier,
    TokenLiteral,
    TokenDelimiter,
    TokenEnd,
    TokenUnknown
} TokenType;

char *TokenName(TokenType tok) {
    switch (tok) {
        case TokenKeyword:
            return "Keyword";
        case TokenIdentifier:
            return "Indentifier";
        case TokenLiteral:
            return "Literal";
        case TokenDelimiter:
            return "Delimiter";
        case TokenEnd:
            return "End of File";
        case TokenUnknown:
            return "Unknown";
        default:
            return "Unknown";
    }
}

// grouping together a struct of variables called a "Token" to be used later
typedef struct {
    TokenType tType;
    char lex[100];
    int row;
    int column;
} Token;

// grouping together a struct of variables called "LexorPos" to be used later
typedef struct {
    char *source;
    int row;
    int column;
} LexorPos;

// 2 lists that can be looped over to make it easier to find matches
char *keywords[] = {"int", "char", "if", "else", "while", "for", "do", "return"};
char delimiters[] = ";,(){}[]";


int main(void) {
    printf("Hello, World!\n");
    return 0;
}

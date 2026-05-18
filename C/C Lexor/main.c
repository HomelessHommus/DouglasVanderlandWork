#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// grouping together a list of possible token tpyes called "TokenType"
typedef enum {
    TokenKeyword,
    TokenIdentifier,
    TokenLiteral,
    TokenDelimiter,
    TokenEnd,
    TokenUnknown
} TokenType;

// switch statement to return the type of token to then print it
char *TokenName(TokenType tok) {
    switch (tok) {
        case TokenKeyword:
            return "Keyword";
        case TokenIdentifier:
            return "Identifier";
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

// grouping together a struct of variables called a "Token" to be used to define a token to be returned
// includes the TokenType typedef as a token ID with the tokens position
typedef struct {
    TokenType tType;
    char lex[100];
} Token;

// grouping together a struct of variables called "LexorPos" to be used by nextPos function
// to keep track of the lexor position variables
typedef struct {
    char *source;
} LexorPos;

// 2 lists that can be looped over to make it easier to find matches
char *keywords[] = {"int", "char", "if", "else", "while", "for", "do", "return"};
char delimiters[] = ";,(){}[]";

// Moves the head of the turing machine to the next character in the string
static char nextPos(LexorPos *lxpos) {
    char c = *lxpos->source++;
    return c;
}

// A function that scans the text and processes the token
Token NextToken(LexorPos *lxpos) {

    Token token;

    // scans for whitespace then calls the nextPos function until it reads something
    while (*lxpos->source && isspace((unsigned char) *lxpos->source)) {
        nextPos(lxpos);
    }


    // if the turing machine head is at the end of the file return a TokenEnd token
    if (*lxpos->source == '\0') {
        token.lex[0] = '\0';
        token.tType = TokenEnd;
        return token;
    }

    // if the turing machine head is at a digit, calls nextPos to get whole digit string,
    // then returns whole thing as TokenLiteral token
    if (isdigit((unsigned char)*lxpos->source)) {
        int i = 0;

        // while loops until next position is not a digit
        while (isdigit((unsigned char)*lxpos->source)) {
            token.lex[i++] = nextPos(lxpos);
        }

        token.lex[i] = '\0';
        token.tType = TokenLiteral;
        return token;
    }

    // if the turing machine head detects an identifier string, sets a TokenIdentifier token,
    // then checks if these identifiers are keywords or not
    if (isalpha((unsigned char)*lxpos->source) || *lxpos->source == '_') {

        int i = 0;

        // while loops until next character is not an alphanumeric or and underscore
        while (isalnum((unsigned char)*lxpos->source) || *lxpos->source == '_') {
            token.lex[i++] = nextPos(lxpos);
        }

        token.lex[i] = '\0';
        token.tType = TokenIdentifier;

        // checks the keywords array to see if the TokenIdentifier matches any keywords,
        // if so, sets this to TokenKeyword instead of TokenIdentifier
        for (int a = 0; a < 8; a++) {
            if (strcmp(token.lex, keywords[a]) == 0) {
                token.tType = TokenKeyword;
                break;
            }
        }
        return token;
    }

    // if the turing machine head is a character from the delimiter rules, returns a TokenDelimiter
    if (strchr(delimiters, *lxpos->source)) {

        token.lex[0] = nextPos(lxpos);
        token.lex[1] = '\0';
        token.tType = TokenDelimiter;
        return token;
    }

    // if no tokens have been returned, automatically returns a TokenUnknown
    token.lex[0] = nextPos(lxpos);
    token.lex[1] = '\0';
    token.tType = TokenUnknown;
    return token;
}

    int main(void) {

    // reads a file (HAS TO BE FROM ABSOLUTE PATH), sees how big it is,
    // creates space for it, then reads it into finalSource
    FILE *testcode = fopen("M:/Github Repos/DouglasVanderlandWork/C/C Lexor/Code test 2.c", "rb");
    if (!testcode) {
        printf("Error opening test 1.txt\n");
    }
    fseek(testcode, 0, SEEK_END);
    int size = ftell(testcode);
    rewind(testcode);
    char *finalSource = malloc(size + 1);
    fread(finalSource, 1, size, testcode);
    finalSource[size] = '\0';
    fclose(testcode);

    // Sets the turing head position at the start of the file ready to read
    LexorPos lxpos = {
        .source = finalSource
    };

    Token token;

    // a do-while loop that constantly calls NextToken on a Token variable, then prints it
    // loop stops when a TokenEnd is returned signalling the end of the file
    do {
        token = NextToken(&lxpos);
        printf("%-10s %-10s \n",
            token.lex, TokenName(token.tType));

    } while (token.tType != TokenEnd);

    return 0;
}
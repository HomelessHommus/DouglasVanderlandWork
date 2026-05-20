#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// MUST INCLUDE
// set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Os -s")
// in the CMakeLists.txt to get the executable to under 50KB

// grouping together a list of possible token tpyes called "TokenType"
typedef enum {
    TokenKeyword,
    TokenIdentifier,
    TokenLiteral,
    TokenDelimiter,
    TokenEnd,
    TokenUnknown,
    TokenTooLong
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
        case TokenTooLong:
            return "Sequence Too Long";
        default:
            return "Unknown";
    }
}

// grouping together a struct of variables called a "Token" to be used to define a token to be returned
// includes the TokenType typedef as a token ID with the tokens position
typedef struct {
    TokenType tType;
    char lex[100];
    int row;
    int column;
} Token;

// grouping together a struct of variables called "LexorPos" to be used by nextPos function
// to keep track of the lexor position variables
typedef struct {
    char *source;
    int row;
    int column;
} LexorPos;

// 2 lists that can be looped over to make it easier to find matches
char *keywords[] = {"int", "char", "if", "else", "while", "for", "do", "return"};
char delimiters[] = ";,(){}[]";

// Moves the head of the turing machine to the next character in the string
static char nextPos(LexorPos *lxpos) {
    char c = *lxpos->source++;
    if (c == '\n') {
        lxpos->row++;
        lxpos->column = 1;
    }
    else {
        lxpos->column++;
    }
    return c;
}

// A function that scans the character/s and processes the token
Token NextToken(LexorPos *lxpos) {

    Token token;

    // scans for whitespace then calls the nextPos function until it reads something
    while (*lxpos->source && isspace((unsigned char) *lxpos->source)) {
        nextPos(lxpos);
    }

    //assigns the row and column values to the token to be printed later
    token.row = lxpos->row;
    token.column = lxpos->column;


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

        // while loops until next position is not a digit, or index == 99
        // if index is 99, then the input is too long. Skips over rest if the input
        while (isdigit((unsigned char)*lxpos->source)) {
            token.lex[i++] = nextPos(lxpos);
            if (i == 99) {
                token.lex[i] = '\0';
                token.tType = TokenTooLong;
                while (*lxpos->source && isdigit((unsigned char) *lxpos->source)) {
                    nextPos(lxpos);
                }
                return token;
            }
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
        // or index == 9. If index is 99, then the input is too long. Skips over rest if the input
        while (isalnum((unsigned char)*lxpos->source) || *lxpos->source == '_') {
            token.lex[i++] = nextPos(lxpos);
            if (i == 99) {
                token.lex[i] = '\0';
                token.tType = TokenTooLong;
                while (isalnum((unsigned char)*lxpos->source) || *lxpos->source == '_') {
                    nextPos(lxpos);
                }
                return token;
            }
        }

        token.lex[i] = '\0';
        token.tType = TokenIdentifier;

        // checks the keywords array to see if the TokenIdentifier matches any keywords,
        // if so, sets this to TokenKeyword instead of TokenIdentifier
        // keyword array is length 8, hence for loop up to 8
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

    int main(int argc, char *argv[]) {

    // reads a file provided in the arguments and sees how big it is,
    // creates space for it, then reads it into finalSource
    FILE *testcode = fopen(argv[1], "rb");
    if (!testcode) {
        printf("Error opening file\n");
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
        .source = finalSource,
        .row = 1,
        .column = 1
    };

    Token token;

    // a do-while loop that constantly calls NextToken on a Token variable, then prints it
    // loop stops when a TokenEnd is returned signalling the end of the file
    do {
        token = NextToken(&lxpos);
        printf("%-20s %-20s row %-20d column %d\n",
            token.lex,
            TokenName(token.tType),
            token.row,
            token.column);

    } while (token.tType != TokenEnd);

    return 0;
}
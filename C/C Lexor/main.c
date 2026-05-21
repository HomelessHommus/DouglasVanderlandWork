#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Douglas Vanderland
// vand0475

// MUST INCLUDE
// set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Os -s")
// in the CMakeLists.txt to get the executable to under 50KB

// FOR THE PROGRAM TO WORK, MUST HAVE 2 ARGUMENTS
// 1ST FOR INPUT FILE, 2ND FOR OUTPUT FILE. WILL NOT WORK WITHOUT THESE
// EXAMPLE: "C:\Users\Doug\Desktop\input.txt" "C:\Users\Doug\Desktop\output.txt"
// FOUND IN Configuration -> Edit... -> Program Arguments
// :)

// grouping together a list of possible token types called "TokenType"
typedef enum {
    TokenKeyword,
    TokenIdentifier,
    TokenLiteral,
    TokenDelimiter,
    TokenEnd,
    TokenUnknown,
    TokenTooLong
} TokenType;

// a switch statement to return the type of token to then print it
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

// Moves to the next position in the character sequence
// Mainly used to read a whole word or digit >9 to be analysed for a token type
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

// A function that calls the next token to be read and where the if statements are located
Token NextToken(LexorPos *lxpos) {

    Token token;

    // scans for whitespace then calls the nextPos function until it reads something
    while (*lxpos->source && isspace((unsigned char) *lxpos->source)) {
        nextPos(lxpos);
    }

    //assigns the row and column values to the token to be printed later
    token.row = lxpos->row;
    token.column = lxpos->column;

    // If the current character is null, a TokenEnd is returned to signify the end of the file
    if (*lxpos->source == '\0') {

        token.lex[0] = '\0';
        token.tType = TokenEnd;
        return token;
    }

    // if the character being read is a digit, calls nextPos to get whole digit string,
    // then returns whole thing as TokenLiteral token
    if (isdigit((unsigned char)*lxpos->source)) {

        int i = 0;

        // while loops until next position is not a digit, or index == 99
        // if index is 99, then the input is too long. Skips over rest of the input
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

    // if the character being read is an alphabet or underscore character, calls the while loop to read the rest of the
    // word then sets the sequence to a TokenIdentifier. Then the for loop checks if these identifiers are keywords
    // or not from the previously defined character array
    if (isalpha((unsigned char)*lxpos->source) || *lxpos->source == '_') {

        int i = 0;

        // while loops until next character is not an alphanumeric or an underscore
        // or index == 99. If index is 99, then the input is too long. Skips over rest
        // of the input and returns a TokenTooLong
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

    // if the character being read is a delimiter, returns a TokenDelimiter
    // this is done by comparing is to the previously defined character array containing
    // the delimiters in the language
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

    // the main method that calls all the methods above
    int main(int argc, char *argv[]) {

    // read 2 files. 1 for input and 1 for output
    FILE *input = fopen(argv[1], "rb");
    if (!input) {
        printf("Error opening input file\n");
        return 1;
    }
    FILE *output = fopen(argv[2], "w");
    if(!output) {
        printf("Error opening output file\n");
        return 1;
    }

    // sees how big the input file is and allocates enough memory for it
    fseek(input, 0, SEEK_END);
    int size = ftell(input);
    rewind(input);
    char *finalSource = malloc(size + 1);
    fread(finalSource, 1, size, input);
    // finalSource[size] used to find the end of file
    finalSource[size] = '\0';
    fclose(input);

    // Goes to the beginning of the file ready to read
    LexorPos lxpos = {
        .source = finalSource,
        .row = 1,
        .column = 1
    };

    Token token;

    // a do-while loop that constantly calls NextToken, gets the token type, then prints it
    // printed to both the console and to the output file
    // loop stops when a TokenEnd is returned signalling the end of the file
    // do-while is used to ensure at least one line is printed
    // (in case of empty file the TokenEnd is printed)
    do {
        token = NextToken(&lxpos);
        printf("%-20s %-20s row %-20d column %d\n",
            token.lex,
            TokenName(token.tType),
            token.row,
            token.column);
        fprintf(output, "%-20s %-20s row %-20d column %d\n",
            token.lex,
            TokenName(token.tType),
            token.row,
            token.column);

    } while (token.tType != TokenEnd);

    fclose(output);

    return 0;
}
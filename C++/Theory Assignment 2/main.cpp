#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <set>
#include <stack>
#include <variant>
#include <algorithm>
#include <memory>

// Douglas Vanderland
// vand0475

// VERY IMPORTANT
// FOR THIS TO WORK YOU HAVE TO INCLUDE THE PATH TO THE OUTPUT FILE OF THE LEXOR IN THE PARAMETERS
// 2 PARAMETERS ARE NOT NEEDED THIS TIME LIKE THE LEXOR BUT IT DOES PRINT TO A FILE IF A PATH IS GIVEN AS SECOND PARAMETER
// EXAMPLE: "C:\Users\Doug\Desktop\LexorOutput.txt" "C:\Users\Doug\Desktop\ParserOutput.txt"
// FOUND IN Configuration -> Edit... -> Program Arguments
// :)



// an enum class of tokens. The delimiters and keywords have been split up so the SLR parser can tell the
// difference between them
enum class Tokens {

    INT,
    CHAR,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    RETURN,
    CLASS,
    PUBLIC,
    PRIVATE,
    PROTECTED,
    NEW,
    DELETE,
    IDENTIFIER,
    LITERAL,
    SEMICOLON,
    COMMA,
    LEFTSQUAREBRACKET,
    RIGHTSQUAREBRACKET,
    LEFTBRACKET,
    RIGHTBRACKET,
    LEFTCURLYBRACKET,
    RIGHTCURLYBRACKET,
    COLON,
    ENDOFFILE,
    UNKNOWN,
    TOKEN
};

// enum class of the non-terminals defined in the CFG
enum class NonTerminals {
    Program,
    ClassDeclarationList,
    ClassDeclaration,
    AccessSpecifierSections,
    AccessSpecifierSection,
    AccessSpecifier,
    MemberList,
    MemberDeclaration,
    VariableDeclaration,
    VariableList,
    FunctionDeclaration,
    ParameterList,
    Parameter,
    VariableType,
    CodeBlock,
    StatementList,
    Statement,
    MatchedStatement,
    UnmatchedStatement,
    NonIfStatement,
    Declaration,
    DeclarationEnd,
    ExpressionStatement,
    WhileStatement,
    DoWhileStatement,
    ForStatement,
    InsideForStatement,
    ReturnStatement,
    Expression,
    PostfixExpression,
    PrimaryExpression,
    ArgumentList
};

// A struct of name Token to hold info about it
// same as the lexor token
struct Token {
    Tokens type;
    std::string lex;
    int row;
    int column;
};

// a method to read in the output from the lexor
std::vector<Token> readLexerFile(const std::string& lexorOutput) {
    std::ifstream lexorStream(lexorOutput);

    // map of the defined keywords
    std::map<std::string,Tokens> keywords = {
        {"int",Tokens::INT},
        {"char",Tokens::CHAR},
        {"if",Tokens::IF},
        {"else",Tokens::ELSE},
        {"while",Tokens::WHILE},
        {"for",Tokens::FOR},
        {"do",Tokens::DO},
        {"return",Tokens::RETURN},
        {"class",Tokens::CLASS},
        {"public",Tokens::PUBLIC},
        {"private",Tokens::PRIVATE},
        {"protected",Tokens::PROTECTED},
        {"new",Tokens::NEW},
        {"delete",Tokens::DELETE}
    };

    // map of the defined delimiters
    std::map<std::string,Tokens> delimiters = {
        {";",Tokens::SEMICOLON},
        {",",Tokens::COMMA},
        {"(",Tokens::LEFTBRACKET},
        {")",Tokens::RIGHTBRACKET},
        {"[",Tokens::LEFTSQUAREBRACKET},
        {"]",Tokens::RIGHTSQUAREBRACKET},
        {"{",Tokens::LEFTCURLYBRACKET},
        {"}",Tokens::RIGHTCURLYBRACKET},
        {":",Tokens::COLON}
    };

    // used to trim the lex output and split it into sections to be used (lex, token type, row, column)
    auto trim = [](std::string trimString) {
        auto trimmedSize = trimString.find_last_not_of(' ');
        return trimmedSize == std::string::npos ? "" : trimString.substr(0, trimmedSize+1);
    };
 
    std::vector<Token> tokens;
    std::string line;

    while (std::getline(lexorStream, line)) {

        // this is done because the lexor output has 20 spaces between each section as per the lexor design
        if (line.size() < 42) line.resize(42, ' ');
        std::string lex = trim(line.substr(0, 20));
        std::string typeString = trim(line.substr(21, 20));
        std::string rest = line.substr(41);

        // this is how the row and column info is parsed. Used for error printing
        auto rowAuto  = rest.find("row ");
        auto columnAuto = rest.find("column ");
        int row = std::stoi(rest.substr(rowAuto + 4));
        int column = std::stoi(rest.substr(columnAuto + 7));

        // if / else-if statement to ID token type from input
        Tokens type;
        if (typeString == "End of File") {
            type = Tokens::ENDOFFILE;
        }
        else if (typeString == "Identifier") {
            type = Tokens::IDENTIFIER;
        }
        else if (typeString == "Literal") {
            type = Tokens::LITERAL;
        }
        else if (typeString == "Keyword") {
            auto findIt = keywords.find(lex);
            if (findIt != keywords.end()) {
                type=findIt->second;
            }
        }
        else if (typeString == "Delimiter") {
            auto it = delimiters.find(lex);
            if (it != delimiters.end()) {
                type=it -> second;
            }
        }
        else if (typeString == "Unknown") {
            type=Tokens::UNKNOWN;
            std::cout << "Unknown Token Parsed: lexeme = " + lex << std::endl;
            continue;
        }

        // has assigned a token at this point, so push it back
        tokens.push_back({type, lex, row, column});

        // checks if end of file to break the while loop
        if (type == Tokens::ENDOFFILE) {
            break;
        }
    }

    // checks tokens are empty OR token type is == to endoffile
    // pushs an endoffile token onto stack
    if (tokens.empty() || tokens.back().type != Tokens::ENDOFFILE) {
        tokens.push_back({Tokens::ENDOFFILE, "", 0, 0});
    }
    return tokens;
}



// used to label the new part of the parse tree.
// is the same thing as the class above just as a char array
std::string nonTerminalName(NonTerminals nonterms) {
    static const char* CFGList[] = {
        "Program",
        "ClassDeclarationList",
        "ClassDeclaration",
        "AccessSpecifierSections",
        "AccessSpecifierSection",
        "AccessSpecifier",
        "MemberList",
        "MemberDeclaration",
        "VariableDeclaration",
        "VariableList",
        "FunctionDeclaration",
        "ParameterList",
        "Parameter",
        "VariableType",
        "CodeBlock",
        "StatementList",
        "Statement",
        "MatchedStatement",
        "UnmatchedStatement",
        "NonIfStatement",
        "Declaration",
        "DeclarationEnd",
        "ExpressionStatement",
        "WhileStatement",
        "DoWhileStatement",
        "ForStatement",
        "InsideForLoop",
        "ReturnStatement",
        "Expression",
        "PostfixExpression",
        "PrimaryExpression",
        "ArgumentList"
    };
    return CFGList[static_cast<int>(nonterms)];
}

// uses a variant to hold both Tokens and non-terminals
// this makes it easier to write the grammar below
using tokOrNonT = std::variant<Tokens, NonTerminals>;
static auto Tok(Tokens tok) -> tokOrNonT {
    return tok;
}
static auto NonTe(NonTerminals nt) -> tokOrNonT {
    return nt;
}

// a struct for a grammarRule that defined the different parts.
// non-terminal on the left
// token or non-terminal on the right
// then the string name of it (defined in the CFG)
struct grammarRule {
    NonTerminals leftHS;
    std::vector<tokOrNonT> RightHS;
    std::string name;
};

// method to build the grammar using variant and struct above
std::vector<grammarRule> buildGrammar() {
    std::vector<grammarRule> grammarRules;

    // defined addToGrammar used heavily below
    // parses on the left side, right side, and name because the buildGrammar returns vector of the grammarRule struct
    // this is CFG from the report
    auto addToGrammar = [&](NonTerminals nonTerms, std::vector<tokOrNonT> symbol, std::string string) {
        grammarRules.push_back({
            nonTerms,
            std::move(symbol),
            std::move(string)});
    };

    // adds the 64 different sections of the CFG here
    // first parameter is the non-terminal enum class
    // second parameter is the token or non-terminal, if a non-terminal it's the same enum class, if a token, it's the
    // tokens enum class at the top
    // third parameter is the string. copy and pasted from the CFG
    using nonT = NonTerminals;

    addToGrammar(nonT::Program,{NonTe(nonT::ClassDeclarationList)},"Program->ClassDeclarationList");
    addToGrammar(nonT::ClassDeclarationList,{NonTe(nonT::ClassDeclaration)},"ClassDeclarationList->ClassDeclaration");
    addToGrammar(nonT::ClassDeclarationList,{NonTe(nonT::ClassDeclaration),NonTe(nonT::ClassDeclarationList)},"ClassDeclarationList->ClassDeclaration ClassDeclarationList");
    addToGrammar(nonT::ClassDeclaration,{Tok(Tokens::CLASS),Tok(Tokens::IDENTIFIER),Tok(Tokens::LEFTCURLYBRACKET),NonTe(nonT::AccessSpecifierSections),Tok(Tokens::RIGHTCURLYBRACKET),Tok(Tokens::SEMICOLON)},"ClassDeclaration->class identifier{ AccessSpecifierSections } ;");
    addToGrammar(nonT::AccessSpecifierSections,{NonTe(nonT::AccessSpecifierSection)},"AccessSpecifierSections->AccessSpecifierSection");
    addToGrammar(nonT::AccessSpecifierSections,{NonTe(nonT::AccessSpecifierSection),NonTe(nonT::AccessSpecifierSections)},"AccessSpecifierSections->AccessSpecifierSection AccessSpecifierSections");
    addToGrammar(nonT::AccessSpecifierSection,{NonTe(nonT::AccessSpecifier),Tok(Tokens::COLON),NonTe(nonT::MemberList)},"AccessSpecifierSection->AccessSpecifier : MemberList");
    addToGrammar(nonT::AccessSpecifier,{Tok(Tokens::PUBLIC)},"AccessSpecifier->public");
    addToGrammar(nonT::AccessSpecifier,{Tok(Tokens::PRIVATE)},"AccessSpecifier->private");
    addToGrammar(nonT::AccessSpecifier,{Tok(Tokens::PROTECTED)},"AccessSpecifier->protected");
    addToGrammar(nonT::MemberList,{NonTe(nonT::MemberDeclaration)},"MemberList->MemberDeclaration");
    addToGrammar(nonT::MemberList,{NonTe(nonT::MemberDeclaration),NonTe(nonT::MemberList)},"MemberList->MemberDeclaration MemberList");
    addToGrammar(nonT::MemberDeclaration,{NonTe(nonT::VariableDeclaration)},"MemberDeclaration->VariableDeclaration");
    addToGrammar(nonT::MemberDeclaration,{NonTe(nonT::FunctionDeclaration)},   "MemberDeclaration->FunctionDeclaration");
    addToGrammar(nonT::VariableDeclaration,{NonTe(nonT::VariableType),NonTe(nonT::VariableList),Tok(Tokens::SEMICOLON)},"VariableDeclaration->VariableType VariableList ;");
    addToGrammar(nonT::VariableList,{Tok(Tokens::IDENTIFIER)},"VariableList->identifier");
    addToGrammar(nonT::VariableList,{Tok(Tokens::IDENTIFIER),Tok(Tokens::COMMA),NonTe(nonT::VariableList)},"VariableList->identifier , VariableList");
    addToGrammar(nonT::FunctionDeclaration,{NonTe(nonT::VariableType),Tok(Tokens::IDENTIFIER),Tok(Tokens::LEFTBRACKET),NonTe(nonT::ParameterList),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::CodeBlock)},"FunctionDeclaration->VariableType identifier( ParameterList ) CodeBlock");
    addToGrammar(nonT::FunctionDeclaration,{NonTe(nonT::VariableType),Tok(Tokens::IDENTIFIER),Tok(Tokens::LEFTBRACKET),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::CodeBlock)},"FunctionDeclaration->VariableType identifier( ) CodeBlock");
    addToGrammar(nonT::ParameterList,{NonTe(nonT::Parameter)},"ParameterList->Parameter");
    addToGrammar(nonT::ParameterList,{NonTe(nonT::Parameter),Tok(Tokens::COMMA),NonTe(nonT::ParameterList)},"ParameterList->Parameter , ParameterList");
    addToGrammar(nonT::Parameter,{NonTe(nonT::VariableType),Tok(Tokens::IDENTIFIER)},"Parameter->VariableType identifier");
    addToGrammar(nonT::VariableType,{Tok(Tokens::INT)},"VariableType->int");
    addToGrammar(nonT::VariableType,{Tok(Tokens::CHAR)},"VariableType->char");
    addToGrammar(nonT::CodeBlock,{Tok(Tokens::LEFTCURLYBRACKET),NonTe(nonT::StatementList),Tok(Tokens::RIGHTCURLYBRACKET)},"CodeBlock->{ StatementList }");
    addToGrammar(nonT::CodeBlock,{Tok(Tokens::LEFTCURLYBRACKET),Tok(Tokens::RIGHTCURLYBRACKET)},"CodeBlock->{ }");
    addToGrammar(nonT::StatementList,{NonTe(nonT::Statement)},"StatementList->Statement");
    addToGrammar(nonT::StatementList,{NonTe(nonT::Statement),NonTe(nonT::StatementList)},"StatementList->Statement StatementList");
    addToGrammar(nonT::Statement,{NonTe(nonT::MatchedStatement)},"Statement->MatchedStatement");
    addToGrammar(nonT::Statement,{NonTe(nonT::UnmatchedStatement)},"Statement->UnmatchedStatement");
    addToGrammar(nonT::MatchedStatement,{NonTe(nonT::NonIfStatement)},"MatchedStatement->NonIfStatement");
    addToGrammar(nonT::MatchedStatement,{Tok(Tokens::IF),Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::MatchedStatement),Tok(Tokens::ELSE),NonTe(nonT::MatchedStatement)},"MatchedStatement->if( Expression ) MatchedStatement else MatchedStatement");
    addToGrammar(nonT::UnmatchedStatement,{Tok(Tokens::IF),Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::Statement)},"UnmatchedStatement->if( Expression ) Statement");
    addToGrammar(nonT::UnmatchedStatement,{Tok(Tokens::IF),Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::MatchedStatement),Tok(Tokens::ELSE),NonTe(nonT::UnmatchedStatement)},"UnmatchedStatement->if( Expression ) MatchedStatement else UnmatchedStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::Declaration)},"NonIfStatement->Declaration");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::ExpressionStatement)},"NonIfStatement->ExpressionStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::WhileStatement)},"NonIfStatement->WhileStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::DoWhileStatement)},"NonIfStatement->DoWhileStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::ForStatement)},"NonIfStatement->ForStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::ReturnStatement)},"NonIfStatement->ReturnStatement");
    addToGrammar(nonT::NonIfStatement,{NonTe(nonT::CodeBlock)},"NonIfStatement->CodeBlock");
    addToGrammar(nonT::Declaration,{NonTe(nonT::VariableType),Tok(Tokens::IDENTIFIER),NonTe(nonT::DeclarationEnd),Tok(Tokens::SEMICOLON)},"Declaration->VariableType identifier DeclarationEnd ;");
    addToGrammar(nonT::DeclarationEnd,{},"DeclarationEnd->ε");
    addToGrammar(nonT::DeclarationEnd,{Tok(Tokens::COMMA),Tok(Tokens::IDENTIFIER),NonTe(nonT::DeclarationEnd)},"DeclarationEnd->, identifier DeclarationEnd");
    addToGrammar(nonT::ExpressionStatement,{NonTe(nonT::Expression),Tok(Tokens::SEMICOLON)},"ExpressionStatement->Expression ;");
    addToGrammar(nonT::WhileStatement,{Tok(Tokens::WHILE),Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::CodeBlock)},"WhileStatement->while( Expression) CodeBlock");
    addToGrammar(nonT::DoWhileStatement,{Tok(Tokens::DO),NonTe(nonT::CodeBlock),Tok(Tokens::WHILE),Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),Tok(Tokens::SEMICOLON)},"DoWhileStatement->do CodeBlock while( Expression );");
    addToGrammar(nonT::ForStatement,{Tok(Tokens::FOR),Tok(Tokens::LEFTBRACKET),NonTe(nonT::InsideForStatement),Tok(Tokens::SEMICOLON),NonTe(nonT::Expression),Tok(Tokens::SEMICOLON),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET),NonTe(nonT::CodeBlock)},"ForStatement->for(InsideForStatement;Expression;Expression) CodeBlock");
    addToGrammar(nonT::InsideForStatement,{NonTe(nonT::Expression)},"InsideForStatement->Expression");
    addToGrammar(nonT::InsideForStatement,{NonTe(nonT::VariableType),Tok(Tokens::IDENTIFIER)},"InsideForStatement->VariableType identifier");
    addToGrammar(nonT::ReturnStatement,{Tok(Tokens::RETURN),NonTe(nonT::Expression),Tok(Tokens::SEMICOLON)},"ReturnStatement->return Expr ;");
    addToGrammar(nonT::Expression,{NonTe(nonT::PostfixExpression)},"Expression->PostfixExpression");
    addToGrammar(nonT::PostfixExpression,{NonTe(nonT::PrimaryExpression)},"PostfixExpression->PrimaryExpression");
    addToGrammar(nonT::PostfixExpression,{NonTe(nonT::PostfixExpression),Tok(Tokens::LEFTSQUAREBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTSQUAREBRACKET)},"PostfixExpression->PostfixExpression[ Expression ]");
    addToGrammar(nonT::PostfixExpression,{NonTe(nonT::PostfixExpression),Tok(Tokens::LEFTBRACKET),NonTe(nonT::ArgumentList),Tok(Tokens::RIGHTBRACKET)},"PostfixExpression->PostfixExpression( ArgumentList )");
    addToGrammar(nonT::PostfixExpression,{NonTe(nonT::PostfixExpression),Tok(Tokens::LEFTBRACKET),Tok(Tokens::RIGHTBRACKET)},"PostfixExpression->PostfixExpression()");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::IDENTIFIER)},"PrimaryExpression->identifier");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::LITERAL)},"PrimaryExpression->literal");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::LEFTBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTBRACKET)}, "PrimaryExpression->( Expression )");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::NEW),NonTe(nonT::VariableType),Tok(Tokens::LEFTBRACKET),Tok(Tokens::RIGHTBRACKET)}, "PrimaryExpression->new VariableType()");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::NEW),NonTe(nonT::VariableType),Tok(Tokens::LEFTSQUAREBRACKET),NonTe(nonT::Expression),Tok(Tokens::RIGHTSQUAREBRACKET)}, "PrimaryExpression->new VariableType[ Expression ]");
    addToGrammar(nonT::PrimaryExpression,{Tok(Tokens::DELETE),Tok(Tokens::IDENTIFIER)},"PrimaryExpression->delete identifier");
    addToGrammar(nonT::ArgumentList,{NonTe(nonT::Expression)},"ArgumentList->Expression");
    addToGrammar(nonT::ArgumentList,{NonTe(nonT::Expression),Tok(Tokens::COMMA),NonTe(nonT::ArgumentList)},"ArgumentList->Expression,ArgumentList");

    return grammarRules;
}

// a struct to represent the item inside a specific position of a CFG rule
// like the dot position as the parser reads into the rule
struct Item {
    int grammarRuleNumber;
    int dotPosition;
    bool operator < (const Item& item) const{
        return grammarRuleNumber != item.grammarRuleNumber ? grammarRuleNumber < item.grammarRuleNumber : dotPosition < item.dotPosition;
    }
    bool operator == (const Item& item2) const{
        return grammarRuleNumber == item2.grammarRuleNumber && dotPosition == item2.dotPosition;
    }
};

// used to print the token from the lexor in the parser tree
std::string tokenName(Tokens input) {
    switch (input) {
        case Tokens::INT:
            return "Keyword";
        case Tokens::CHAR:
            return "Keyword";
        case Tokens::IF:
            return "Keyword";
        case Tokens::ELSE:
            return "Keyword";
        case Tokens::WHILE:
            return "Keyword";
        case Tokens::FOR:
            return "Keyword";
        case Tokens::DO:
            return "Keyword";
        case Tokens::RETURN:
            return "Keyword";
        case Tokens::CLASS:
            return "Keyword";
        case Tokens::PUBLIC:
            return "Keyword";
        case Tokens::PRIVATE:
            return "Keyword";
        case Tokens::PROTECTED:
            return "Keyword";
        case Tokens::NEW:
            return "Keyword";
        case Tokens::DELETE:
            return "Keyword";
        case Tokens::IDENTIFIER:
            return "Identifier";
        case Tokens::LITERAL:
            return "Literal";
        case Tokens::SEMICOLON:
            return "Delimiter";
        case Tokens::COMMA:
            return "Delimiter";
        case Tokens::LEFTBRACKET:
            return "Delimiter";
        case Tokens::RIGHTBRACKET:
            return "Delimiter";
        case Tokens::LEFTSQUAREBRACKET:
            return "Delimiter";
        case Tokens::RIGHTSQUAREBRACKET:
            return "Delimiter";
        case Tokens::LEFTCURLYBRACKET:
            return "Delimiter";
        case Tokens::RIGHTCURLYBRACKET:
            return "Delimiter";
        case Tokens::COLON:
            return "Delimiter";
        case Tokens::ENDOFFILE:
            return "$";
        default:
            return "unknown token";
    }
}

// a class about the possible moves the parser can do
enum class ActionMoves {
    SHIFT,
    REDUCE,
    ACCEPT,
    ERROR
};

// a struct using the action class above
// holds an action move (set to error by default)
// hold an int, or where to go after performing an action (set to -1, or an invalid entry by default)
// for example: SHIFT 14 means parser must shift to state 14
struct Action {
    ActionMoves AM = ActionMoves::ERROR;
    int value = -1;
};

// a struct with info about the SLR parse table
// an int to hold the amount of state
// a vector of maps holding the action table. the map holds a token and action
// a vector of maps holding the go-to table. the map holds a non-terminal and an int
struct ParseTable {
    int stateNumber = 0;
    std::vector<std::map<Tokens, Action>> actionTable;
    std::vector<std::map<NonTerminals, int>> goToTable;
};

// a massive struct that builds the entire parsing table from the previously defined grammar
struct SLRBuilder {

    // all the data that is stored for the table.
    // including CFG rules, CFG size, the states, table etc.
    using ItemSet = std::set<Item>;
    std::vector<grammarRule> grammar;
    int grammarSize = 64;
    std::map<NonTerminals,std::set<Tokens>> first;
    std::map<NonTerminals,std::set<Tokens>> follow;
    std::map<NonTerminals, bool> epsilonDecider;
    std::vector<ItemSet> states;
    std::map<ItemSet, int> stateNumberMap;
    ParseTable table;

    // the variable assignment and method calls are in the constructor so the SLR table is built upon making the object
    SLRBuilder() {
        grammar = buildGrammar();
        grammarSize = static_cast<int>(grammar.size());
        grammar.push_back(grammarRule{NonTerminals::Program,{static_cast<tokOrNonT>(NonTerminals::Program)},"S' -> Program"});
        calculateFirstSet();
        calculateFollowSet();
        buildStates();
        buildTables();
    }

    // this method gets an ItemSet and expands it according to the CFG
    // this is a seperate method because it is used twice so avoiding duplicate code
    // this is the closure operation from the report
    ItemSet ItemSetExpander (ItemSet itemSet) const {
        bool whileBool = true;

        while (whileBool) {
            // set as false because if any if statements are true, this is set back to true and while loop continues
            whileBool = false;

            for (auto partOfItem : std::vector(itemSet.begin(),itemSet.end())) {
                auto& RHS = grammar[partOfItem.grammarRuleNumber].RightHS;

                if (partOfItem.dotPosition >= static_cast<int>(RHS.size())) {
                    continue;
                }
                auto* nonTAuto = std::get_if<NonTerminals>(&RHS[partOfItem.dotPosition]);

                if(!nonTAuto) {
                    continue;
                }

                for (int a = 0; a < static_cast<int>(grammar.size()); a++)
                    if (grammar[a].leftHS == *nonTAuto) {
                        if(itemSet.insert({a,0}).second) {
                            whileBool = true;
                        }
                    }
            }
        }

        return itemSet;
    }

    // method that takes an item and token or non-terminal and returns the next item according to CFG
    // also a seperate method because it's used more than once
    // this is the go-to operation from the report
    ItemSet goToNextState(const ItemSet& itemSet, const tokOrNonT& tokOrNonTerm) const {

        ItemSet iSet;

        for (auto& item:itemSet) {
            auto& RHS = grammar[item.grammarRuleNumber].RightHS;

            if (item.dotPosition < static_cast<int>(RHS.size()) && RHS[item.dotPosition] == tokOrNonTerm) {
                iSet.insert({item.grammarRuleNumber, item.dotPosition + 1});
            }
        }
        return ItemSetExpander(iSet);
    }

    // method that calculates the first tokens after every non-terminal is expanded according to CFG
    // this is the building block for the FIRST set in the FIRST FOLLOW sets for SLR parser
    // this is first set from the report
    void calculateFirstSet() {

        //initialising all the non-terminals
        for (int i = 0; i < grammarSize; i++) {
            first [static_cast<NonTerminals>(i)] = {};
            epsilonDecider [static_cast<NonTerminals>(i)] = false;
        }

        bool whileBool = true;

        while (whileBool) {
            whileBool = false;

            // iterates over all productions in grammar
            for (int a = 0; a < grammarSize; a++) {
                auto& grammarRuleAuto = grammar[a];
                auto& tokenSetAuto = first[grammarRuleAuto.leftHS];

                if (grammarRuleAuto.RightHS.empty()) {
                    if (!epsilonDecider[grammarRuleAuto.leftHS]) {
                        epsilonDecider[grammarRuleAuto.leftHS] = true;
                        whileBool = true;
                    } continue;
                }

                // goes through rhs symbol until it cant do the epsilon decider
                for (auto& tokOrNonTSymbol:grammarRuleAuto.RightHS) {
                    if (auto* t = std::get_if<Tokens>(&tokOrNonTSymbol)) {
                        if(tokenSetAuto.insert(*t).second){
                        whileBool = true;
                    }
                        break;

                    }

                    // adds current rhs non-terminal from tokenSetAuto to first set
                    for (auto t:first[std::get<NonTerminals>(tokOrNonTSymbol)]) {
                        if(tokenSetAuto.insert(t).second) {
                            whileBool = true;
                        }
                    }

                    if (!epsilonDecider[std::get<NonTerminals>(tokOrNonTSymbol)]) {
                        break;
                    }
                }

                bool allNull=true;

                // checks if all symbols on rhs can do the pass the epsilon decider bool
                for (auto& tONTSymbol:grammarRuleAuto.RightHS) {
                    auto* nonTermAuto = std::get_if<NonTerminals>(&tONTSymbol);

                    if (!nonTermAuto || !epsilonDecider[*nonTermAuto]) {
                        allNull = false;
                        break;
                    }
                }
                if (allNull && !epsilonDecider[grammarRuleAuto.leftHS]) {
                    epsilonDecider[grammarRuleAuto.leftHS] = true;
                    whileBool = true;
                }
            }
        }
    }

    // method that calculates the tokens that might appear right after every non-terminal
    // this is the building block for the FOLLOW set in the FIRST FOLLOW sets for SLR parser
    // this is the follow set from the report
    void calculateFollowSet() {

        follow [NonTerminals::Program].insert(Tokens::ENDOFFILE);
        bool whileBool = true;

        while (whileBool) {
            whileBool = false;

            // loop over all productions to see what follows every non-terminal
            for (int a = 0; a < grammarSize; a++) {
                auto& grammarRuleAuto = grammar[a];


                // loops over production rule looking for non-terminals
                for (int i = 0; i < static_cast<int>(grammarRuleAuto.RightHS.size()); i++) {
                    auto* nonTerm = std::get_if<NonTerminals>(&grammarRuleAuto.RightHS[i]);

                    if(!nonTerm) {
                        continue;
                    }

                   // scanning everything to the right of position i from previous for loop
                    for (int j = i + 1; j < static_cast<int>(grammarRuleAuto.RightHS.size()); j++) {
                        if (auto* token = std::get_if<Tokens>(&grammarRuleAuto.RightHS[j])) {
                            if(follow[*nonTerm].insert(*token).second) {
                                whileBool = true;
                            }
                            break;
                        }

                        // merge first of the next non-terminal into follow set
                        for (auto token : first[std::get<NonTerminals>(grammarRuleAuto.RightHS[j])]) {
                            if(follow[*nonTerm].insert(token).second)
                                whileBool = true;
                        }

                        if (!epsilonDecider[std::get<NonTerminals>(grammarRuleAuto.RightHS[j])]) {
                            break;
                        }
                    }

                    bool nullBool = true;

                    // checking if everything can do epsilon decider
                    for (int j = i + 1; j < static_cast<int>(grammarRuleAuto.RightHS.size()); j++) {
                        auto* nonTermAuto = std::get_if<NonTerminals>(&grammarRuleAuto.RightHS[j]);
                        if (!nonTermAuto || !epsilonDecider[*nonTermAuto]) {
                            nullBool = false;
                            break;
                        }
                    }

                    if (nullBool) {
                        for (auto tokensAuto : follow[grammarRuleAuto.leftHS]) {
                            if(follow[*nonTerm].insert(tokensAuto).second) {
                                whileBool = true;
                            }
                        }
                    }
                }
            }
        }
    }

    // method that constructs all states starting from state 0
    // this is done by calling the goToNextState method recursively and adding it to the list if not seen before
    // ends up with 130 states based on the CFG
    void buildStates() {
        ItemSet state0 = ItemSetExpander({{grammarSize,0}});
        states.push_back(state0);
        stateNumberMap[state0] = 0;

        // goes over all stats
        for (int i = 0; i < static_cast<int>(states.size()); i++) {
            std::set<tokOrNonT> tokOrNonTSet;

            // get all symbol after turing head in the state from previous for loop
            for (auto& item:states[i]) {
                auto& RHS = grammar[item.grammarRuleNumber].RightHS;

                if (item.dotPosition < static_cast<int>(RHS.size())) {
                    tokOrNonTSet.insert(RHS[item.dotPosition]);
                }
            }

            // find next state for each symbol
            for (auto& tokOrNonTAuto:tokOrNonTSet) {
                ItemSet itemSet = goToNextState(states[i],tokOrNonTAuto);

                if (itemSet.empty()) {
                    continue;
                }

                if (!stateNumberMap.count(itemSet)) {
                    stateNumberMap[itemSet] = static_cast<int>(states.size());
                    states.push_back(itemSet);
                }
            }
        }
    }

    // method that fills in action and goto tables by calling goToNextState recursively
    // this is the action table and go-to table from the report
    void buildTables() {

        int size = static_cast<int>(states.size());
        table.stateNumber = size;
        table.actionTable.resize(size);
        table.goToTable.resize(size);

        // for loop to go through all states, starts at 0 because there is a state 0
        // either adds item at index a to the action table or go to table
        for (int a = 0; a < size; a++) {
            for (auto& item : states[a]) {
                auto& RHS = grammar[item.grammarRuleNumber].RightHS;
                if (item.dotPosition < static_cast<int>(RHS.size())) {

                    auto& variantSymbol = RHS[item.dotPosition];
                    ItemSet itemSet = goToNextState(states[a], variantSymbol);
                    if (itemSet.empty()) {
                        continue;
                    }
                    int target = stateNumberMap.at(itemSet);

                    // adding to action table
                    if (auto* t = std::get_if<Tokens>(&variantSymbol)) {
                        table.actionTable[a][*t] = {ActionMoves::SHIFT, target};
                    }
                    // adding to go-to table
                    else {
                        table.goToTable[a][std::get<NonTerminals>(variantSymbol)] = target;
                    }

                }
                else {
                    if (item.grammarRuleNumber==grammarSize) {
                        table.actionTable[a][Tokens::ENDOFFILE] = {ActionMoves::ACCEPT,0};
                    }
                    else {
                        for (auto tokens : follow.at(grammar[item.grammarRuleNumber].leftHS)) {
                            table.actionTable[a][tokens] = {ActionMoves::REDUCE,item.grammarRuleNumber};
                        }
                    }
                }
            }
        }
    }
};

// a struct about a node within the parse tree
struct Node {

    // label, value, and nodeVector for variables
    std::string label;
    std::string value;
    std::vector<std::shared_ptr<Node>> nodeVector;


    // constructor that creates nodes based on the parameters
    // s2 is = "" because it is an optional value
    // terminal takes both, example(Keyword, while)
    // non-terminal just passes one, example(ArgumentList)
    Node(const std::string &s, const std::string &s2 = "") {
        label = s;
        value = s2;
    }

    // method to print the parse tree to the console
    // same thing with prePrint = "". parameter is optional for same reason
    void print(std::ostream& osStream, std::string prePrint = "") const {

        // if true means printing a non-terminal
        if (value.empty()) {
            osStream << prePrint << label;
        }

        // if true mean printing a terminal
        if (!value.empty()) {
            osStream << prePrint << value << " [" << label << "]";
        }

        // new line and tab indent to better view parse tree
        osStream << std::endl;
        std::string prePrintPlus = prePrint + "    ";

        // calls itself over and over until i is == to size of nodeVector
        for (int i = 0; i < static_cast<int>(nodeVector.size()); i++) {
            nodeVector[i]->print(osStream, prePrintPlus);
        }
    }
};

// the actual parser using all the rules and grammar defined earlier
// takes a parseTable, vector of grammarRules, and vector of tokens to use
std::shared_ptr<Node> parseFunction(const ParseTable& parseTable, const std::vector<grammarRule>& grammarRuleVector, const std::vector<Token>& tokens) {
    std::stack<int> stateStack;
    std::stack<std::shared_ptr<Node>> nodeStack;
    stateStack.push(0);
    int positionInt=0;

    // while true so it goes through whole lexor file recursively
    while (true) {
        int stackInt=stateStack.top();
        Tokens ToK = tokens[positionInt].type;

        // error detection for inputs that don't fit the rules
        if (parseTable.actionTable[stackInt].find(ToK) == parseTable.actionTable[stackInt].end()) {
            std::cout << "Bad Input: lexeme = " << tokens[positionInt].lex << " row = " << tokens[positionInt].row << " column = " << tokens[positionInt].column  << std::endl;
            return nullptr;
        }

        auto& anAuto= parseTable.actionTable[stackInt].find(ToK)->second;

        // Shift action from the report
        if (anAuto.AM == ActionMoves::SHIFT) {
            nodeStack.push(std::make_shared<Node>(tokenName(ToK), tokens[positionInt].lex));
            stateStack.push(anAuto.value); positionInt++;

            // reduce action from the report
        } else if (anAuto.AM == ActionMoves::REDUCE) {

            auto& anotherAuto=grammarRuleVector[anAuto.value];

            auto node = std::make_shared<Node>(nonTerminalName(anotherAuto.leftHS));

            std::vector<std::shared_ptr<Node>> nodeVector(static_cast<int>(anotherAuto.RightHS.size()));

            for (int i = static_cast<int>(anotherAuto.RightHS.size()) -1; i >= 0 ; i--) {
                nodeVector[i] = nodeStack.top();
                nodeStack.pop();
                stateStack.pop();
            }

            node -> nodeVector = std::move(nodeVector);
            nodeStack.push(node);
            auto gi = parseTable.goToTable[stateStack.top()].find(anotherAuto.leftHS);

            if (gi == parseTable.goToTable [stateStack.top()].end()) {
                std::cout << "Missing goto for " + nonTerminalName(anotherAuto.leftHS) << std::endl;
            }
            stateStack.push (gi->second);

            // Accept state from the report
        } else {
            auto programStart = std::make_shared<Node>("Program");
            programStart->nodeVector.push_back(nodeStack.top());
            return programStart;
        }
    }
}
 
int main(int argc, char *argv[]) {

    std::ofstream outputFile(argv[2]);

    // SLRBuilder that creates all rules and tables
    SLRBuilder builder;

    // reading in the lexor file (has to be the file from vand0475 lexor because of formatting)
    auto tokens= readLexerFile(argv[1]);

    // does the parsing of the tokens with the built grammar
    auto tree= parseFunction(builder.table, builder.grammar, tokens);

    // prints created tree with print function
    // prints to both console and output file
    tree -> print(std::cout);
    tree -> print(outputFile);

    return 0;
}
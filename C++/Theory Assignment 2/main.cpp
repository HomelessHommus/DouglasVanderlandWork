#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include <map>
#include <set>
#include <stack>
#include <variant>
#include <stdexcept>
#include <algorithm>
#include <memory>
 
// ═══════════════════════════════════════════════════════════════════
//  TOKENS
// ═══════════════════════════════════════════════════════════════════
 
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
    UNKNOWN
};
 
std::string tname(Tokens input) {
    switch (input) {
        case Tokens::INT:
            return "TokenKeyword(int)";
        case Tokens::CHAR:
            return "TokenKeyword(char)";
        case Tokens::IF:
            return "TokenKeyword(if)";
        case Tokens::ELSE:
            return "TokenKeyword(else)";
        case Tokens::WHILE:
            return "TokenKeyword(while)";
        case Tokens::FOR:
            return "TokenKeyword(for)";
        case Tokens::DO:
            return "TokenKeyword(do)";
        case Tokens::RETURN:
            return "TokenKeyword(return)";
        case Tokens::CLASS:
            return "TokenKeyword(class)";
        case Tokens::PUBLIC:
            return "TokenKeyword(public)";
        case Tokens::PRIVATE:
            return "TokenKeyword(private)";
        case Tokens::PROTECTED:
            return "TokenKeyword(protected)";
        case Tokens::NEW:
            return "TokenKeyword(new)";
        case Tokens::DELETE:
            return "TokenKeyword(delete)";
        case Tokens::IDENTIFIER:
            return "TokenIdentifier";
        case Tokens::LITERAL:
            return "TokenLiteral";
        case Tokens::SEMICOLON:
            return ";";
        case Tokens::COMMA:
            return ",";
        case Tokens::LEFTBRACKET:
            return "(";
        case Tokens::RIGHTBRACKET:
            return ")";
        case Tokens::LEFTSQUAREBRACKET:
            return "[";
        case Tokens::RIGHTSQUAREBRACKET:
            return "]";
        case Tokens::LEFTCURLYBRACKET:
            return "{";
        case Tokens::RIGHTCURLYBRACKET:
            return "}";
        case Tokens::COLON:
            return ":";
        case Tokens::ENDOFFILE:
            return "$";
        default:
            return "?";
    }
}
 
struct Token { Tokens type; std::string lex; int row, col; };
 
// ═══════════════════════════════════════════════════════════════════
//  READ LEXER OUTPUT FILE
//  Format: %-20s %-20s row %-20d column %d
// ═══════════════════════════════════════════════════════════════════
 
std::vector<Token> readLexerFile(const std::string& path) {
    std::ifstream f(path);
    if (!f) throw std::runtime_error("Cannot open: " + path);
 
    // keyword/delimiter string → T
    std::map<std::string,Tokens> kw = {
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
    std::map<std::string,Tokens> delim = {
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
 
    auto rtrim = [](std::string s) {
        auto p = s.find_last_not_of(' ');
        return p == std::string::npos ? "" : s.substr(0, p+1);
    };
 
    std::vector<Token> tokens;
    std::string line;
    while (std::getline(f, line)) {
        if (line.size() < 42) line.resize(42, ' ');
        std::string lex     = rtrim(line.substr(0, 20));
        std::string typeStr = rtrim(line.substr(21, 20));
        std::string rest    = line.substr(41);
 
        // parse row / column
        int row = 0, col = 0;
        auto rp = rest.find("row ");
        auto cp = rest.find("column ");
        if (rp != std::string::npos) row = std::stoi(rest.substr(rp+4));
        if (cp != std::string::npos) col = std::stoi(rest.substr(cp+7));
 
        Tokens type = Tokens::UNKNOWN;
        if (typeStr == "End of File") {
            type = Tokens::ENDOFFILE;
        }
        else if (typeStr == "Identifier") {
            type = Tokens::IDENTIFIER;
        }
        else if (typeStr == "Literal") {
            type = Tokens::LITERAL;
        }
        else if (typeStr == "Keyword") {
            auto it = kw.find(lex);
            if (it!=kw.end()) {
                type=it->second;
            }
        }
        else if (typeStr == "Delimiter") {
            auto it = delim.find(lex);
            if (it!=delim.end()) {
                type=it->second;
            }
        }
 
        if (type == Tokens::UNKNOWN) {
            continue;
        }// skip unknowns
        tokens.push_back({type, lex, row, col});
        if (type == Tokens::ENDOFFILE) {
            break;
        }
    }
    if (tokens.empty() || tokens.back().type != Tokens::ENDOFFILE) {
        tokens.push_back({Tokens::ENDOFFILE, "", 0, 0});
    }
    return tokens;
}
 
// ═══════════════════════════════════════════════════════════════════
//  GRAMMAR
// ═══════════════════════════════════════════════════════════════════
 
enum class NonTerminals {
    Program, ClassDeclarationList, ClassDeclaration,
    AccessSpecifierSections, AccessSpecifierSection, AccessSpecifier,
    MemberList, MemberDeclaration, VarDeclaration, VariableList,
    FunctionDeclaration, ParameterList, Parameter, VarType,
    CodeBlock, StatementList, Statement, MatchedStatement,
    UnmatchedStatement, NonIfStatement, Declaration, DeclarationTail,
    ExpressionStatement, WhileStatement, DoWhileStatement, ForStatement,
    InsideForStatement, ReturnStatement, Expression, PostfixExpression,
    PrimaryExpression, ArgumentList
};

std::string ntname(NonTerminals nonterms) {
    static const char* CFGList[] = {
        "Program","ClassDeclarationList","ClassDeclaration",
        "AccessSpecifierSections","AccessSpecifierSection","AccessSpecifier",
        "MemberList","MemberDeclaration","VarDeclaration","VariableList",
        "FunctionDeclaration","ParameterList","Parameter","VarType",
        "CodeBlock","StatementList","Statement","MatchedStatement",
        "UnmatchedStatement","NonIfStatement","Declaration","DeclarationTail",
        "ExpressionStatement","WhileStatement","DoWhileStatement","ForStatement",
        "ForInit","ReturnStatement","Expression","PostfixExpression",
        "PrimaryExpression","ArgumentList"
    };
    return CFGList[(int)nonterms];
}

using Sym = std::variant<Tokens, NonTerminals>;
struct Prod { NonTerminals lhs; std::vector<Sym> rhs; std::string name; };

// short aliases to keep table readable
static auto K(Tokens t) -> Sym {
    return t;
}
static auto N(NonTerminals n) -> Sym {
    return n;
}

std::vector<Prod> buildGrammar() {
    std::vector<Prod> G;
    auto add = [&](NonTerminals l, std::vector<Sym> r, std::string nm) {
        G.push_back({l, std::move(r), std::move(nm)});
    };
    using E = NonTerminals;
    add(E::Program,{N(E::ClassDeclarationList)},"Program→ClassDeclarationList");
    add(E::ClassDeclarationList,{N(E::ClassDeclaration)},"ClassDeclarationList→ClassDeclaration");
    add(E::ClassDeclarationList,{N(E::ClassDeclaration),N(E::ClassDeclarationList)},"ClassDeclarationList→ClassDeclaration ClassDeclarationList");
    add(E::ClassDeclaration,{K(Tokens::CLASS),K(Tokens::IDENTIFIER),K(Tokens::LEFTCURLYBRACKET),N(E::AccessSpecifierSections),K(Tokens::RIGHTCURLYBRACKET),K(Tokens::SEMICOLON)},"ClassDeclaration→class id { ... } ;");
    add(E::AccessSpecifierSections,{N(E::AccessSpecifierSection)},"AccessSpecifierSections→AccessSpecifierSection");
    add(E::AccessSpecifierSections,{N(E::AccessSpecifierSection),N(E::AccessSpecifierSections)},"AccessSpecifierSections→AccessSpecifierSection AccessSpecifierSections");
    add(E::AccessSpecifierSection,{N(E::AccessSpecifier),K(Tokens::COLON),N(E::MemberList)},"AccessSpecifierSection→AccessSpecifier : MemberList");
    add(E::AccessSpecifier,{K(Tokens::PUBLIC)},"AccessSpecifier→public");
    add(E::AccessSpecifier,{K(Tokens::PRIVATE)},"AccessSpecifier→private");
    add(E::AccessSpecifier,{K(Tokens::PROTECTED)},"AccessSpecifier→protected");
    add(E::MemberList,{N(E::MemberDeclaration)},"MemberList→MemberDeclaration");
    add(E::MemberList,{N(E::MemberDeclaration),N(E::MemberList)},"MemberList→MemberDeclaration MemberList");
    add(E::MemberDeclaration,{N(E::VarDeclaration)},"MemberDeclaration→VarDeclaration");
    add(E::MemberDeclaration,{N(E::FunctionDeclaration)},   "MemberDeclaration→FunctionDeclaration");
    add(E::VarDeclaration,{N(E::VarType),N(E::VariableList),K(Tokens::SEMICOLON)},"VarDeclaration→VarType VariableList ;");
    add(E::VariableList,{K(Tokens::IDENTIFIER)},"VariableList→id");
    add(E::VariableList,{K(Tokens::IDENTIFIER),K(Tokens::COMMA),N(E::VariableList)},"VariableList→id , VariableList");
    add(E::FunctionDeclaration,{N(E::VarType),K(Tokens::IDENTIFIER),K(Tokens::LEFTBRACKET),N(E::ParameterList),K(Tokens::RIGHTBRACKET),N(E::CodeBlock)},"FunctionDeclaration→VarType id ( ParameterList ) CodeBlock");
    add(E::FunctionDeclaration,{N(E::VarType),K(Tokens::IDENTIFIER),K(Tokens::LEFTBRACKET),K(Tokens::RIGHTBRACKET),N(E::CodeBlock)},"FunctionDeclaration→VarType id ( ) CodeBlock");
    add(E::ParameterList,{N(E::Parameter)},"ParameterList→Parameter");
    add(E::ParameterList,{N(E::Parameter),K(Tokens::COMMA),N(E::ParameterList)},"ParameterList→Parameter , ParameterList");
    add(E::Parameter,{N(E::VarType),K(Tokens::IDENTIFIER)},"Parameter→VarType id");
    add(E::VarType,{K(Tokens::INT)},"VarType→int");
    add(E::VarType,{K(Tokens::CHAR)},"VarType→char");
    add(E::CodeBlock,{K(Tokens::LEFTCURLYBRACKET),N(E::StatementList),K(Tokens::RIGHTCURLYBRACKET)},"CodeBlock→{ StatementList }");
    add(E::CodeBlock,{K(Tokens::LEFTCURLYBRACKET),K(Tokens::RIGHTCURLYBRACKET)},"CodeBlock→{ }");
    add(E::StatementList,{N(E::Statement)},"StatementList→Statement");
    add(E::StatementList,{N(E::Statement),N(E::StatementList)},"StatementList→Statement StatementList");
    add(E::Statement,{N(E::MatchedStatement)},"Statement→MatchedStatement");
    add(E::Statement,{N(E::UnmatchedStatement)},"Statement→UnmatchedStatement");
    add(E::MatchedStatement,{N(E::NonIfStatement)},"MatchedStatement→NonIfStatement");
    add(E::MatchedStatement,{K(Tokens::IF),K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET),N(E::MatchedStatement),K(Tokens::ELSE),N(E::MatchedStatement)},"MatchedStatement→if(Expr) Matched else Matched");
    add(E::UnmatchedStatement,{K(Tokens::IF),K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET),N(E::Statement)},"UnmatchedStatement→if(Expr) Statement");
    add(E::UnmatchedStatement,{K(Tokens::IF),K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET),N(E::MatchedStatement),K(Tokens::ELSE),N(E::UnmatchedStatement)},"UnmatchedStatement→if(Expr) Matched else Unmatched");
    add(E::NonIfStatement,{N(E::Declaration)},"NonIfStatement→Declaration");
    add(E::NonIfStatement,{N(E::ExpressionStatement)},"NonIfStatement→ExpressionStatement");
    add(E::NonIfStatement,{N(E::WhileStatement)},"NonIfStatement→WhileStatement");
    add(E::NonIfStatement,{N(E::DoWhileStatement)},"NonIfStatement→DoWhileStatement");
    add(E::NonIfStatement,{N(E::ForStatement)},"NonIfStatement→ForStatement");
    add(E::NonIfStatement,{N(E::ReturnStatement)},"NonIfStatement→ReturnStatement");
    add(E::NonIfStatement,{N(E::CodeBlock)},"NonIfStatement→CodeBlock");
    add(E::Declaration,{N(E::VarType),K(Tokens::IDENTIFIER),N(E::DeclarationTail),K(Tokens::SEMICOLON)},"Declaration→VarType id DeclarationTail ;");
    add(E::DeclarationTail,{},"DeclarationTail→ε");
    add(E::DeclarationTail,{K(Tokens::COMMA),K(Tokens::IDENTIFIER),N(E::DeclarationTail)},"DeclarationTail→, id DeclarationTail");
    add(E::ExpressionStatement,{N(E::Expression),K(Tokens::SEMICOLON)},"ExpressionStatement→Expression ;");
    add(E::WhileStatement,{K(Tokens::WHILE),K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET),N(E::CodeBlock)},"WhileStatement→while(Expr) CodeBlock");
    add(E::DoWhileStatement,{K(Tokens::DO),N(E::CodeBlock),K(Tokens::WHILE),K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET),K(Tokens::SEMICOLON)},"DoWhileStatement→do CodeBlock while(Expr);");
    add(E::ForStatement,{K(Tokens::FOR),K(Tokens::LEFTBRACKET),N(E::InsideForStatement),K(Tokens::SEMICOLON),N(E::Expression),K(Tokens::SEMICOLON),N(E::Expression),K(Tokens::RIGHTBRACKET),N(E::CodeBlock)},"ForStatement→for(ForInit;Expr;Expr) CodeBlock");
    add(E::InsideForStatement,{N(E::Expression)},"ForInit→Expression");
    add(E::InsideForStatement,{N(E::VarType),K(Tokens::IDENTIFIER)},"ForInit→VarType id");
    add(E::ReturnStatement,{K(Tokens::RETURN),N(E::Expression),K(Tokens::SEMICOLON)},"ReturnStatement→return Expr ;");
    add(E::Expression,{N(E::PostfixExpression)},"Expression→PostfixExpression");
    add(E::PostfixExpression,{N(E::PrimaryExpression)},"PostfixExpression→PrimaryExpression");
    add(E::PostfixExpression,{N(E::PostfixExpression),K(Tokens::LEFTSQUAREBRACKET),N(E::Expression),K(Tokens::RIGHTSQUAREBRACKET)},"PostfixExpression→PostfixExpr[Expr]");
    add(E::PostfixExpression,{N(E::PostfixExpression),K(Tokens::LEFTBRACKET),N(E::ArgumentList),K(Tokens::RIGHTBRACKET)},"PostfixExpression→PostfixExpr(ArgList)");
    add(E::PostfixExpression,{N(E::PostfixExpression),K(Tokens::LEFTBRACKET),K(Tokens::RIGHTBRACKET)},"PostfixExpression→PostfixExpr()");
    add(E::PrimaryExpression,{K(Tokens::IDENTIFIER)},"PrimaryExpression→id");
    add(E::PrimaryExpression,{K(Tokens::LITERAL)},"PrimaryExpression→lit");
    add(E::PrimaryExpression,{K(Tokens::LEFTBRACKET),N(E::Expression),K(Tokens::RIGHTBRACKET)}, "PrimaryExpression→(Expr)");
    add(E::PrimaryExpression,{K(Tokens::NEW),N(E::VarType),K(Tokens::LEFTBRACKET),K(Tokens::RIGHTBRACKET)}, "PrimaryExpression→new VarType()");
    add(E::PrimaryExpression,{K(Tokens::NEW),N(E::VarType),K(Tokens::LEFTSQUAREBRACKET),N(E::Expression),K(Tokens::RIGHTSQUAREBRACKET)}, "PrimaryExpression→new VarType[Expr]");
    add(E::PrimaryExpression,{K(Tokens::DELETE),K(Tokens::IDENTIFIER)},"PrimaryExpression→delete id");
    add(E::ArgumentList,{N(E::Expression)},"ArgumentList→Expression");
    add(E::ArgumentList,{N(E::Expression),K(Tokens::COMMA),N(E::ArgumentList)},"ArgumentList→Expr,ArgumentList");
    return G;
}
 
// ═══════════════════════════════════════════════════════════════════
//  SLR TABLE BUILDER
// ═══════════════════════════════════════════════════════════════════
 
struct Item {
    int prod, dot;
    bool operator<(const Item& o)  const { return prod!=o.prod ? prod<o.prod : dot<o.dot; }
    bool operator==(const Item& o) const { return prod==o.prod && dot==o.dot; }
};
using ISet = std::set<Item>;
 
enum class ActionMoves {
    SHIFT, REDUCE, ACCEPT, ERROR
};
struct Action { ActionMoves kind=ActionMoves::ERROR; int val=-1; };
 
struct ParseTable {
    int n=0;
    std::vector<std::map<Tokens,  Action>> act;
    std::vector<std::map<NonTerminals, int>>    go;
};
 
struct SLRBuilder {
    std::vector<Prod>  G;
    int                aug;
    std::map<NonTerminals,std::set<Tokens>> first, follow;
    std::map<NonTerminals,bool>        nullable;
    std::vector<ISet>        states;
    std::map<ISet,int>       sid;
    ParseTable               tbl;
 
    SLRBuilder() {
        G = buildGrammar();
        aug = (int)G.size();
        G.push_back(Prod{NonTerminals::Program,{(Sym)NonTerminals::Program},"S'→Program"});
        calcFirst(); calcFollow(); buildStates(); buildTable();
    }
 
    // FIRST of one symbol
    std::set<Tokens> firstSym(const Sym& s) const {
        if (auto* t = std::get_if<Tokens>(&s)) return {*t};
        return first.at(std::get<NonTerminals>(s));
    }
 
    // FIRST of suffix seq[from..]
    std::set<Tokens> firstSeq(const std::vector<Sym>& seq, int from=0) const {
        std::set<Tokens> r;
        for (int i=from; i<(int)seq.size(); i++) {
            auto fs = firstSym(seq[i]);
            r.insert(fs.begin(), fs.end());
            if (auto* n = std::get_if<NonTerminals>(&seq[i]); !n || !nullable.at(*n)) break;
        }
        return r;
    }
 
    bool seqNullable(const std::vector<Sym>& seq, int from=0) const {
        for (int i=from; i<(int)seq.size(); i++) {
            auto* n = std::get_if<NonTerminals>(&seq[i]);
            if (!n || !nullable.at(*n)) return false;
        }
        return true;
    }
 
    void calcFirst() {
        for (int i=0;i<(int)NonTerminals::_COUNT;i++) { first[(NonTerminals)i]={}; nullable[(NonTerminals)i]=false; }
        bool chg=true;
        while (chg) { chg=false;
            for (int pi=0;pi<aug;pi++) {
                auto& p=G[pi]; auto& fs=first[p.lhs];
                if (p.rhs.empty()) { if (!nullable[p.lhs]) { nullable[p.lhs]=true; chg=true; } continue; }
                for (auto& sym:p.rhs) {
                    for (auto t:firstSym(sym)) if(fs.insert(t).second) chg=true;
                    if (auto* n=std::get_if<NonTerminals>(&sym); !n||!nullable.at(*n)) break;
                }
                if (seqNullable(p.rhs) && !nullable[p.lhs]) { nullable[p.lhs]=true; chg=true; }
            }
        }
    }
 
    void calcFollow() {
        for (int i=0;i<(int)NonTerminals::_COUNT;i++) follow[(NonTerminals)i]={};
        follow[NonTerminals::Program].insert(Tokens::ENDOFFILE);
        bool chg=true;
        while (chg) { chg=false;
            for (int pi=0;pi<aug;pi++) {
                auto& p=G[pi];
                for (int i=0;i<(int)p.rhs.size();i++) {
                    auto* B=std::get_if<NonTerminals>(&p.rhs[i]); if(!B) continue;
                    for (auto t:firstSeq(p.rhs,i+1)) if(follow[*B].insert(t).second) chg=true;
                    if (seqNullable(p.rhs,i+1))
                        for (auto t:follow[p.lhs]) if(follow[*B].insert(t).second) chg=true;
                }
            }
        }
    }
 
    ISet closure(ISet I) const {
        bool chg=true;
        while (chg) { chg=false;
            for (auto item:std::vector<Item>(I.begin(),I.end())) {
                auto& rhs=G[item.prod].rhs;
                if (item.dot>=(int)rhs.size()) continue;
                auto* B=std::get_if<NonTerminals>(&rhs[item.dot]); if(!B) continue;
                for (int pi=0;pi<(int)G.size();pi++)
                    if (G[pi].lhs==*B) if(I.insert({pi,0}).second) chg=true;
            }
        }
        return I;
    }
 
    ISet goTo(const ISet& I, const Sym& X) const {
        ISet mv;
        for (auto& item:I) {
            auto& rhs=G[item.prod].rhs;
            if (item.dot<(int)rhs.size() && rhs[item.dot]==X)
                mv.insert({item.prod,item.dot+1});
        }
        return closure(mv);
    }
 
    void buildStates() {
        ISet s0=closure({{aug,0}});
        states.push_back(s0); sid[s0]=0;
        for (int si=0;si<(int)states.size();si++) {
            std::set<Sym> syms;
            for (auto& item:states[si]) {
                auto& rhs=G[item.prod].rhs;
                if (item.dot<(int)rhs.size()) syms.insert(rhs[item.dot]);
            }
            for (auto& X:syms) {
                ISet g=goTo(states[si],X);
                if (g.empty()) continue;
                if (!sid.count(g)) { sid[g]=(int)states.size(); states.push_back(g); }
            }
        }
    }
 
    void setAct(int s, Tokens t, Action a) {
        auto it=tbl.act[s].find(t);
        if (it!=tbl.act[s].end()) {
            if (it->second.kind==a.kind && it->second.val==a.val) return;
            throw std::runtime_error(
                "SLR conflict state "+std::to_string(s)+" on "+tname(t)+
                "\n  have: "+(it->second.kind==ActionMoves::SHIFT?"SHIFT ":"REDUCE ")+std::to_string(it->second.val)+
                "\n  new:  "+(a.kind==ActionMoves::SHIFT?"SHIFT ":"REDUCE ")+std::to_string(a.val));
        }
        tbl.act[s][t]=a;
    }
 
    void buildTable() {
        int n=(int)states.size();
        tbl.n=n; tbl.act.resize(n); tbl.go.resize(n);
        for (int si=0;si<n;si++) {
            for (auto& item:states[si]) {
                auto& rhs=G[item.prod].rhs;
                if (item.dot<(int)rhs.size()) {
                    auto& sym=rhs[item.dot];
                    ISet g=goTo(states[si],sym);
                    if (g.empty()) continue;
                    int tgt=sid.at(g);
                    if (auto* t=std::get_if<Tokens>(&sym))
                        setAct(si,*t,{ActionMoves::SHIFT,tgt});
                    else
                        tbl.go[si][std::get<NonTerminals>(sym)]=tgt;
                } else {
                    if (item.prod==aug) setAct(si,Tokens::ENDOFFILE,{ActionMoves::ACCEPT,0});
                    else for (auto t:follow.at(G[item.prod].lhs))
                        setAct(si,t,{ActionMoves::REDUCE,item.prod});
                }
            }
        }
    }
};
 
// ═══════════════════════════════════════════════════════════════════
//  PARSE TREE
// ═══════════════════════════════════════════════════════════════════
 
struct Node {
    std::string label, val;
    std::vector<std::shared_ptr<Node>> ch;
    Node(std::string l, std::string v="") : label(std::move(l)), val(std::move(v)) {}
    void print(std::ostream& os, std::string pre="", bool last=true) const {
        os << pre << (last?"└── ":"├── ") << label;
        if (!val.empty()) os << " [" << val << "]";
        os << "\n";
        std::string np = pre + (last?"    ":"│   ");
        for (int i=0;i<(int)ch.size();i++) ch[i]->print(os,np,i==(int)ch.size()-1);
    }
};
 
// ═══════════════════════════════════════════════════════════════════
//  SLR PARSER
// ═══════════════════════════════════════════════════════════════════
 
std::shared_ptr<Node> parse(const ParseTable& tbl,
                            const std::vector<Prod>& G,
                            const std::vector<Token>& tokens) {
    std::stack<int>                        ss;
    std::stack<std::shared_ptr<Node>>      ns;
    ss.push(0);
    int pos=0;
 
    while (true) {
        int s=ss.top();
        Tokens   t=tokens[pos].type;
        auto it=tbl.act[s].find(t);
        if (it==tbl.act[s].end()) {
            std::ostringstream e;
            e << "Syntax error at row " << tokens[pos].row
              << " col " << tokens[pos].col
              << ": unexpected '" << tokens[pos].lex << "'\n  expected:";
            for (auto& [tok,_]:tbl.act[s]) e << " " << tname(tok);
            throw std::runtime_error(e.str());
        }
        auto& a=it->second;
        if (a.kind==ActionMoves::SHIFT) {
            ns.push(std::make_shared<Node>(tname(t), tokens[pos].lex));
            ss.push(a.val); pos++;
        } else if (a.kind==ActionMoves::REDUCE) {
            auto& p=G[a.val];
            auto node=std::make_shared<Node>(ntname(p.lhs));
            int len=(int)p.rhs.size();
            std::vector<std::shared_ptr<Node>> ch(len);
            for (int i=len-1;i>=0;i--) { ch[i]=ns.top(); ns.pop(); ss.pop(); }
            node->ch=std::move(ch);
            ns.push(node);
            auto gi=tbl.go[ss.top()].find(p.lhs);
            if (gi==tbl.go[ss.top()].end())
                throw std::runtime_error("Missing goto for "+ntname(p.lhs));
            ss.push(gi->second);
        } else { // ACCEPT
            auto root=std::make_shared<Node>("Program");
            root->ch.push_back(ns.top());
            return root;
        }
    }
}
 
// ═══════════════════════════════════════════════════════════════════
//  MAIN
// ═══════════════════════════════════════════════════════════════════
 
int main(int argc, char* argv[]) {
    if (argc < 2) {
        std::cerr << "Usage: " << argv[0] << " <lexer_output_file> [--tree]\n";
        return 1;
    }
    bool printTree = (argc >= 3 && std::string(argv[2]) == "--tree");
 
    std::cout << "Building SLR table...\n";
    SLRBuilder builder;
    std::cout << "Done. " << builder.tbl.n << " states.\n\n";
 
    try {
        auto tokens = readLexerFile(argv[1]);
        auto tree   = parse(builder.tbl, builder.G, tokens);
        std::cout << "Input accepted.\n";
        if (printTree) tree->print(std::cout);
    } catch (const std::exception& e) {
        std::cout << e.what() << "\n";
        return 1;
    }
    return 0;
}
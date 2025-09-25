//
// COMP2711,8801 Computer Programming 2
// Flinders University
//

#include <iostream>
#include <string>
#include <unistd.h>

#include "Rolodex.h"

using namespace std;

auto verbose      = false; // Log Rolodex actions
auto printCurrent = false; // Output only the current Rolodex card

void rolodex()
{

    string word;
    Rolodex rolodex;
    while (cin >> word) {
        if (rolodex.isBeforeFirst() || rolodex.currentValue() <= word) {
            while (!rolodex.isAfterLast() && rolodex.currentValue() < word) {
                rolodex.rotateForward();
                if (verbose)
                    cerr << "rotateForward\n";
            }
            rolodex.insertBeforeCurrent(word);
            if (verbose)
                cerr << "insertBeforeCurrent\n";
        }
        else if (rolodex.isAfterLast() || rolodex.currentValue() >= word) {
            while (!rolodex.isBeforeFirst() && rolodex.currentValue() > word) {
                rolodex.rotateBackward();
                if (verbose)
                    cerr << "rotateBackward\n";
            }
            rolodex.insertAfterCurrent(word);
            if (verbose)
                cerr << "insertAfterCurrent\n";
        }
    }

    if (printCurrent) {
        cout << rolodex.currentValue() << '\n';
    }
    else { // Output all Rolodex card values (default)
        while (!rolodex.isBeforeFirst()) {
            rolodex.rotateBackward();
        }
        rolodex.rotateForward(); // Go to the first card
        while (!rolodex.isAfterLast()) {
            cout << rolodex.currentValue() << '\n';
            rolodex.rotateForward();
        }
    }
}

int main(int argc, char** argv)
{
    int c;
    while ((c = getopt(argc, argv, "vc")) != EOF) {
        switch (c) {
        case 'v':
            verbose = true;
            break;
        case 'c':
            printCurrent = true;
            break;
        }
    }
    argc -= optind;
    argv += optind;
    rolodex();
    return 0;
}

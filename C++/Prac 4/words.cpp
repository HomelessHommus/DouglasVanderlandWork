/**
 * Computer Programming 2 (COMP2711, COMP8801)
 * Practical 4: Words
 */

#include <iostream>
#include <string>
#include <unistd.h>
#include "MyVector.h"
using namespace std;


using namespace std;

int main(int argc, char** argv)
{
    enum {
        total,
        unique,
        iteration
    } mode = total;

    for (int c; (c = getopt(argc, argv, "tui")) != -1;) {
        switch (c) {

            case 't':
                mode = total;
            break;

            case 'u':
                mode = unique;
            break;

            case 'i':
                mode = iteration;
            break;
        }
    }
    argc -= optind;
    argv += optind;

   MyVector vector;
   if(mode == unique) {
       vector.unique();
   }
   else if(mode == total) {
        vector.total();
   }
   else if(mode == iteration) {
        vector.iteration();
   }

    switch (mode) {
        case total:
            cout << "Total: " << vector.getTotal() << '\n';
            break;
        case unique:
            cout << "Unique: " << vector.getUnique() << '\n';
            break;
        case iteration:
            vector.getIteration();
    }
    return 0;
}

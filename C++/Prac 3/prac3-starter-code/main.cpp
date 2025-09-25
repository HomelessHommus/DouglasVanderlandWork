#include "Zelotron.h"
#include <cstring>

int main(int argc, char *argv[]) {
    Zelotron z;
    z.read_program();
    z.run_program();
    return 0;
}

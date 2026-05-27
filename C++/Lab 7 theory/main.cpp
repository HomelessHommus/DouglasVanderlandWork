#include <string>
#include <iostream>

enum State {q1,q2};


bool run(const std::string& input) {
    State current = q1;

    for (char c : input) {
        if (c == '0') {
            current = q1;
        }

        else if (c == '1') {
            current = q2;
        }

        if (current == q1) {
            return true;
        }
            return false;

    }
}

    int main(){
        std::string input;
        std::cout << "input a binary number" << std::endl;
        std::cin >> input;

        bool result = run(input);

        if (result == 1) {
            std::cout << "True";
        }
        else{
            std::cout << "False";
        }
    }


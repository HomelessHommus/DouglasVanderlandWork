#include <iostream>
#include <algorithm>
#include <string>
#define MAX 1000
using namespace std;

std::string dec_to_binary(int dec) {
    // string binNum;
    // int modNum;
    // while(dec > 0) {
    //     modNum = dec % 2;
    //     dec = dec / 2;
    //     binNum = binNum + to_string(modNum);
    // }
    // int array[binNum.length];
    //
    // for (int i = binNum.length(); i >= 0; i--) {
    //     array[i] = binNum.at(i);
    // }
    // string actualNum;
    // for (int i = 0; i <= binNum.length(); i++) {
    //     actualNum = actualNum + to_string(array[i]);
    // }

    int modNum;
    string binNum = "";
    while (dec > 0) {
        modNum = dec % 2;
        binNum = binNum + to_string(modNum);
        dec = dec / 2;
    }

    reverse(binNum.begin(), binNum.end());

    return binNum;
}

int reverse_plus(int first, int second) {
    string revNum1 = to_string(first);
    string revNum2 = to_string(second);
    reverse(revNum1.begin(), revNum1.end());
    reverse(revNum2.begin(), revNum2.end());

    int reRevNum1 = stoi(revNum1);
    int reRevNum2 = stoi(revNum2);

    int addNum = reRevNum1 + reRevNum2;

    string sumRev = to_string(addNum);
    reverse(sumRev.begin(), sumRev.end());

    int finalNum = stoi(sumRev);

    return finalNum;
}

int multiply(int i, int array[], int arraySize) {
    int carry = 0;

    for (int a=0; a<arraySize; a++) {
        int total = array[a] * i + carry;
        array[a] = total % 10;
        carry = total / 10;
    }

    while (carry) {
        array[arraySize] = carry % 10;
        carry = carry / 10;
        arraySize++;
    }

    return arraySize;
}

string factorial(long n) {
    // long long product = 1;
    // for (int i = 1; i <= n; i++) {
    //     product = product * i;
    // }

    // for (int i = n; i > 0; i--) {
    //     aa[i] = i;
    // }

    // for (int i = 0; i < n; i++) {
    //     aa[i] = i + 1;
    // }
    //
    // long product = 1;
    // for (int i = 0; i < n; i++) {
    // product = product * aa[i];
    // }

    string product;
    string temp;
    int array[MAX];

    array[0] = 1;
    int arraySize = 1;

    for (int i=2; i<=n; i++) {
        arraySize = multiply(i, array, arraySize);
    }

    for (int b=arraySize-1; b>=0; b--) {
        temp = to_string(array[b]);
        product = product + temp;
    }

    return product;
}


int main(int argc, char const *argv[]) { // Don't modify anything in main!
    char *end;
    long which = 0;
    which=strtol(argv[1], &end, 10);
    switch(which){
        case 1: {
            long dec = strtol(argv[2], &end, 10);
            std::cout << dec << " in binary = " << dec_to_binary(dec) << std::endl;
            break;
        }
        case 2: {
            long first = strtol(argv[2], &end, 10);
            long second = strtol(argv[3], &end, 10);
            std::cout << "reverse_plus(first, second) = " << reverse_plus(first, second) << std::endl;
            break;
        }
        case 3: {
            long limit = strtol(argv[2], &end, 10);
            std::cout << limit << "! = " << factorial(limit) << std::endl;
            break;
        }
        default: {
            std::cout << "invalid option: " << which; break;
        }
    }
    return 0;
}

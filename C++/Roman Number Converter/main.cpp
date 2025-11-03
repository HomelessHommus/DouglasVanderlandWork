#include <iostream>

using namespace std;

int romanToDec(string word) {

    for(int i = 0; i <= word.length()-1; i++) {
        word[i] = toupper(word[i]);
    }
    int I = 1;
    int V = 5;
    int IV = 4;
    int IX = 9;
    int X = 10;
    int L = 50;
    int XL = 40;
    int XC = 90;
    int C = 100;
    int D = 500;
    int CD = 400;
    int CM = 900;
    int M = 1000;

    int finalNum = 0;
        for(int i = 0; i <= word.length()-1; i++) {
            if(word[i] == 'I') {
                if(word[i+1] == 'V') {
                    finalNum = finalNum + IV;
                    i++;
                }
                else if(word[i+1] == 'X') {
                    finalNum = finalNum + IX;
                    i++;
                }
                else {
                    finalNum = finalNum + I;
                }
            }
            else if(word[i] == 'V') {
                finalNum = finalNum + V;
            }
            else if(word[i] == 'X') {
                if(word[i+1] == 'L') {
                    finalNum = finalNum + XL;
                    i++;
                }
                else if(word[i+1] == 'C') {
                    finalNum = finalNum + XC;
                    i++;
                }
                else {
                    finalNum = finalNum + X;
                }
            }
            else if(word[i] == 'L') {
                finalNum = finalNum + L;
            }
            else if(word[i] == 'C') {
                if(word[i+1] == 'D') {
                    finalNum = finalNum + CD;
                    i++;
                }
                else if(word[i+1] == 'M') {
                    finalNum = finalNum + CM;
                    i++;
                }
                else {
                    finalNum = finalNum + C;
                }
            }
            else if(word[i] == 'D') {
                finalNum = finalNum + D;
            }
            else if(word[i] == 'M') {
                finalNum = finalNum + M;
            }
        }
    return finalNum;
}

string decToRoman(string input) {

    string units [10];
    units [1] = "I";
    units [2] = "II";
    units [3] = "III";
    units [4] = "IV";
    units [5] = "V";
    units [6] = "VI";
    units [7] = "VII";
    units [8] = "VIII";
    units [9] = "IX";

    string tens [10];
    tens [1] = "X";
    tens [2] = "XX";
    tens [3] = "XXX";
    tens [4] = "XL";
    tens [5] = "L";
    tens [6] = "LX";
    tens [7] = "LXX";
    tens [8] = "LXXX";
    tens [9] = "XC";

    string hundreds [10];
    hundreds [1] = "C";
    hundreds [2] = "CC";
    hundreds [3] = "CCC";
    hundreds [4] = "CD";
    hundreds [5] = "D";
    hundreds [6] = "DC";
    hundreds [7] = "DCC";
    hundreds [8] = "DCCC";
    hundreds [9] = "CM";

    string thousands [4];
    thousands [1] = "M";
    thousands [2] = "MM";
    thousands [3] = "MMM";

    string finalWord;

        int output = stoi(input);
        int thousand = output / 1000;
        int hundred = (output / 100) % 10;
        int ten = (output / 10) % 10;
        int unit = (output % 10);

        if (thousand != 0) {
            finalWord = finalWord + thousands[thousand];
        }
        if (hundred != 0) {
            finalWord = finalWord + hundreds[hundred];
        }
        if (ten != 0) {
            finalWord = finalWord + tens[ten];
        }
        if (unit != 0) {
            finalWord = finalWord + units[unit];
        }
    return finalWord;
}

void conversion() {

    string convert;
    while(getline(cin, convert)) {
        if(
        convert[0] == '0' || convert[0] == '1' ||
        convert[0] == '2' || convert[0] == '3' ||
        convert[0] == '4' || convert[0] == '5' ||
        convert[0] == '6' || convert[0] == '7' ||
        convert[0] == '8' || convert[0] == '9'){
            cout << decToRoman(convert) << endl;
        }
        else if(convert[0] == 'i' || convert[0] == 'I' ||
                convert[0] == 'v' || convert[0] == 'V' ||
                convert[0] == 'x' || convert[0] == 'X' ||
                convert[0] == 'l' || convert[0] == 'L' ||
                convert[0] == 'c' || convert[0] == 'C' ||
                convert[0] == 'm' || convert[0] == 'M' ||
                convert[0] == 'd' || convert[0] == 'D'){
            cout << romanToDec(convert) << endl;
        }
        else {
            cout << 0 << endl;
        }
    }
}

void plusMode() {

    string convert;
    while(getline(cin, convert)) {
        if(
        convert[0] == '0' || convert[0] == '1' ||
        convert[0] == '2' || convert[0] == '3' ||
        convert[0] == '4' || convert[0] == '5' ||
        convert[0] == '6' || convert[0] == '7' ||
        convert[0] == '8' || convert[0] == '9'){
            cout << decToRoman(convert) << endl;
        }
        else{
            cout << romanToDec(convert) << endl;
        }
    }
}

int main(int argc, char const *argv[]) {
    if(argc > 1) {
        plusMode();
    }
    else {
        conversion();
    }

    double arr[] = {1.1};
}

//
// Created by dougl on 7/10/2024.
//

#ifndef MYVECTOR_H
#define MYVECTOR_H

#include <iostream>
#include <string>
#include <unistd.h>
using namespace std;



class MyVector {

public:
    void unique();
    int getUnique();

    void iteration();
    void getIteration();

    void total();
    int getTotal();


private:
    string arrayTemp[1000];
    string arrayUnique[1000];
    string word;
    string swapTemp;
    int swapTempInt = 0;
    int index = 0;
    int count = 0;
    int arrayInt = 0;
    int uniqueCount = 0;
    int uniqueFinal = 0;
    int orderCheck = 0;
    bool swap = true;


    struct WordInfo {
        string text;
        int count;
    };

    WordInfo arrayInter [1000];



};



#endif //MYVECTOR_H

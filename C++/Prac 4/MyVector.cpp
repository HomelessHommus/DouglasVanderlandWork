//
// Created by dougl on 7/10/2024.
//

#include "MyVector.h"
#include <sstream>
#include <iostream>
#include <algorithm>
#include <string>
using namespace std;

void MyVector::unique() {
    string sentence;
    string words;
    getline(cin, sentence);
    istringstream stream(sentence);
    while(stream >> words) {
        arrayTemp[index++] = words;
    }
    arrayInt = 0;
        for(int i = 0; i < index; i++) {
            for(int a = 0; a < index; a++) {
                if(arrayUnique[a] == arrayTemp [i]) {
                    arrayInt++;
                    break;
                }
            }
            if(arrayInt > 0) {

            }
            else {
                arrayUnique[i] = arrayTemp[i];
            }
        }
        uniqueCount = 0;
        uniqueFinal = index-arrayInt;
    }

int MyVector::getUnique() {
    return uniqueFinal;
}

void MyVector::iteration() {
    string sentence;
    string words;
    getline(cin, sentence);
    istringstream stream(sentence);
    while(stream >> words) {
        arrayInt = 0;
        for(int i = 0; i < index; i++) {
            if(words == arrayInter[i].text) {
                arrayInter[i].count += 1;
                arrayInt++;
            }
        }
        if(arrayInt == 0) {
            arrayInter[index].text = words;
            arrayInter[index].count = 1;
            index++;
        }

    }
    while (orderCheck == 0) {
        swap = true;
        for(int i = 0; i < index - 1; i++) {
            if(arrayInter[i].text > arrayInter[i+1].text) {
                swapTemp = arrayInter[i].text;
                swapTempInt = arrayInter[i].count;
                arrayInter[i].text = arrayInter[i+1].text;
                arrayInter[i].count = arrayInter[i+1].count;
                arrayInter[i+1].text = swapTemp;
                arrayInter[i+1].count = swapTempInt;
                swap = false;
            }
        }
        if(swap == true) {
            orderCheck = 1;
        }
    }
}

void MyVector::getIteration() {
    for(int i = 0; i < index; i++) {
        cout << arrayInter[i].text << ": " << arrayInter[i].count << endl;
    }
}

void MyVector::total() {
    string *array = new string[1000];
    string sentence;
    string words;
    getline(cin, sentence);
    istringstream stream(sentence);
    count = 1000;
    while (stream >> words) {
        if (index < count) {
            array[index] = words;
        } else {
            string *bigArray = new string[count * 2];
            for (int i = 0; i < count; i++) {
                bigArray[i] = array[i];
            }
            delete [] array;
            array = bigArray;
            count *= 2;
            array[index] = words;
        }
        index++;
    }
}

int MyVector::getTotal() {
    return index;
}








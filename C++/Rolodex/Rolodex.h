//
// COMP2711,8801 Computer Programming 2
// Flinders University
//

#ifndef ROLODEX_H
#define ROLODEX_H

#include <string>
#include <iostream>
using namespace std;

struct RolodexItem {
 string value_;
 RolodexItem* next_;
 RolodexItem* prev_;

 RolodexItem(string val) : value_(val), next_(nullptr), prev_(nullptr) {}
};

class Rolodex {

public:
    /**
     * Creates a new empty Rolodex.
     */

    Rolodex() : sentinal(new RolodexItem("sentinal")) {
     sentinal->next_=sentinal;
     sentinal->prev_=sentinal;
    }

    /**
     * Returns true if the Rolodex is positioned before the first card.
     */
    bool isBeforeFirst() const;

    /**
     * Returns true if the Rolodex is positioned after the last card.
     */
    bool isAfterLast() const;

    /**
     * Rotates the Rolodex one card forwards.
     */
    void rotateForward();

    /**
     * Rotates the Rolodex one card backwards.
     */
    void rotateBackward();

    /**
     * Returns the value of the current card.
     */
    const string& currentValue() const;
    /**
     * Inserts a new card after the current card and positions the Rolodex
     * at the newly inserted card.
     *
     * @param value The value to insert into a new card.
     */
    void insertAfterCurrent(const string& value);

    /**
     * Inserts a new card before the current card and positions the Rolodex
     * at the newly inserted card.
     *
     * @param value The value to insert into a new card.
     */
    void insertBeforeCurrent(const string& value);

private:
  RolodexItem* sentinal;




};

#endif //ROLODEX_H

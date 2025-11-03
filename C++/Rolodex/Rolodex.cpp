//
// COMP2711,8801 Computer Programming 2
// Flinders University
//

#include "Rolodex.h"


/**
 * Returns true if the Rolodex is positioned before the first card.
 */
bool Rolodex::isBeforeFirst() const
{
}

/**
 * Returns true if the Rolodex is positioned after the last card.
 */
bool Rolodex::isAfterLast() const
{
    // Replace with real code
    return false;
}

/**
 * Rotates the Rolodex one card forwards.
 */
void Rolodex::rotateForward()
{
    // Add code here
}

/**
 * Rotates the Rolodex one card backwards.
 */
void Rolodex::rotateBackward()
{
    // Add code here
}

/**
 * Returns the value of the current card.
 */
const std::string& Rolodex::currentValue() const
{
    // Temporary hack to keep the compiler happy
    // Replace both lines with real code
    static std::string dummy;
    return dummy;
}

/**
 * Inserts a new card after the current card and positions the Rolodex
 * at the newly inserted card.
 *
 * @param value The value to insert into a new card.
 */
void Rolodex::insertAfterCurrent(const std::string& value)
{
    // Add code here
}

/**
 * Inserts a new card before the current card and positions the Rolodex
 * at the newly inserted card.
 *
 * @param value The value to insert into a new card.
 */
void Rolodex::insertBeforeCurrent(const std::string& value)
{
    // Add code here
}

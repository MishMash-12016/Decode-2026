package edu.wpi.first.math.numbers;

import edu.wpi.first.math.Nat;
import edu.wpi.first.math.Num;

/** A class representing the number 0. */
public final class N0 extends Num implements Nat<N0> {
    private N0() {}

    /**
     * The integer this class represents.
     *
     * @return The literal number 0.
     */
    @Override
    public int getNum() {
        return 0;
    }

    /** The singleton instance of this class. */
    public static final N0 instance = new N0();
}

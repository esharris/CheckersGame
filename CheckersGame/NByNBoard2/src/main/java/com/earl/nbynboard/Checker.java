package com.earl.nbynboard;

import java.util.Objects;

/**
 *
 * A piece on the game board.
 *
 * @author earlharris
 *
 */
public final class Checker {

    private final CheckerColor checkerColor;
    private final CheckerRank checkerRank;

    /**
     *
     * @param checkerColor
     * @param checkerRank
     */
    public Checker(CheckerColor checkerColor, CheckerRank checkerRank) {
        this.checkerColor = checkerColor;
        this.checkerRank = checkerRank;
    }

    public CheckerColor getCheckerColor() {
        return checkerColor;
    }

    public CheckerRank getCheckerRank() {
        return checkerRank;
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Checker.class);
        stringBuffer.append("(");
        stringBuffer.append(checkerColor);
        stringBuffer.append(", ");
        stringBuffer.append(checkerRank);
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkerColor, checkerRank);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Checker other = (Checker) obj;
        return checkerColor == other.checkerColor && checkerRank == other.checkerRank;
    }
}

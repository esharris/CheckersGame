package com.earl.nbynboard;

import java.util.Objects;

/**
 *
 * @author earlharris
 *
 */
public final class Cell {

    public static final Cell UNOCCUPIED_CELL = new Cell();

    private final boolean isOccupied;
    private final Checker checker;

    /**
     *
     */
    public Cell() {
        this.isOccupied = false;
        this.checker = null;
    }

    /**
     *
     * @param checker
     */
    public Cell(Checker checker) {
        this.isOccupied = true;
        this.checker = checker;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Checker getChecker() {
        if (isOccupied) {
            return checker;
        } else {
            throw new CellNotOccupiedException();
        }
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Cell.class);
        stringBuffer.append("(");
        stringBuffer.append(isOccupied);
        if (isOccupied) {
            stringBuffer.append(", ");
            stringBuffer.append(checker);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isOccupied ? checker : new Checker(CheckerColor.RED, CheckerRank.REGULAR));
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
        Cell other = (Cell) obj;
        return isOccupied && other.isOccupied ? Objects.equals(checker, other.checker)
                : !isOccupied && !other.isOccupied;
    }
}

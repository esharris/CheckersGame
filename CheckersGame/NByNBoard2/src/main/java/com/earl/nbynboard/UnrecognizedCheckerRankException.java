package com.earl.nbynboard;

/**
 *
 * @author earlharris
 *
 */
public class UnrecognizedCheckerRankException extends RuntimeException {

    private static final long serialVersionUID = 1329537966618163789L;

    private final CheckerRank checkerRank;

    /**
     *
     * @param checkerRank
     */
    public UnrecognizedCheckerRankException(CheckerRank checkerRank) {
        super("Unrecognized CheckerRank" + checkerRank);
        this.checkerRank = checkerRank;
    }

    public CheckerRank getCheckerRank() {
        return checkerRank;
    }
}

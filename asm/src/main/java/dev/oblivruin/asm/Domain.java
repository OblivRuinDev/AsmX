package dev.oblivruin.asm;

public class Domain {
    /** Always bigger than or equal to 0 */
    public final int start;
    /** Always bigger than or equal to {@link #start} */
    public final int end;

    /**
     * Construct a new domain.
     *
     * @param start the start of the domain (inclusive)
     * @param end the end of the domain (exclusive)
     * @throws IllegalArgumentException if arguments is illegal
     */
    public Domain(int start, int end) {
        if (start > end || start < 0) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
    }
}

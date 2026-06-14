package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;

public record ExpirationEntry(long expirationTime, String nonce) implements Comparable<ExpirationEntry> {
    @Override
    public int compareTo(ExpirationEntry other) {
        int byExpiration = Long.compare(expirationTime, other.expirationTime);
        if (byExpiration != 0) {
            return byExpiration;
        }
        return nonce.compareTo(other.nonce);
    }
}

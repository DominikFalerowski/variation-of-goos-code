package auctionsniper;

class MissingValueException extends RuntimeException {
    public MissingValueException(String value) {
        super("Missing value exception: " + value);
    }
}

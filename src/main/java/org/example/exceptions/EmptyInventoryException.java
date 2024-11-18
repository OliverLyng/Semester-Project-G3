package org.example.exceptions;

// Adjust this !!

public class EmptyInventoryException extends Exception {
    public EmptyInventoryException() {
        super("Inventory is empty");
    }

    public EmptyInventoryException(String message) {
        super(message);
    }

    public EmptyInventoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyInventoryException(Throwable cause) {
        super(cause);
    }
}

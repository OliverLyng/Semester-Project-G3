package org.example.exceptions;

// Adjust this !!

public class MaintenanceException extends Exception {
    public MaintenanceException() {
        super("Maintenance is required");
    }

    public MaintenanceException(String message) {
        super(message);
    }

    public MaintenanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaintenanceException(Throwable cause) {
        super(cause);
    }
}

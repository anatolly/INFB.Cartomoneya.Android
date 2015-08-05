package com.intrafab.cartomoneya.ocr;

/**
 * Created by mikhailzubov on 24.07.15.
 */
/**
 * An OcrException is thrown when the recognition manager is failed to
 * initialize.
 *
 */
public class OcrException extends Exception {
    /** An ID for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new OcrException that includes the current stack trace.
     */
    public OcrException() {
        super();
    }

    /**
     * Constructs a new OcrException with the current stack trace, the specified
     * detail message and the specified cause.
     *
     * @param detailMessage
     *            the detail message for this exception.
     * @param throwable
     *            the cause of this exception.
     */
    public OcrException( final String detailMessage, final Throwable throwable ) {
        super( detailMessage, throwable );
    }

    /**
     * Constructs a new OcrException with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public OcrException( final String detailMessage ) {
        super( detailMessage );
    }

    /**
     * Constructs a new OcrException with the current stack trace and the specified cause.
     *
     * @param throwable
     *            the cause of this exception.
     */
    public OcrException( final Throwable throwable ) {
        super( throwable );
    }
}

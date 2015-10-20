package io.probedock.demo.arquillian.service;

/**
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
public class CalculationException extends Exception {
    public CalculationException(Throwable cause) {
        super(cause);
    }
}

package io.probedock.demo.arquillian.service;

import io.probedock.demo.arquillian.to.OperationTO;
import io.probedock.demo.arquillian.to.ResultTO;

import javax.ejb.Local;

/**
 * Calculator service.
 *
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
@Local
public interface CalculatorService {
    /**
     * Process an operation and return the computed result
     *
     * @param operation The operation to process
     * @return The result
     * @throws CalculationException When processing the operation is not possible
     */
    ResultTO process(OperationTO operation) throws CalculationException;
}

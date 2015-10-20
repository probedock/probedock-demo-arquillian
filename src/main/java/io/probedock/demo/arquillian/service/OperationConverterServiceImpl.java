package io.probedock.demo.arquillian.service;

import io.probedock.demo.arquillian.to.OperationTO;
import io.probedock.demo.arquillian.utils.OperationAddBuilder;
import io.probedock.demo.arquillian.utils.OperationDivBuilder;
import io.probedock.demo.arquillian.utils.OperationMulBuilder;
import io.probedock.demo.arquillian.utils.OperationSubBuilder;
import io.probedock.demo.junit.Operation;

import javax.ejb.Stateless;

/**
 * Implementation of {@link OperationConverterService}
 *
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
@Stateless
public class OperationConverterServiceImpl implements OperationConverterService {
    @Override
    public Operation convert(OperationTO operation) {
        // Check and convert the left operation
        Operation leftOperation = null;
        if (operation.getLeftOperation() != null) {
            leftOperation = convert(operation.getLeftOperation());
        }

        // Check and convert the right operation
        Operation rightOperation = null;
        if (operation.getRightOperation() != null) {
            rightOperation = convert(operation.getRightOperation());
        }

        // Convert the correct operation
        switch (operation.getType()) {
            case "add":
                return new OperationAddBuilder().build(operation.getLeft(), operation.getRight(), leftOperation, rightOperation);
            case "sub":
                return new OperationSubBuilder().build(operation.getLeft(), operation.getRight(), leftOperation, rightOperation);
            case "mul":
                return new OperationMulBuilder().build(operation.getLeft(), operation.getRight(), leftOperation, rightOperation);
            case "div":
                return new OperationDivBuilder().build(operation.getLeft(), operation.getRight(), leftOperation, rightOperation);
            default:
                throw new IllegalStateException("The operation type " + operation.getType() + " is unknown.");
        }
    }
}

package io.probedock.demo.arquillian.service;

import io.probedock.client.annotations.ProbeTest;
import io.probedock.client.annotations.ProbeTestClass;
import io.probedock.demo.arquillian.to.ErrorTO;
import io.probedock.demo.arquillian.to.OperationTO;
import io.probedock.demo.arquillian.utils.OperationBuilder;
import io.probedock.demo.junit.Operation;
import io.probedock.demo.junit.OperationAdd;
import io.probedock.demo.junit.OperationDiv;
import io.probedock.demo.junit.OperationMul;
import io.probedock.demo.junit.OperationSub;

import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the {@link OperationConverterService}
 *
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
@RunWith(Arquillian.class)
@ProbeTestClass(category = "Arquillian", contributors = "laurent.prevost@probedock.io", tickets = "feature-1", tags = "converter")
public class OperationConverterServiceImplTest {
	@EJB
	private OperationConverterService operationConverterService;

	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap
			.create(JavaArchive.class)
			.addPackage(CalculationException.class.getPackage())
			.addPackage(ErrorTO.class.getPackage())
			.addPackage(OperationBuilder.class.getPackage())
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return jar;
	}

	@Test
	@ProbeTest(tags = "add")
	public void itShouldBePossibleToConvertSingleAddOperation() {
		Operation op = operationConverterService.convert(new OperationTO("add", 5, 6));

		assertThat(op).isInstanceOf(OperationAdd.class);
		assertThat(op.getLeftOperand()).isEqualTo(5);
		assertThat(op.getRightOperand()).isEqualTo(6);
	}

	@Test
	@ProbeTest(tags = "sub")
	public void itShouldBePossibleToConvertSingleSubOperation() {
		Operation op = operationConverterService.convert(new OperationTO("sub", 5, 6));

		assertThat(op).isInstanceOf(OperationSub.class);
		assertThat(op.getLeftOperand()).isEqualTo(5);
		assertThat(op.getRightOperand()).isEqualTo(6);
	}

	@Test
	@ProbeTest(tags = "mul")
	public void itShouldBePossibleToConvertSingleMulOperation() {
		Operation op = operationConverterService.convert(new OperationTO("mul", 5, 6));

		assertThat(op).isInstanceOf(OperationMul.class);
		assertThat(op.getLeftOperand()).isEqualTo(5);
		assertThat(op.getRightOperand()).isEqualTo(6);
	}

	@Test
	@ProbeTest(tags = "div")
	public void itShouldBePossibleToConvertSingleDivOperation() {
		Operation op = operationConverterService.convert(new OperationTO("div", 5, 6));

		assertThat(op).isInstanceOf(OperationDiv.class);
		assertThat(op.getLeftOperand()).isEqualTo(5);
		assertThat(op.getRightOperand()).isEqualTo(6);
	}

	@Test
	@ProbeTest(tags = "composed", name = "It should be possible to convert this operation: 3 + (5 - (7 * (12 / 2)))")
	public void itShouldBePossibleToConvertComplexOperation() {
		Operation op = operationConverterService.convert(
			new OperationTO(
				"add",
				3,
				new OperationTO(
					"sub",
					5,
					new OperationTO(
						"mul",
						7,
						new OperationTO("div", 12, 2)
					)
				)
			)
		);

		assertThat(op).isInstanceOf(OperationAdd.class);
		assertThat(op.getLeftOperand()).isEqualTo(3);
		
		Operation opSub = op.getRightOperation();
		assertThat(opSub).isNotNull().isInstanceOf(OperationSub.class);
		assertThat(opSub.getLeftOperand()).isEqualTo(5);

		Operation opMul = opSub.getRightOperation();
		assertThat(opMul).isNotNull().isInstanceOf(OperationMul.class);
		assertThat(opMul.getLeftOperand()).isEqualTo(7);

		Operation opDiv = opMul.getRightOperation();
		assertThat(opDiv).isNotNull().isInstanceOf(OperationDiv.class);
		assertThat(opDiv.getLeftOperand()).isEqualTo(12);
		assertThat(opDiv.getRightOperand()).isEqualTo(2);
	}
}

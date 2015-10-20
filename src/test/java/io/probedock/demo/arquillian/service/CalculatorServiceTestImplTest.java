package io.probedock.demo.arquillian.service;

import io.probedock.client.annotations.ProbeTest;
import io.probedock.client.annotations.ProbeTestClass;
import io.probedock.demo.arquillian.to.ErrorTO;
import io.probedock.demo.arquillian.to.OperationTO;
import io.probedock.demo.arquillian.to.ResultTO;
import io.probedock.demo.arquillian.utils.OperationBuilder;
import io.probedock.rt.client.junit.ProbeDockRTRule;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for the {@link CalculatorService}
 *
 * @author Laurent Prevost <laurent.prevost@probedock.io>
 */
@RunWith(Arquillian.class)
@ProbeTestClass(category = "Arquillian", contributors = "laurent.prevost@probedock.io", tickets = "feature-2", tags = "calculator")
public class CalculatorServiceTestImplTest {
	@EJB
	private CalculatorService calculatorService;

	@Rule
	public ProbeDockRTRule rule = new ProbeDockRTRule();
	
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
	public void itShouldBePossibleToProcessSingleAdditionOperation() throws Exception {
		ResultTO res = calculatorService.process(new OperationTO("add", 3, 5));

		assertThat(res.getResult()).isEqualTo(8);
	}

	@Test
	@ProbeTest(tags = "sub")
	public void itShouldBePossibleToProcessSingleSubtractionOperation() throws Exception {
		ResultTO res = calculatorService.process(new OperationTO("sub", 8, 5));

		assertThat(res.getResult()).isEqualTo(3);
	}
	
	@Test
	@ProbeTest(tags = "mul")
	public void itShouldBePossibleToProcessSingleMultiplicationOperation() throws Exception {
		ResultTO res = calculatorService.process(new OperationTO("mul", 3, 5));

		assertThat(res.getResult()).isEqualTo(15);
	}

	@Test
	@ProbeTest(tags = "div")
	public void itShouldBePossibleToProcessSingleDivisionOperation() throws Exception {
		ResultTO res = calculatorService.process(new OperationTO("div", 15, 5));

		assertThat(res.getResult()).isEqualTo(3);
		
	}

	@Test
	@ProbeTest(tags = "add")
	public void itShouldNotBePossibleToProcessDivisionOperationWhenRightOperandIsZero() {
		try {
			calculatorService.process(new OperationTO("div", 3, 0));
			failBecauseExceptionWasNotThrown(CalculationException.class);
		}
		catch (CalculationException ce) { }
	}

	@Test
	@ProbeTest(tags = "composed", name = "It should be possible to process this operation: 3 + (5 - (7 * (12 / 2)))")
	public void itShouldBePossibleToProcessComplexOperation() throws Exception {
		ResultTO res = calculatorService.process(
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

		assertThat(res.getResult()).isEqualTo(-34);
	}
}

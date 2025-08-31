package com.hkcapital.portoflio;

import com.hkcapital.portoflio.ui.panels.configuartion.ConfigurationPanelTest;
import com.hkcapital.portoflio.ui.panels.instrument.InstrumentPanelTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class PortfolioPnlSimulatorApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		ConfigurableApplicationContext context = SpringApplication.run(PortfolioPnlSimulatorApplication.class, args);
		SwingUtilities.invokeLater(() -> {
			//PnLSimulatorFacad simulator = context.getBean(PnLSimulatorFacad.class);
			//simulator.createApplication();
			ConfigurationPanelTest instrumentPanelTest = context.getBean(ConfigurationPanelTest.class);
			instrumentPanelTest.launch();
		});
	}

}

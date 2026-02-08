package com.hkcapital.portoflio;

import com.hkcapital.portoflio.ui.PnLSimulatorFacad;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixPanelTest;
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
			PnLSimulatorFacad simulator = context.getBean(PnLSimulatorFacad.class);
			try
			{
				simulator.createApplication();
			} catch (UnsupportedLookAndFeelException e)
			{
				throw new RuntimeException(e);
			}
		});
	}

}

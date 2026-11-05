package com.hkcapital.portflio;

import com.hkcapital.portflio.ui.PnLSimulatorFacad;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@EnableScheduling
@SpringBootApplication
public class PortfolioPnlSimulatorApplication
{
	public static void main(String[] args) {

		com.formdev.flatlaf.FlatDarkLaf.setup();

		ConfigurableApplicationContext context = new SpringApplicationBuilder(PortfolioPnlSimulatorApplication.class)
				.headless(false)
				.run(args);
		SwingUtilities.invokeLater(() -> {
			PnLSimulatorFacad simulator = context.getBean(PnLSimulatorFacad.class);
			try {
				simulator.createApplication();
			} catch (UnsupportedLookAndFeelException e) {
				throw new RuntimeException(e);
			}
		});
	}
}

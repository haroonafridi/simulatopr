package com.hkcapital.portflio.ui.panels.configuartion.panels;

import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.registry.Service;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class ConfigurationPanelTest
{
    private final ServiceRegistery<Service> serviceRegistery;
    private final ConfigurationService configurationService;

    public ConfigurationPanelTest(final ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.configurationService = (ConfigurationService)this.serviceRegistery.getService(Service.ConfigurationService);
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        ConfigurationPanel instrumentPanel = new ConfigurationPanel(serviceRegistery, null);
        mainFrame.add(instrumentPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
        instrumentPanel.getSaveButton().addActionListener(e -> instrumentPanel.save());
        instrumentPanel.getRemoveButton().addActionListener(e -> instrumentPanel.remove());
        instrumentPanel.getCancelButton().addActionListener(e -> instrumentPanel.clear());
        instrumentPanel.getCloseButton().addActionListener(e -> mainFrame.dispose());
    }

    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

}

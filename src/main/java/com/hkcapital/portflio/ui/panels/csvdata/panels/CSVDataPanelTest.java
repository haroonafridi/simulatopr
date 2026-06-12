package com.hkcapital.portflio.ui.panels.csvdata.panels;

import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Component
public class CSVDataPanelTest
{
    private final ServiceRegistery<Service> serviceRegistery;

    public CSVDataPanelTest(ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        CSVDataPanel instrumentPanel = new CSVDataPanel(this.serviceRegistery);
        mainFrame.add(instrumentPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }

}

package com.hkcapital.portoflio.ui.panels.instrument;

import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.ui.panels.instrument.InstrumentPanel;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Component
public class InstrumentPanelTest
{
    private final ServiceRegistery<Service> serviceRegistery;

    public InstrumentPanelTest(ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        InstrumentPanel instrumentPanel = new InstrumentPanel(this.serviceRegistery);
        mainFrame.add(instrumentPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }

}

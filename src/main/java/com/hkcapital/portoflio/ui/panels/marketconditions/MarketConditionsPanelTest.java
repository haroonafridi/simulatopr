package com.hkcapital.portoflio.ui.panels.marketconditions;

import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import com.hkcapital.portoflio.service.Service;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MarketConditionsPanelTest
{
    private final MarketConditionsService marketconditionsService;
    private final InstrumentService instrumentService;

    private final ServiceRegistery<Service> serviceRegistery;

    public MarketConditionsPanelTest(ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.marketconditionsService = (MarketConditionsService)this.serviceRegistery.getService("MarketConditionsService");
        this.instrumentService = (InstrumentService)this.serviceRegistery.getService("InstrumentService");;
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        MarketConditionsPanel marketConditionsPanel = new MarketConditionsPanel(serviceRegistery, null);
        mainFrame.add(marketConditionsPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }

    public MarketConditionsService getMarketconditionsService()
    {
        return marketconditionsService;
    }

}

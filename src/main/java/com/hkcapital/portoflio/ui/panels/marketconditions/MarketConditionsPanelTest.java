package com.hkcapital.portoflio.ui.panels.marketconditions;

import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class MarketConditionsPanelTest
{
    private final MarketConditionsService marketconditionsService;
    private final InstrumentService instrumentService;

    public MarketConditionsPanelTest(MarketConditionsService marketConditionsService,
                                     InstrumentService instrumentService)
    {
        this.marketconditionsService = marketConditionsService;
        this.instrumentService = instrumentService;
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        MarketConditionsPanel marketConditionsPanel = new MarketConditionsPanel(marketconditionsService, instrumentService, null);
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

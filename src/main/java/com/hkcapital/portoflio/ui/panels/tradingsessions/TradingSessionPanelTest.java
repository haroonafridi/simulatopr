package com.hkcapital.portoflio.ui.panels.tradingsessions;

import com.hkcapital.portoflio.model.TradingSessions;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.service.TradingSessionsService;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class TradingSessionPanelTest
{
    private final ServiceRegistery<Service> serviceRegistery;
    private final TradingSessionsService<TradingSessions> tradingSessionService;


    public TradingSessionPanelTest(ServiceRegistery<Service> serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.tradingSessionService = (TradingSessionsService<TradingSessions>) serviceRegistery.getService(Service.TradingSessionsService);
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        TradingSessionPanel tradingSessionPanel = new TradingSessionPanel(serviceRegistery);
        mainFrame.add(tradingSessionPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
        tradingSessionPanel.getSaveButton().addActionListener(e -> tradingSessionPanel.save());
        tradingSessionPanel.getRemoveButton().addActionListener(e -> tradingSessionPanel.remove());
        tradingSessionPanel.getCancelButton().addActionListener(e -> tradingSessionPanel.clear());
        tradingSessionPanel.getCloseButton().addActionListener(e -> mainFrame.dispose());
    }

}

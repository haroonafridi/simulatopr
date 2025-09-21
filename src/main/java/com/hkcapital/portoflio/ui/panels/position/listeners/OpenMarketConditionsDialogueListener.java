package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.panels.marketconditions.dialogues.MarketConditionsDialogue;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsPanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsSourcePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenMarketConditionsDialogueListener implements ActionListener
{


    private final MarketConditionsSourcePanel marketConditionsSourcePanel;

    final ServiceRegistery<Service> serviceRegistery;

    private final Frame frame;


    public OpenMarketConditionsDialogueListener(MarketConditionsSourcePanel marketConditionsSourcePanel, //
                                                ServiceRegistery<Service> serviceRegistery, Frame frame)
    {
        this.marketConditionsSourcePanel = marketConditionsSourcePanel;
        this.serviceRegistery = serviceRegistery;
        this.frame = frame;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        MarketConditionsDialogue marketConditionsDialogue = //
                new MarketConditionsDialogue(frame, new MarketConditionsPanel(serviceRegistery,
                        marketConditionsSourcePanel));
        marketConditionsDialogue.setVisible(true);
    }
}

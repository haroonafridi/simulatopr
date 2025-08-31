package com.hkcapital.portoflio.ui.panels.instrument;

import com.hkcapital.portoflio.service.InstrumentService;
import com.hkcapital.portoflio.ui.panels.instrument.InstrumentPanel;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class InstrumentPanelTest
{
    private final InstrumentService instrumentService;

    public InstrumentPanelTest(InstrumentService instrumentService)
    {
        this.instrumentService = instrumentService;
    }

    public void launch()
    {
        JFrame mainFrame = new JFrame("PnL Simulator App");
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        contents.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200)); // margins
        InstrumentPanel instrumentPanel = new InstrumentPanel(instrumentService);
        mainFrame.add(instrumentPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
        instrumentPanel.getSaveButton().addActionListener(e -> instrumentPanel.save());
        instrumentPanel.getRemoveButton().addActionListener(e -> instrumentPanel.remove());
        instrumentPanel.getCancelButton().addActionListener(e -> instrumentPanel.clear());
        instrumentPanel.getCloseButton().addActionListener(e -> mainFrame.dispose());
    }

    public InstrumentService getInstrumentService()
    {
        return instrumentService;
    }

}

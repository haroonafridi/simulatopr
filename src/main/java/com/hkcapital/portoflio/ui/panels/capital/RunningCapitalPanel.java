package com.hkcapital.portoflio.ui.panels.capital;

import com.hkcapital.portoflio.model.RunningCapital;
import com.hkcapital.portoflio.ui.fields.NumberTextField;

import javax.swing.*;
import java.awt.*;

public class RunningCapitalPanel extends JPanel
{

    private  JLabel capitalLabel = new JLabel(Labels.Capital.getLabel());
    private  NumberTextField runningCapital = new NumberTextField();

    public RunningCapitalPanel(RunningCapital rc)
    {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        setBorder(BorderFactory.createTitledBorder(Labels.Capital.getLabel()));
        add(capitalLabel);
        runningCapital.setText(Double.toString(rc.getCapital()));
        add(runningCapital);
    }

    public RunningCapital getOpeningCapital()
    {
        return new RunningCapital(1, runningCapital.getDoubleValue());
    }

    public NumberTextField getRunningCapital()
    {
        return runningCapital;
    }

    public void setRunningCapital(NumberTextField runningCapital)
    {
        this.runningCapital = runningCapital;
    }
}

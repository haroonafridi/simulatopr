package com.hkcapital.portflio.ui.panels.srmatrix.dialogues;

import com.hkcapital.portflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portflio.ui.panels.srmatrix.panels.SRMatrixTolerancePanel;

import javax.swing.*;
import java.awt.*;

public class SRMatrixToleranceDialogue extends JDialog
{
    private final SRMatrixTolerancePanel sRMatrixTolerancePanel;

    public SRMatrixToleranceDialogue(Frame owner, final SRMatrixTolerancePanel sRMatrixTolerancePanel)
    {
        super(owner, Labels.SRMatrix.getLabel(), false);
        this.sRMatrixTolerancePanel = sRMatrixTolerancePanel;
        getContentPane().add(sRMatrixTolerancePanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}

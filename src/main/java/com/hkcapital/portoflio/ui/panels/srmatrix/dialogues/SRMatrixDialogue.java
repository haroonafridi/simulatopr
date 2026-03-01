package com.hkcapital.portoflio.ui.panels.srmatrix.dialogues;

import com.hkcapital.portoflio.ui.panels.srmatrix.labels.Labels;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixPanel;

import javax.swing.*;
import java.awt.*;

public class SRMatrixDialogue extends JDialog
{
    private final SRMatrixPanel srMatrixPanel;

    public SRMatrixDialogue(Frame owner, final SRMatrixPanel srMatrixPanel)
    {
        super(owner, Labels.SRMatrix.getLabel(), false);
        this.srMatrixPanel = srMatrixPanel;
        getContentPane().add(srMatrixPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        pack();
    }

}

package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.repository.registry.ServiceRegistery;
import com.hkcapital.portoflio.service.registry.Service;
import com.hkcapital.portoflio.ui.panels.srmatrix.dialogues.SRMatrixDialogue;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixPanel;
import com.hkcapital.portoflio.ui.panels.srmatrix.panels.SRMatrixSourcePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenSRMatrixDialogueListener implements ActionListener
{


    private final SRMatrixSourcePanel srMatrixSourcePanel;

    final ServiceRegistery<Service> serviceRegistery;

    private final Frame frame;


    public OpenSRMatrixDialogueListener(SRMatrixSourcePanel srMatrixSourcePanel, //
                                        ServiceRegistery<Service> serviceRegistery, Frame frame)
    {
        this.srMatrixSourcePanel = srMatrixSourcePanel;
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
        SRMatrixDialogue configurationDialogue = //
                new SRMatrixDialogue(frame, new SRMatrixPanel(serviceRegistery, srMatrixSourcePanel));
        configurationDialogue.setVisible(true);
    }
}

package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.Service;
import com.hkcapital.portoflio.ui.panels.configuartion.dialogues.ConfigurationDialogue;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationPanel;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationSourcePanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenConfigurationDialogueListener implements ActionListener
{


    private final ConfigurationSourcePanel configurationSourcePanel;

    final ServiceRegistery<Service> serviceRegistery;

    private final Frame frame;


    public OpenConfigurationDialogueListener(ConfigurationSourcePanel configurationSourcePanel, //
                                             ServiceRegistery<Service> serviceRegistery, Frame frame)
    {
        this.configurationSourcePanel = configurationSourcePanel;
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
        ConfigurationDialogue configurationDialogue = //
                new ConfigurationDialogue(frame, new ConfigurationPanel(serviceRegistery, configurationSourcePanel));
        configurationDialogue.setVisible(true);
    }
}

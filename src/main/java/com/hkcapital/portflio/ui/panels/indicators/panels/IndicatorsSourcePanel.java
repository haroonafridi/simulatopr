package com.hkcapital.portflio.ui.panels.indicators.panels;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.service.tradingindicators.TradingIndicatorsService;
import com.hkcapital.portflio.ui.UIBag;
import com.hkcapital.portflio.ui.fields.NumberTextField;
import com.hkcapital.portflio.ui.panels.indicators.labels.Labels;

import javax.swing.*;

import static com.hkcapital.portflio.ui.panels.indicators.labels.Labels.*;

public class IndicatorsSourcePanel extends UIBag
{

    private final TradingIndicatorsService tradingIndicatorService;
    private final JLabel idLabel = new JLabel((Labels.Id.getLabel()));
    private final NumberTextField id = new NumberTextField(60);
    private JComboBox<Instrument> instrumentList = new JComboBox<>();

    private final JLabel nameLabel = new JLabel(Name.name());
    private final JTextField name = new JTextField(40);
    private final JLabel periodLabel = new JLabel(Period.name());
    private final NumberTextField period = new NumberTextField(40);
    private final JLabel timeFrameLabel = new JLabel(TimeFrame.name());
    private final NumberTextField timeFrame = new NumberTextField(40);

    public IndicatorsSourcePanel(final TradingIndicatorsService configurationService)
    {
        super(IndicatorsSourcePanel.class);
        this.tradingIndicatorService = configurationService;
        setBorder(BorderFactory.createTitledBorder(Indicators.getLabel()));
        add(idLabel);
        add(id);
        id.setEnabled(false);
        add(instrumentList);
        add(nameLabel);
        add(name);
        add(periodLabel);
        add(period);
        add(timeFrameLabel);
        add(timeFrame);
    }

    public TradingIndicator getTradingIndicators()
    {

        if (id != null && id.getText() != "" && id.intValue() != null)
        {
            return tradingIndicatorService.findById(id.intValue());
        }

        return null;
    }


}

package com.hkcapital.portoflio.ui.panels.position.listeners;

import com.hkcapital.portoflio.model.MarketConditions;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.service.ConfigurationService;
import com.hkcapital.portoflio.service.MarketConditionsService;
import com.hkcapital.portoflio.service.PositionService;
import com.hkcapital.portoflio.ui.panels.configuartion.panels.ConfigurationSourcePanel;
import com.hkcapital.portoflio.ui.panels.marketconditions.panels.MarketConditionsSourcePanel;
import com.hkcapital.portoflio.ui.panels.position.PositionParameters;
import com.hkcapital.portoflio.ui.panels.position.tablemodels.PositionTableModel;
import com.hkcapital.portoflio.ui.panels.strategy.StrategyHeaderPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static com.hkcapital.portoflio.service.impl.PositionServiceImpl.calculatePosition;

public class AddPositionsButtonListener implements ActionListener
{
    private final MarketConditionsService marketConditionsService;
    private final MarketConditionsSourcePanel marketConditionsSourcePanel;
    private final ConfigurationService configurationService;

    private final ConfigurationSourcePanel configurationSourcePanel;

    private final StrategyHeaderPanel strategyHeaderPanel;

    private final List<Position> positionPnLList;

    private final PositionService positionService;

    private final PositionTableModel model;

    public AddPositionsButtonListener(final MarketConditionsService marketConditionsService,
                                      final MarketConditionsSourcePanel marketConditionsSourcePanel,
                                      final ConfigurationService configurationService,
                                      final ConfigurationSourcePanel configurationSourcePanel,
                                      final StrategyHeaderPanel strategyHeaderPanel,
                                      final  List<Position> positionPnLList,
                                      final PositionService positionService,
                                      final PositionTableModel model)
    {
        this.marketConditionsService = marketConditionsService;
        this.marketConditionsSourcePanel = marketConditionsSourcePanel;
        this.configurationService = configurationService;
        this.configurationSourcePanel = configurationSourcePanel;
        this.strategyHeaderPanel = strategyHeaderPanel;
        this.positionPnLList = positionPnLList;
        this.positionService = positionService;
        this.model = model;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        MarketConditions marketConditions = marketConditionsService.findById(marketConditionsSourcePanel.getPositionId().getIntValue());
        Position position = new Position();
        position.setInstrument(marketConditions.getInstrument());
        position.setConfigurtaion(configurationService.findById(configurationSourcePanel.getId().getIntValue()));
        position.setMarketConditions(marketConditions);
        position.setStrategy(strategyHeaderPanel.getStrategy());
        PositionParameters positionParameter = calculatePosition(positionPnLList);
        position.setAllowedFirePower(positionParameter.allowedFirePower());
        position.setPercentPnL(positionParameter.percentPnl());
        position.setPnl(positionParameter.pnl());
        position.setCapitalRemainingFirePower(positionParameter.remainingCapital());
        positionService.add(position);
        List<Position> positionList = positionService.findByStrategyId(strategyHeaderPanel.getStrategy().getId());
        model.updateData(positionList);
    }
}

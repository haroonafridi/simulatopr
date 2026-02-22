package com.hkcapital.portoflio.etoro.dto.portfolio;

import java.time.OffsetDateTime;
import java.util.List;

public class EtoroPortfolioMirrorDTO
{
    private Long mirrorId;
    private Integer cid;
    private Integer parentCid;
    private double stopLossPercentage;
    private boolean isPaused;
    private boolean copyExistingPositions;
    private double availableAmount;
    private double stopLossAmount;
    private double initialInvestment;
    private double depositSummary;
    private double withdrawalSummary;
    private List<EtoroPortfolioPositionDTO> positions;
    private List<Object> entryOrders;
    private List<Object> exitOrders;
    private String parentUsername;
    private double closedPositionsNetProfit;
    private OffsetDateTime startedCopyDate;
    private boolean pendingForClosure;
    private List<Object> parentMirrors;
    private Integer mirrorCalculationType;
    private List<Object> ordersForOpen;
    private List<Object> ordersForClose;
    private List<Object> ordersForCloseMultiple;
    private List<Object> delayedOrderForClose;
    private List<Object> delayedOrderForOpen;
    private Integer mirrorStatusId;

    public Long getMirrorId()
    {
        return mirrorId;
    }

    public void setMirrorId(Long mirrorId)
    {
        this.mirrorId = mirrorId;
    }

    public Integer getCid()
    {
        return cid;
    }

    public void setCid(Integer cid)
    {
        this.cid = cid;
    }

    public Integer getParentCid()
    {
        return parentCid;
    }

    public void setParentCid(Integer parentCid)
    {
        this.parentCid = parentCid;
    }

    public double getStopLossPercentage()
    {
        return stopLossPercentage;
    }

    public void setStopLossPercentage(double stopLossPercentage)
    {
        this.stopLossPercentage = stopLossPercentage;
    }

    public boolean isPaused()
    {
        return isPaused;
    }

    public void setPaused(boolean paused)
    {
        isPaused = paused;
    }

    public boolean isCopyExistingPositions()
    {
        return copyExistingPositions;
    }

    public void setCopyExistingPositions(boolean copyExistingPositions)
    {
        this.copyExistingPositions = copyExistingPositions;
    }

    public double getAvailableAmount()
    {
        return availableAmount;
    }

    public void setAvailableAmount(double availableAmount)
    {
        this.availableAmount = availableAmount;
    }

    public double getStopLossAmount()
    {
        return stopLossAmount;
    }

    public void setStopLossAmount(double stopLossAmount)
    {
        this.stopLossAmount = stopLossAmount;
    }

    public double getInitialInvestment()
    {
        return initialInvestment;
    }

    public void setInitialInvestment(double initialInvestment)
    {
        this.initialInvestment = initialInvestment;
    }

    public double getDepositSummary()
    {
        return depositSummary;
    }

    public void setDepositSummary(double depositSummary)
    {
        this.depositSummary = depositSummary;
    }

    public double getWithdrawalSummary()
    {
        return withdrawalSummary;
    }

    public void setWithdrawalSummary(double withdrawalSummary)
    {
        this.withdrawalSummary = withdrawalSummary;
    }

    public List<EtoroPortfolioPositionDTO> getPositions()
    {
        return positions;
    }

    public void setPositions(List<EtoroPortfolioPositionDTO> positions)
    {
        this.positions = positions;
    }

    public List<Object> getEntryOrders()
    {
        return entryOrders;
    }

    public void setEntryOrders(List<Object> entryOrders)
    {
        this.entryOrders = entryOrders;
    }

    public List<Object> getExitOrders()
    {
        return exitOrders;
    }

    public void setExitOrders(List<Object> exitOrders)
    {
        this.exitOrders = exitOrders;
    }

    public String getParentUsername()
    {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername)
    {
        this.parentUsername = parentUsername;
    }

    public double getClosedPositionsNetProfit()
    {
        return closedPositionsNetProfit;
    }

    public void setClosedPositionsNetProfit(double closedPositionsNetProfit)
    {
        this.closedPositionsNetProfit = closedPositionsNetProfit;
    }

    public OffsetDateTime getStartedCopyDate()
    {
        return startedCopyDate;
    }

    public void setStartedCopyDate(OffsetDateTime startedCopyDate)
    {
        this.startedCopyDate = startedCopyDate;
    }

    public boolean isPendingForClosure()
    {
        return pendingForClosure;
    }

    public void setPendingForClosure(boolean pendingForClosure)
    {
        this.pendingForClosure = pendingForClosure;
    }

    public List<Object> getParentMirrors()
    {
        return parentMirrors;
    }

    public void setParentMirrors(List<Object> parentMirrors)
    {
        this.parentMirrors = parentMirrors;
    }

    public Integer getMirrorCalculationType()
    {
        return mirrorCalculationType;
    }

    public void setMirrorCalculationType(Integer mirrorCalculationType)
    {
        this.mirrorCalculationType = mirrorCalculationType;
    }

    public List<Object> getOrdersForOpen()
    {
        return ordersForOpen;
    }

    public void setOrdersForOpen(List<Object> ordersForOpen)
    {
        this.ordersForOpen = ordersForOpen;
    }

    public List<Object> getOrdersForClose()
    {
        return ordersForClose;
    }

    public void setOrdersForClose(List<Object> ordersForClose)
    {
        this.ordersForClose = ordersForClose;
    }

    public List<Object> getOrdersForCloseMultiple()
    {
        return ordersForCloseMultiple;
    }

    public void setOrdersForCloseMultiple(List<Object> ordersForCloseMultiple)
    {
        this.ordersForCloseMultiple = ordersForCloseMultiple;
    }

    public List<Object> getDelayedOrderForClose()
    {
        return delayedOrderForClose;
    }

    public void setDelayedOrderForClose(List<Object> delayedOrderForClose)
    {
        this.delayedOrderForClose = delayedOrderForClose;
    }

    public List<Object> getDelayedOrderForOpen()
    {
        return delayedOrderForOpen;
    }

    public void setDelayedOrderForOpen(List<Object> delayedOrderForOpen)
    {
        this.delayedOrderForOpen = delayedOrderForOpen;
    }

    public Integer getMirrorStatusId()
    {
        return mirrorStatusId;
    }

    public void setMirrorStatusId(Integer mirrorStatusId)
    {
        this.mirrorStatusId = mirrorStatusId;
    }
}

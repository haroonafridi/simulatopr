package com.hkcapital.portoflio.etoro.dto.portfolio;


import java.util.List;

public class EtoroClientPortfolioDTO
{
    private List<EtoroPortfolioPositionDTO> positions;
    private double credit;
    private List<EtoroPortfolioMirrorDTO> mirrors;
    private List<EtoroPortfolioOrderDTO> orders;
    private List<Object> stockOrders; // Empty in JSON, Object used as placeholder
    private List<Object> entryOrders;
    private List<Object> exitOrders;
    private List<Object> ordersForOpen;
    private List<Object> ordersForClose;
    private List<Object> ordersForCloseMultiple;
    private double bonusCredit;

    public List<EtoroPortfolioPositionDTO> getPositions()
    {
        return positions;
    }

    public void setPositions(List<EtoroPortfolioPositionDTO> positions)
    {
        this.positions = positions;
    }

    public double getCredit()
    {
        return credit;
    }

    public void setCredit(double credit)
    {
        this.credit = credit;
    }

    public List<EtoroPortfolioMirrorDTO> getMirrors()
    {
        return mirrors;
    }

    public void setMirrors(List<EtoroPortfolioMirrorDTO> mirrors)
    {
        this.mirrors = mirrors;
    }

    public List<EtoroPortfolioOrderDTO> getOrders()
    {
        return orders;
    }

    public void setOrders(List<EtoroPortfolioOrderDTO> orders)
    {
        this.orders = orders;
    }

    public List<Object> getStockOrders()
    {
        return stockOrders;
    }

    public void setStockOrders(List<Object> stockOrders)
    {
        this.stockOrders = stockOrders;
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

    public double getBonusCredit()
    {
        return bonusCredit;
    }

    public void setBonusCredit(double bonusCredit)
    {
        this.bonusCredit = bonusCredit;
    }
}
package com.hkcapital.portflio.broker.etoro.instrument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class InstrumentResponse
{
    private int page;
    private int pageSize;
    private int totalItems;
    private List<EtoroInstrument> items;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<EtoroInstrument> getItems() {
        return items;
    }

    public void setItems(List<EtoroInstrument> items) {
        this.items = items;
    }
}

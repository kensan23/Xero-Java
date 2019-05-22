package com.xero.Business;


import com.xero.models.accounting.Invoice;
import com.xero.models.accounting.Invoices;
import org.threeten.bp.LocalDate;

import java.util.TreeMap;

public class InvoiceSummary {
    private TreeMap<LocalDate, Double> summary;

    /**
     *
     * @param invoices Xero Invoices Model
     */
    public InvoiceSummary(Invoices invoices) {

        summary = new TreeMap<LocalDate, Double>();
        for (Invoice i : invoices.getInvoices()) {

            if (getSummary().containsKey(i.getDate())) {
                getSummary().put(i.getDate(), getSummary().get(i.getDate()) + i.getTotal());
            } else {
                getSummary().put(i.getDate(), i.getTotal());
            }
        }

    }

    /**
     *
     * @return Ordered Treemap by LocalDate and Double of total
     */
    public TreeMap<LocalDate, Double> getSummary() {
        return summary;
    }
}


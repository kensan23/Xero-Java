package com.xero.example;

import com.xero.Business.InvoiceSummary;
import com.xero.api.Config;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.xero.api.JsonConfig;
import com.xero.api.client.AccountingApi;
import com.xero.api.ApiClient;
import com.xero.models.accounting.Invoices;
import com.google.gson.Gson;


@WebServlet(name = "InvoiceSummaryServlet")
public class InvoiceSummaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Config config = null;

    public InvoiceSummaryServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TokenStorage storage = new TokenStorage();
        String token = storage.get(request, "token");
        String tokenSecret = storage.get(request, "tokenSecret");

        if (storage.tokenIsNull(token)) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }

        try {
            config = JsonConfig.getInstance();
            System.out.println("Your user agent is: " + config.getUserAgent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        String ids = null;
        boolean includeArchived = false;
        String invoiceNumbers = null;
        String contactIDs = null;
        String statuses = null;
        boolean createdByMyApp = false;
        String where = "Type==\"ACCREC\"";

        ApiClient apiClientForAccounting = new ApiClient(config.getApiUrl(), null, null, null);
        AccountingApi accountingApi = new AccountingApi(config);
        accountingApi.setApiClient(apiClientForAccounting);
        accountingApi.setOAuthToken(token, tokenSecret);

        Invoices invoices = accountingApi.getInvoices(null, where, null, ids, invoiceNumbers, contactIDs, statuses, null, includeArchived, createdByMyApp, 2);
        InvoiceSummary invSum = new InvoiceSummary(invoices);

        String data = new Gson().toJson(invSum.getSummary());
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(data);

    }
}

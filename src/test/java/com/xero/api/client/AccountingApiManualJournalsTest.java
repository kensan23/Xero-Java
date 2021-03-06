package com.xero.api.client;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;


import com.xero.api.XeroApiException;
import com.xero.api.ApiClient;
import com.xero.example.CustomJsonConfig;

import com.xero.api.client.*;
import com.xero.models.accounting.*;

import com.xero.example.SampleData;

import org.threeten.bp.*;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class AccountingApiManualJournalsTest {

	CustomJsonConfig config;
	ApiClient apiClientForAccounting; 
	AccountingApi api; 

    private static boolean setUpIsDone = false;
	
	@Before
	public void setUp() {
		config = new CustomJsonConfig();
		apiClientForAccounting = new ApiClient("https://virtserver.swaggerhub.com/Xero/accounting/2.0.0",null,null,null);
		api = new AccountingApi(config);
		api.setApiClient(apiClientForAccounting);
		api.setOAuthToken(config.getConsumerKey(), config.getConsumerSecret());

        // ADDED TO MANAGE RATE LIMITS while using SwaggerHub to mock APIs
        if (setUpIsDone) {
            return;
        }

        try {
            System.out.println("Sleep for 30 seconds");
            Thread.sleep(60000);
        } catch(InterruptedException e) {
            System.out.println(e);
        }
        // do the setup
        setUpIsDone = true;
	}

	public void tearDown() {
		api = null;
		apiClientForAccounting = null;
	}

    @Test
    public void createManualJournalTest() throws IOException {
        System.out.println("@Test - createManualJournal");
        ManualJournals manualJournals = null;
        ManualJournals response = api.createManualJournal(manualJournals);

        assertThat(response.getManualJournals().get(0).getNarration(), is(equalTo("Foo bar")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount(), is(equalTo(100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount().toString(), is(equalTo("100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getAccountCode(), is(equalTo("400")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getDescription(), is(equalTo("Hello there")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getTaxType(), is(equalTo("NONE")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount(), is(equalTo(-100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount().toString(), is(equalTo("-100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getAccountCode(), is(equalTo("400")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getDescription(), is(equalTo("Goodbye")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTaxType(), is(equalTo("NONE"))); 
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTracking().get(0).getTrackingCategoryID(), is(equalTo(UUID.fromString("6a68adde-f210-4465-b0a9-0d8cc6f50762"))));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTracking().get(0).getTrackingOptionID(), is(equalTo(UUID.fromString("dc54c220-0140-495a-b925-3246adc0075f"))));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTracking().get(0).getName(), is(equalTo("Simpsons")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTracking().get(0).getOption(), is(equalTo("Bart")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getDate(), is(equalTo(LocalDate.of(2019,03,13))));  
        assertThat(response.getManualJournals().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.NOTAX)));
        assertThat(response.getManualJournals().get(0).getStatus(), is(equalTo(com.xero.models.accounting.ManualJournal.StatusEnum.DRAFT)));
        assertThat(response.getManualJournals().get(0).getShowOnCashBasisReports(), is(equalTo(true)));
        assertThat(response.getManualJournals().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-14T13:39:32.920-07:00"))));  
        assertThat(response.getManualJournals().get(0).getManualJournalID(), is(equalTo(UUID.fromString("d312dd5e-a53e-46d1-9d51-c569ef4570b7"))));
        assertThat(response.getManualJournals().get(0).getWarnings().get(0).getMessage(), is(equalTo("Account code '476' has been removed as it does not match a recognised account.")));
        assertThat(response.getManualJournals().get(0).getValidationErrors().get(0).getMessage(), is(equalTo("The total debits (100.00) must equal total credits (-10.00)")));
        //System.out.println(response.getManualJournals().get(0).toString());
    }
    
 
    @Test
    public void createManualJournalAttachmentByFileNameTest() throws IOException {
        System.out.println("@Test - createManualJournalAttachmentByFileName");
        UUID manualJournalID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        String fileName = "sample5.jpg";
        InputStream inputStream = CustomJsonConfig.class.getResourceAsStream("/helo-heros.jpg");
        byte[] body = IOUtils.toByteArray(inputStream);
        Attachments response = api.createManualJournalAttachmentByFileName(manualJournalID, fileName, body);

        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("47ac97ff-d4f9-48a0-8a8e-49fae29129e7"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("foobar.jpg")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/jpg")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/ManualJournals/0b159335-606b-485f-b51b-97b3b32bad32/Attachments/foobar.jpg")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("2878711"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));
        //System.out.println(response.getAttachments().get(0).toString());
    }

    @Test
    public void getManualJournalTest() throws IOException {
        System.out.println("@Test - getManualJournal");
        UUID manualJournalID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        ManualJournals response = api.getManualJournal(manualJournalID);

        assertThat(response.getManualJournals().get(0).getNarration(), is(equalTo("These aren't the droids you are looking for")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount(), is(equalTo(100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount().toString(), is(equalTo("100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getAccountCode(), is(equalTo("429")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getDescription(), is(equalTo("These aren't the droids you are looking for")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getTaxType(), is(equalTo("NONE")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount(), is(equalTo(-100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount().toString(), is(equalTo("-100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getAccountCode(), is(equalTo("200")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getDescription(), is(equalTo("Yes the are")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTaxType(), is(equalTo("NONE"))); 
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getDate(), is(equalTo(LocalDate.of(2019,03,11))));  
        assertThat(response.getManualJournals().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.NOTAX)));
        assertThat(response.getManualJournals().get(0).getStatus(), is(equalTo(com.xero.models.accounting.ManualJournal.StatusEnum.POSTED)));
        assertThat(response.getManualJournals().get(0).getShowOnCashBasisReports(), is(equalTo(true)));
        assertThat(response.getManualJournals().get(0).getHasAttachments(), is(equalTo(true)));
        assertThat(response.getManualJournals().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-11T19:19:48.147-07:00"))));  
        assertThat(response.getManualJournals().get(0).getManualJournalID(), is(equalTo(UUID.fromString("99cb8353-ce73-4a5d-8e0d-8b0edf86cfc4"))));        
        assertThat(response.getManualJournals().get(0).getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("166ca8f8-8bc6-4780-8466-a0e474d586ea"))));
        assertThat(response.getManualJournals().get(0).getAttachments().get(0).getFileName(), is(equalTo("giphy.gif")));
        assertThat(response.getManualJournals().get(0).getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/manualjournal/99cb8353-ce73-4a5d-8e0d-8b0edf86cfc4/Attachments/giphy.gif")));
        assertThat(response.getManualJournals().get(0).getAttachments().get(0).getMimeType(), is(equalTo("image/gif")));
        assertThat(response.getManualJournals().get(0).getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("495727"))));
        //System.out.println(response.getManualJournals().get(0).toString());
    }

    @Test
    public void getManualJournalAttachmentsTest() throws IOException {
        System.out.println("@Test - getManualJournalAttachments");
        UUID manualJournalID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        Attachments response = api.getManualJournalAttachments(manualJournalID);

        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("16e86f32-3e25-4209-8662-c0dfd91b654c"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/jpg")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/ManualJournals/0b159335-606b-485f-b51b-97b3b32bad32/Attachments/HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("2878711"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));
        //System.out.println(response.getAttachments().get(0).toString());
    }

    @Test
    public void getManualJournalsTest() throws IOException {
        System.out.println("@Test - getManualJournals");
        OffsetDateTime ifModifiedSince = null;
        String where = null;
        String order = null;
        Integer page = null;
        ManualJournals response = api.getManualJournals(ifModifiedSince, where, order, page);

        assertThat(response.getManualJournals().get(0).getNarration(), is(equalTo("Reversal: These aren't the droids you are looking for")));
        assertThat(response.getManualJournals().get(0).getDate(), is(equalTo(LocalDate.of(2019,03,20))));  
        assertThat(response.getManualJournals().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.NOTAX)));
        assertThat(response.getManualJournals().get(0).getStatus(), is(equalTo(com.xero.models.accounting.ManualJournal.StatusEnum.POSTED)));
        assertThat(response.getManualJournals().get(0).getShowOnCashBasisReports(), is(equalTo(true)));
        assertThat(response.getManualJournals().get(0).getHasAttachments(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-11T19:19:48.083-07:00"))));  
        assertThat(response.getManualJournals().get(0).getManualJournalID(), is(equalTo(UUID.fromString("0b159335-606b-485f-b51b-97b3b32bad32"))));
        //System.out.println(response.getManualJournals().get(0).toString());
    }

    @Test
    public void updateManualJournalTest() throws IOException {
        System.out.println("@Test - updateManualJournal");
        UUID manualJournalID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        ManualJournals manualJournals = null;
        ManualJournals response = api.updateManualJournal(manualJournalID, manualJournals);

        assertThat(response.getManualJournals().get(0).getNarration(), is(equalTo("Hello Xero")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount(), is(equalTo(100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getLineAmount().toString(), is(equalTo("100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getAccountCode(), is(equalTo("400")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getDescription(), is(equalTo("Hello there")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getTaxType(), is(equalTo("NONE")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(0).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount(), is(equalTo(-100.0)));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getLineAmount().toString(), is(equalTo("-100.0")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getAccountCode(), is(equalTo("400")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getDescription(), is(equalTo("Goodbye")));
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getTaxType(), is(equalTo("NONE"))); 
        assertThat(response.getManualJournals().get(0).getHasAttachments(), is(equalTo(false)));          
        assertThat(response.getManualJournals().get(0).getJournalLines().get(1).getIsBlank(), is(equalTo(false)));
        assertThat(response.getManualJournals().get(0).getDate(), is(equalTo(LocalDate.of(2019,03,10))));  
        assertThat(response.getManualJournals().get(0).getLineAmountTypes(), is(equalTo(com.xero.models.accounting.LineAmountTypes.NOTAX)));
        assertThat(response.getManualJournals().get(0).getStatus(), is(equalTo(com.xero.models.accounting.ManualJournal.StatusEnum.DRAFT)));
        assertThat(response.getManualJournals().get(0).getShowOnCashBasisReports(), is(equalTo(true)));
        assertThat(response.getManualJournals().get(0).getUpdatedDateUTC(), is(equalTo(OffsetDateTime.parse("2019-03-11T19:28:56.820-07:00"))));  
        assertThat(response.getManualJournals().get(0).getManualJournalID(), is(equalTo(UUID.fromString("07eac261-78ef-47a0-a0eb-a57b74137877"))));
        //System.out.println(response.getManualJournals().get(0).toString());
    }
    
    @Test
    public void updateManualJournalAttachmentByFileNameTest() throws IOException {
        System.out.println("@Test - updateManualJournalAttachmentByFileName");
        UUID manualJournalID = UUID.fromString("8138a266-fb42-49b2-a104-014b7045753d");  
        String fileName = "sample5.jpg";
        InputStream inputStream = CustomJsonConfig.class.getResourceAsStream("/helo-heros.jpg");
        byte[] body = IOUtils.toByteArray(inputStream);
        Attachments response = api.updateManualJournalAttachmentByFileName(manualJournalID, fileName, body);

        assertThat(response.getAttachments().get(0).getAttachmentID(), is(equalTo(UUID.fromString("16e86f32-3e25-4209-8662-c0dfd91b654c"))));
        assertThat(response.getAttachments().get(0).getFileName(), is(equalTo("HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getMimeType(), is(equalTo("image/jpg")));
        assertThat(response.getAttachments().get(0).getUrl(), is(equalTo("https://api.xero.com/api.xro/2.0/ManualJournals/0b159335-606b-485f-b51b-97b3b32bad32/Attachments/HelloWorld.jpg")));
        assertThat(response.getAttachments().get(0).getContentLength(), is(equalTo(new BigDecimal("2878711"))));
        assertThat(response.getAttachments().get(0).getIncludeOnline(), is(equalTo(null)));
        //System.out.println(response.getAttachments().get(0).toString());
    }
}

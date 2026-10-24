package com.liskart.pages;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;
import com.liskart.utilities.APIUtility;
import com.liskart.utilities.FileUtility;
import com.liskart.utilities.PdfUtility;

import io.restassured.response.Response;

public class SellerPage {
	private ActionDriver actionDriver;

	// Seller order history locator
	private By gstNumberInputLocator = By.xpath("//input[@id='formGst']");
	private By yourGstDetailsheaderTextLocator = By.xpath("//h1[text()='Your GST Details']");
	private By sellerRegisterBtnLocator = By.xpath("//button[text()='Register']");
	private By sellerLoginLinkLocator = By.xpath("//a[contains(text(),'Seller Login/Register')]");
	private By sellerOrderHistoryLinkLocator = By.xpath("//button[contains(text(),'Order History')]");
	private By loginAsSellerLoginBtn = By.xpath("//button[text()='Login as seller']");
	private By orderHistoryHeaderTextLocator = By.xpath("//h1[text()='Order history']");
	private By oderIdInputBoxLocatoor = By.xpath("//input[@placeholder='Put the order ID']");
	private By submitBtnLocator = By.xpath("//button[@type='submit' and text()='Submit']");
	private By orderStatusLocator = By
			.xpath("//span[contains(text(),'Grand Total: ')]/parent::span/parent::div/following-sibling::div//span[1]");
	private By acceptLinkLocator = By.xpath("//button[text()='Accept']");
	private By generateInvoiceLinkLocator = By.xpath("//button[text()='Generate Invoice']");
	private By modalGenerateInvoiceBtnLocator = By
			.xpath("//div[@class='modal-body']//button[text()='Generate Invoice']");
	private By closeBtnLocator = By.xpath("//button[text()='Close']");
	private By generateShippingLabelLinkLocator = By.xpath("//button[contains(text(),'Generate Shipping Label')]");
	private By downloadShippingLabelLinkLocator = By.xpath("//button[contains(text(),'Download Shipping Label')]");
	private By sellerLoginHeaderLocator = By.xpath("//h1[contains(text(),'Welcome to LISKart Seller Profile')]");

	public SellerPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void enterSellerGstNumber(String gstNumber) {
		this.actionDriver.enterText(gstNumberInputLocator, gstNumber);
	}

	public void clickOnSellerRegisterBtn() {
		this.actionDriver.waitForElementToBeClickable(sellerRegisterBtnLocator);
		this.actionDriver.clickJS(sellerRegisterBtnLocator);
	}

	public boolean verifyYourGstDetailsHeaderTextDisplayed() {
		return this.actionDriver.isDisplayed(yourGstDetailsheaderTextLocator);
	}

	public void clickOnSellerLoginLink() {
		this.actionDriver.scrollToElement(sellerLoginLinkLocator);
		this.actionDriver.clickJS(sellerLoginLinkLocator);
	}

	public boolean verifySellerLoginHeaderDisplayed() {
		return this.actionDriver.isDisplayed(sellerLoginHeaderLocator);
	}

	public void clickOnSellerOrderHistory() {
		this.actionDriver.click(sellerOrderHistoryLinkLocator);
	}

	public void clickonSellerLoginBtn() {
		this.actionDriver.clickUsingActions(loginAsSellerLoginBtn);
	}

	public boolean verifyOrderHistoryHeaderTextDisplayed() {
		return this.actionDriver.isDisplayed(orderHistoryHeaderTextLocator);
	}

	public void enterOrderId(String orderId) {
		this.actionDriver.enterText(oderIdInputBoxLocatoor, orderId);
	}

	public void clickOnSubmitBtn() {
		this.actionDriver.clickJS(submitBtnLocator);
	}

	public String orderStatus() {
		return this.actionDriver.getText(orderStatusLocator);
	}

	public void clickOnAcceptLink() {
		this.actionDriver.clickJS(acceptLinkLocator);
		this.actionDriver.waitForElementToBeVisible(orderStatusLocator);
	}

	public void clickOnGenerateInvoiceLink() {
		this.actionDriver.clickJS(generateInvoiceLinkLocator);
	}

	public void clickOnModalGenerateInvoiceBtn() {
		this.actionDriver.clickJS(modalGenerateInvoiceBtnLocator);
	}

	public void clickOnCloseBtn() {
		this.actionDriver.clickJS(closeBtnLocator);
	}

	public void clickOGenerateShippingLabelLink() {
		this.actionDriver.clickJS(generateShippingLabelLinkLocator);
		this.actionDriver.waitForElementToBeVisible(orderStatusLocator);
	}

	public void clickOnDownloadShippingLabelLink() {
		this.actionDriver.clickJS(downloadShippingLabelLinkLocator);
	}

	public void downloadShippingLabelAndDeliveredOrder() throws Exception {
		String downloadFolder = System.getProperty("user.dir") + "/downloads";
		File pdfFile = FileUtility.waitForPdfDownload(downloadFolder);
		String awbNo = PdfUtility.extractAwb(pdfFile);
		System.out.println("AWB Number: " + awbNo);
		Response response = APIUtility.markShipmentDelivered(awbNo);
		Assert.assertEquals(response.getStatusCode(), 200);
	}

	public void refreshSellerPageAfterDelivery() {
		this.actionDriver.refreshPage();
	}

}

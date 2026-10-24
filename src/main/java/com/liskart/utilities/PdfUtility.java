package com.liskart.utilities;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.liskart.base.BaseClass;

public class PdfUtility {

	public static final Logger logger = BaseClass.logger;

	public static String extractAwb(File pdfFile) throws Exception {

		logger.info("Extracting AWB number from PDF: " + pdfFile.getName());
		ExtentManager.logStep("Extracting AWB number from downloaded PDF");

		try {

			PDDocument document = PDDocument.load(pdfFile);

			PDFTextStripper stripper = new PDFTextStripper();

			String text = stripper.getText(document);

			document.close();

			Pattern pattern = Pattern.compile("\\b\\d{6,10}\\b");

			Matcher matcher = pattern.matcher(text);

			if (matcher.find()) {

				String awb = matcher.group();

				logger.info("AWB number extracted successfully: " + awb);

				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "AWB Extracted",
						"AWB number extracted successfully: " + awb);

				return awb;
			}

			logger.error("AWB number not found in PDF");

			ExtentManager.logFailure(BaseClass.getDriver(), "AWB Extraction Failed", "AWB number not found in PDF");

			throw new RuntimeException("AWB not found in PDF");

		} catch (Exception e) {

			logger.error("Failed to extract AWB from PDF: " + e.getMessage());

			ExtentManager.logFailure(BaseClass.getDriver(), "PDF Processing Failed",
					"Unable to read PDF file: " + e.getMessage());

			throw e;
		}
	}
}
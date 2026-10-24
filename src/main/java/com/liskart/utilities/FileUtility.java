package com.liskart.utilities;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.liskart.base.BaseClass;

public class FileUtility {

	public static final Logger logger = BaseClass.logger;

	public static File waitForPdfDownload(String folderPath) throws InterruptedException {

		File dir = new File(folderPath);

		logger.info("Waiting for PDF download in folder: " + folderPath);
		ExtentManager.logStep("Waiting for PDF download in folder: " + folderPath);

		for (int i = 0; i < 20; i++) {

			File[] files = dir.listFiles((d, name) -> name.endsWith(".pdf"));

			if (files != null && files.length > 0) {

				logger.info("PDF downloaded successfully: " + files[0].getName());
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "PDF Downloaded",
						"PDF downloaded successfully: " + files[0].getName());

				return files[0];
			}

			logger.info("PDF not found yet. Retry attempt: " + (i + 1));
			Thread.sleep(2000);
		}

		logger.error("PDF not downloaded within expected time.");
		ExtentManager.logFailure(BaseClass.getDriver(), "PDF Download Failed",
				"PDF not downloaded within expected time.");

		throw new RuntimeException("PDF not downloaded");
	}

	// Method to clear existing files from downloads folder
	public static void clearDownloadsFolder(String folderPath) {

		File folder = new File(folderPath);

		logger.info("Clearing downloads folder: " + folderPath);

		if (folder.exists()) {

			File[] files = folder.listFiles();

			if (files != null) {

				for (File file : files) {

					if (file.delete()) {
						logger.info("Deleted existing file: " + file.getName());
					} else {
						logger.warn("Unable to delete file: " + file.getName());
					}
				}
			}
		} else {

			logger.warn("Downloads folder does not exist: " + folderPath);
		}
	}
}
package com.kk.imageoperation.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.kk.imageoperation.Result.ImageOutput;
import com.kk.imageoperation.logs.Log;
import com.kk.imageoperation.operations.CovertImages;

public class CompareTwoImagesUsingNode  {

	private File	nodeModulesFolder;
	private File	nodeExeFolder;
	public String	workingDir;

	public CompareTwoImagesUsingNode(String nodeFolderPath) {
		workingDir  = nodeFolderPath;
	}
	
	public ImageOutput comapreTwoImagesUsingVisualQA(String image1, String image2, String outputImageName, String outputPath, double minimumError) throws IOException {
		ImageOutput imageComparasionOuput = new ImageOutput();
		// set module folders and node.exe folder
		nodeModulesFolder = new File(workingDir + "\\NodeJS\\node_modules");
		nodeExeFolder = new File(workingDir + "\\NodeJS\\");

		// Create a temporary directory
		File resultDir = createTempDirectory("comparasion_result");

		File outputFolder = new File(outputPath);

		Log.debug("Output folder is dir : " + outputFolder.exists());
		Log.debug("Output folder is exist : " + outputFolder.exists());

		if (outputFolder.exists() && outputFolder.isDirectory()) {
			resultDir = outputFolder;
		}

		String outputImage = "";

		if (outputImageName == null || outputImageName.equals("")) {
			outputImage = resultDir.getPath() + "\\" + "OutputImage" + ".png";
		} else {
			if (outputImageName.endsWith(".png"))
				outputImage = resultDir.getPath() + "\\" + outputImageName;
			else
				outputImage = resultDir.getPath() + "\\" + outputImageName + ".png";

		}

		Log.debug(outputImage);

		// convert Images
		CovertImages.convertToPNG(new File(image1));
		CovertImages.convertToPNG(new File(image2));

		// call method and store output.
		String[] output = compareTwoImagesUsingPixelMatcher(image1, image2, outputImage);

		if (!new File(outputImage).exists()) {
			imageComparasionOuput.setResultMessage("Failed to match");
			return imageComparasionOuput;
		}

		// Copy all images in single folder.

		File image1File = new File(image1);
		File image2File = new File(image2);

		Files.copy(image1File.toPath(), new File(resultDir + "/" + image1File.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(image2File.toPath(), new File(resultDir + "/" + image2File.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

		Log.debug("Result Dir Path" + resultDir.getAbsolutePath());

		// Set keyword output on the bases of method output
		if (output.length > 0 && output != null) {
			Log.debug("Image Dir " + output[1]);
			if (output[0].contains("error:")) {
				double resultError = getErrorPercentageFromOutPut(output[0]);
				if (resultError <= minimumError) {
					imageComparasionOuput.setImageMatchingScore(resultError);
					imageComparasionOuput.setResultImage(output[1]);
					imageComparasionOuput.setResultMessage("Image Matched");
					return imageComparasionOuput;
				} else {
					imageComparasionOuput.setImageMatchingScore(resultError);
					imageComparasionOuput.setResultImage(output[1]);
					imageComparasionOuput.setResultMessage(" \n Comparasion fail. Error should be " + minimumError + "But it was " + output[0] );
					return imageComparasionOuput;

				}
			} else {
				imageComparasionOuput.setImageMatchingScore(0);
				imageComparasionOuput.setResultImage(output[1]);
				imageComparasionOuput.setResultMessage(" \n Comparasion fail. Error should be " + minimumError + "But it was " + output[0] );
				return imageComparasionOuput;

			}
		} else {
			Log.debug("Output Null");
			imageComparasionOuput.setResultMessage("Failed to match");
			return imageComparasionOuput;

		}

	}

	private String[] compareTwoImagesUsingPixelMatcher(String inputImage1, String inputImage2, String outputImage) {
		// Binary we need.

		String pixelMatcherLocation = nodeModulesFolder.getAbsolutePath() + "\\pixelmatch\\bin\\pixelmatch";
		String processOutPut;
		String[] args = {
				"\"" + nodeExeFolder.getAbsolutePath() + "\\node.exe" + "\"",
				"\"" + pixelMatcherLocation + "\"",
				"\"" + inputImage1 + "\"",
				"\"" + inputImage2 + "\"",
				"\"" + outputImage + "\"", };

		try {
			// Run the process
			processOutPut = processRunner(args);
		} catch (IOException | InterruptedException e) {
			// In case of exception send null
			e.printStackTrace();
			return null;
		}

		return new String[] {
				processOutPut,
				outputImage };
	}

	private String processRunner(String[] args) throws IOException, InterruptedException {

		ProcessBuilder pb = new ProcessBuilder(args);
		Log.debug(pb.command());
		pb.redirectError(Redirect.INHERIT);
		Process p = pb.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		p.waitFor();
		Log.debug(builder.toString());
		p.destroy();
		return builder.toString();
	}

	private static File createTempDirectory(String FolderName) throws IOException {

		final File temp;

		temp = File.createTempFile(FolderName, "");
		Log.debug("Folder Created : " + temp.getAbsolutePath());
		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		}
		return (temp);
	}

	
	private static double getErrorPercentageFromOutPut(String outputFromJS) {
		String errorRaw = outputFromJS.split("error")[1];
		String errorPercentageString = errorRaw.replaceAll("[^\\w\\s\\.]", "");
		Double errorPercentage = Double.parseDouble(errorPercentageString);
		return errorPercentage;
	}

}
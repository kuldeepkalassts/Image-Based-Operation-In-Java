package com.kk.imageoperation.TemplateMatching;

import java.awt.Rectangle;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.kk.imageoperation.OpenCV.OpenCVConfig;
import com.kk.imageoperation.Result.ImageOutput;
import com.kk.imageoperation.logs.Log;

public class ObjectSearchUsingOpenCV {
	private double				pointX;
	private double				pointY;
	private double				pointX1;
	private double				pointY1;
	private long				mimimumMarksforAlgoC	= 999;
	private long				mFactor					= 1000;
	private long				mimimumMarksforAlgoB	= 417;
	private ImageOutput	imageSearchOutput		= new ImageOutput();
	int							match_method			= Imgproc.TM_CCORR_NORMED;

	public ObjectSearchUsingOpenCV(String openCVLibPath) {
		OpenCVConfig.loadLibraries(openCVLibPath);
	}

	public ImageOutput getObjectImage(String sourceImage, String objectImage) throws Exception {

		String outputFile = System.getProperty("java.io.tmpdir") + System.currentTimeMillis() + ".png";

		if (algo_b(sourceImage, objectImage, outputFile)) {
			imageSearchOutput.setResultMessage("Image Matched with algo B");
			imageSearchOutput.setResultImage(outputFile);

		} else {
			if (algo_C(sourceImage, objectImage, outputFile)) {
				imageSearchOutput.setResultMessage("Image Matched with algo C");
				imageSearchOutput.setResultImage(outputFile);

			} else {
				imageSearchOutput.setResultMessage("Both Algo failed to match Image");

			}
		}
		return imageSearchOutput;
	}

	public void setCustomCutoff(long mimimumMarksforAlgoC, long mimimumMarksforAlgoB) {
		this.mimimumMarksforAlgoB = mimimumMarksforAlgoB;
		this.mimimumMarksforAlgoC = mimimumMarksforAlgoC;
	}

	private boolean algo_C(String sourceImage, String objectImage, String outputFile) throws Exception {
		/**
		 * Load the source image and template:
		 */
		Mat img = Imgcodecs.imread(sourceImage);
		Mat templ = Imgcodecs.imread(objectImage);
		/**
		 * it creates the result matrix that will store the matching results for each template location. Observe in detail the size of the result matrix (which matches all possible locations for it)
		 */
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		/*****************************************************
		 * Algo-C
		 *****************************************************************************************/

		/**
		 * Perform the template matching operation: And now result contains Image Coordinates.
		 */
		Imgproc.matchTemplate(img, templ, result, match_method);
		/**
		 * Normalization (Removing it . because you will never get values in point.)
		 */
		// Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new
		// Mat());
		/**
		 * We localize the minimum and maximum values in the result matrix R by using minMaxLoc.
		 */
		MinMaxLocResult mmr = Core.minMaxLoc(result);
		/**
		 * FIXME FAKEPASS mFactor is number of digit you want to set
		 */
		if (((int) (mmr.maxVal * mFactor)) >= mimimumMarksforAlgoC) {
			// Getting points of found image
			Point matchLoc = mmr.maxLoc;
			pointX = matchLoc.x + templ.cols();
			pointY = matchLoc.y + templ.rows();
			pointX1 = matchLoc.x;
			pointY1 = matchLoc.y;
			Point point = new Point(pointX, pointY);
			// Point point = new Point(matchLoc.x + templ.cols(), matchLoc.y +
			// templ.rows());
			imageSearchOutput.setX(point.x);
			imageSearchOutput.setY(point.y);
			Imgproc.rectangle(img, matchLoc, point, new Scalar(0, 255, 0));
			Imgproc.circle(img, new Point(pointX - 50, pointY - 50), 50, new Scalar(255, 0, 0));
			Imgcodecs.imwrite(outputFile, img);
			imageSearchOutput.setImageMatchingScore(mmr.maxVal * mFactor);
			return true;
		} else {
			imageSearchOutput.setImageMatchingScore(mmr.maxVal * mFactor);
			imageSearchOutput.setResultMessage("Image Failed to Match with Algo-C. Score Was " + (mmr.maxVal * mFactor) + "out of " + mimimumMarksforAlgoC);
			return false;
		}
	}

	private boolean algo_b(String sourceImage, String objectImage, String outputFile) throws Exception {
		/**
		 * Load the source image and template:
		 */
		Mat img = Imgcodecs.imread(sourceImage);
		Mat templ = Imgcodecs.imread(objectImage);
		/**
		 * it creates the result matrix that will store the matching results for each template location. Observe in detail the size of the result matrix (which matches all possible locations for it)
		 */
		int result_cols = img.cols() - templ.cols() + 1;
		int result_rows = img.rows() - templ.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

		/*****************************************************
		 * Algo-B
		 *****************************************************************************************/

		/**
		 * Image Border Detection : First get Gray Scale of Image and Then using canny darken the border.
		 */
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(templ, templ, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(templ, templ, 10, 100, 3, true);
		Imgproc.Canny(img, img, 10, 100, 3, true);
		/**
		 * Perform the template matching operation: And now result contains Image Coordinates.
		 */
		Imgproc.matchTemplate(img, templ, result, match_method);
		/**
		 * Normalization (Removing it . because you will never get values in point.)
		 */
		// Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new
		// Mat());
		/**
		 * We localize the minimum and maximum values in the result matrix R by using minMaxLoc.
		 */
		MinMaxLocResult mmr = Core.minMaxLoc(result);
		/**
		 * FIXME FAKEPASS mFactor is number of digit you want to set
		 */
		if (((int) (mmr.maxVal * mFactor)) >= mimimumMarksforAlgoB) {
			// Getting points of found image
			Point matchLoc = mmr.maxLoc;
			pointX = matchLoc.x + templ.cols();
			pointY = matchLoc.y + templ.rows();
			pointX1 = matchLoc.x;
			pointY1 = matchLoc.y;
			Point point = new Point(pointX, pointY);
			imageSearchOutput.setX(point.x);
			imageSearchOutput.setY(point.y);
			Imgproc.rectangle(img, matchLoc, point, new Scalar(255, 255, 0));
			Imgproc.circle(img, new Point(pointX - 50, pointY - 50), 50, new Scalar(255, 0, 0));
			Imgcodecs.imwrite(outputFile, img);
			imageSearchOutput.setImageMatchingScore(mmr.maxVal * mFactor);
			return true;
		} else {
			imageSearchOutput.setImageMatchingScore(mmr.maxVal * mFactor);
			Log.debug("Image Failed to Match with Algo-B Cutoff was " + mimimumMarksforAlgoB + ". Score Was " + (mmr.maxVal * mFactor));
			Log.debug("Trying To Match Image With Algo-C...");
			return false;
		}

	}

	public double getHeight() {
		java.awt.Point p = new java.awt.Point(((int) pointX), ((int) pointY));
		java.awt.Rectangle rect = new Rectangle(p);
		rect.add(new java.awt.Point((int) pointX1, (int) pointY1));
		return rect.getHeight();

	}

	public double getWidth() {
		java.awt.Point p = new java.awt.Point(((int) pointX), ((int) pointY));
		java.awt.Rectangle rect = new Rectangle(p);
		rect.add(new java.awt.Point((int) pointX1, (int) pointY1));
		return rect.getWidth();
	}

}

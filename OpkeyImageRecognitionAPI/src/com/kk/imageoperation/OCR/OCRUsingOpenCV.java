package com.kk.imageoperation.OCR;

import static org.opencv.imgproc.Imgproc.MORPH_RECT;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.kk.imageoperation.OpenCV.OpenCVConfig;

public class OCRUsingOpenCV {

	private static final int	CV_MOP_CLOSE					= 3;
	private static final int	CV_THRESH_OTSU					= 8;
	private static final int	CV_THRESH_BINARY				= 0;
	private static final int	CV_ADAPTIVE_THRESH_GAUSSIAN_C	= 1;
	private static final int	CV_ADAPTIVE_THRESH_MEAN_C		= 0;
	private static final int	CV_THRESH_BINARY_INV			= 1;

	public OCRUsingOpenCV(String OpenCVPath) {
		OpenCVConfig.loadLibraries(OpenCVPath);
	}

	public static boolean checkRatio(RotatedRect target) {
		double error = 0.3;
		double aspect = 6;
		int min = 15 * (int) aspect * 15;
		int max = 125 * (int) aspect * 125;
		// Get only patches that match to a respect ratio.
		double rmin = aspect - aspect * error;
		double rmax = aspect + aspect * error;
		double area = target.size.height * target.size.width;
		float r = (float) target.size.width / (float) target.size.height;
		if (r < 1)
			r = 1 / r;
		if ((area < min || area > max) || (r < rmin || r > rmax)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkDensity(Mat candidate) {
		float whitePx = 0;
		float allPx = 0;
		whitePx = Core.countNonZero(candidate);
		allPx = candidate.cols() * candidate.rows();
		if (0.62 <= whitePx / allPx)
			return true;
		else
			return false;
	}

	public static void main(String[] args) throws InterruptedException {
		new OCRUsingOpenCV(System.getProperty("user.dir") + "\\lib");
		Mat img = new Mat();
		Mat imgGray = new Mat();
		Mat imgGaussianBlur = new Mat();
		Mat imgSobel = new Mat();
		Mat imgThreshold = new Mat();
		Mat imgAdaptiveThreshold = new Mat();
		Mat imgAdaptiveThreshold_forCrop = new Mat();
		Mat imgMoprhological = new Mat();
		Mat imgContours = new Mat();
		Mat imgMinAreaRect = new Mat();
		Mat imgDetectedPlateCandidate = new Mat();
		Mat imgDetectedPlateTrue = new Mat();

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		img = Imgcodecs.imread("D:/test/destination.png");
		Imgcodecs.imwrite("D:/test/1_True_Image.png", img);

		Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
		Imgcodecs.imwrite("D:/test/2_imgGray.png", imgGray);

		Imgproc.GaussianBlur(imgGray, imgGaussianBlur, new Size(3, 3), 0);
		Imgcodecs.imwrite("D:/test/3_imgGaussianBlur.png", imgGray);

		Imgproc.Sobel(imgGaussianBlur, imgSobel, -1, 1, 0);
		Imgcodecs.imwrite("D:/test/4_imgSobel.png", imgSobel);

		Imgproc.threshold(imgSobel, imgThreshold, 0, 255, CV_THRESH_OTSU + CV_THRESH_BINARY);
		Imgcodecs.imwrite("D:/test/5_imgThreshold.png", imgThreshold);

		Imgproc.adaptiveThreshold(imgSobel, imgAdaptiveThreshold, 255, CV_ADAPTIVE_THRESH_GAUSSIAN_C, CV_THRESH_BINARY_INV, 75, 35);
		Imgcodecs.imwrite("D:/test/5_imgAdaptiveThreshold.png", imgAdaptiveThreshold);

		Imgproc.adaptiveThreshold(imgGaussianBlur, imgAdaptiveThreshold_forCrop, 255, CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 99, 4);
		Imgcodecs.imwrite("D:/test/5_imgAdaptiveThreshold_forCrop.png", imgAdaptiveThreshold_forCrop);

		Mat element = getStructuringElement(MORPH_RECT, new Size(17, 3));
		Imgproc.morphologyEx(imgAdaptiveThreshold, imgMoprhological, CV_MOP_CLOSE, element); // или imgThreshold
		Imgcodecs.imwrite("D:/test/6_imgMoprhologicald.png", imgMoprhological);

		imgContours = imgMoprhological.clone();
		Imgproc.findContours(imgContours, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(imgContours, contours, -1, new Scalar(255, 0, 0));
		Imgcodecs.imwrite("D:/test/7_imgContours.png", imgContours);

		imgMinAreaRect = img.clone();
		if (contours.size() > 0) {
			for (MatOfPoint matOfPoint : contours) {
				MatOfPoint2f points = new MatOfPoint2f(matOfPoint.toArray());

				RotatedRect box = Imgproc.minAreaRect(points);
				if (checkRatio(box)) {
					Imgproc.rectangle(imgMinAreaRect, box.boundingRect().tl(), box.boundingRect().br(), new Scalar(0, 0, 255));
					imgDetectedPlateCandidate = new Mat(imgAdaptiveThreshold_forCrop, box.boundingRect());
					if (checkDensity(imgDetectedPlateCandidate))
						imgDetectedPlateTrue = imgDetectedPlateCandidate.clone();
				} else
					Imgproc.rectangle(imgMinAreaRect, box.boundingRect().tl(), box.boundingRect().br(), new Scalar(0, 255, 0));
			}
		}

		 Imgcodecs.imwrite("D:/test/8_imgMinAreaRect.png", imgMinAreaRect);
		 Imgcodecs.imwrite("D:/test/9_imgDetectedPlateCandidate.png", imgDetectedPlateCandidate);
	     Imgcodecs.imwrite("D:/test/10_imgDetectedPlateTrue.png", imgDetectedPlateTrue);

	     OCRUsingTess4J t4j = new OCRUsingTess4J(System.getProperty("user.dir") + "\\lib\\Tess4j");
	    		 t4j.getTextFromImages("D:/test/5_imgAdaptiveThreshold.png");
		// Imgcodecs.imwrite("D:/test/destination2.png", imgDetectedPlateTrue);

	}

}

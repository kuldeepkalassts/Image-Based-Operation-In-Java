package com.kk.ImageIO.test;

import java.io.IOException;

import com.kk.imageoperation.Result.ImageOutput;
import com.kk.imageoperation.compare.CompareTwoImagesUsingNode;

class ImageComparasionTest {

	public static void main(String[] args) throws IOException {

		CompareTwoImagesUsingNode im = new CompareTwoImagesUsingNode("D:");
		ImageOutput op = im.comapreTwoImagesUsingVisualQA("D:\\sagar\\sc1.png", "D:\\sagar\\sc1.png", "im1.png", "", 1);
		
		System.out.println(op.getImageMatchingScore());
		System.out.println(op.getResultMessage());
		System.out.println(op.getImageMatchingScore());

		// im.Custom_visualComparionForMobile(image1, image2, outputImageName, outputPath, minimumPercentage)
		// im.Custom_visualComparionForMobile("D:\\sagar\\sc1.png", "D:\\sagar\\sc1.png", "im1.png" ,"D:\\", 0);
		// read a jpeg from a inputFile
		// ImageDetecter.convertToPNG(new File("D:\\xx.png"));

	}



}

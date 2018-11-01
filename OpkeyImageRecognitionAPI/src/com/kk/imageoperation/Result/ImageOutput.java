package com.kk.imageoperation.Result;

public class ImageOutput {

	private double	x;
	private double	y;
	private String	resultMessage;
	private String	resultImage;
	private double	imageMatchingScore;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public String getResultImage() {
		return resultImage;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = this.resultMessage + "\n" + resultMessage;
	}

	public void setResultImage(String resultImage) {
		this.resultImage = resultImage;
	}

	public void setImageMatchingScore(double imageMatchingScore) {
		this.imageMatchingScore = imageMatchingScore;
	}

	public double getImageMatchingScore() {
		return imageMatchingScore;
	}
}

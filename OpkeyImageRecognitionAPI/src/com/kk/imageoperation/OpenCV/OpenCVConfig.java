package com.kk.imageoperation.OpenCV;

import org.opencv.core.Core;

import com.kk.imageoperation.logs.Log;

public class OpenCVConfig {
	public static void loadLibraries(String opencvpath) {
		try {
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
				if (bitness == 32) {
					opencvpath = opencvpath + "\\opencv\\x86\\";
				} else if (bitness == 64) {
					opencvpath = opencvpath + "\\opencv\\x64\\";
				} else {
					opencvpath = opencvpath + "\\opencv\\x86\\";
				}
			} else if (osName.equals("Mac OS X")) {
				opencvpath = opencvpath + "Your path to .dylib";
			}
			Log.debug("OpenCV path : " + opencvpath);
			System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		} catch (Exception e) {
			throw new RuntimeException("Failed to load opencv native library", e);
		}
	}

}

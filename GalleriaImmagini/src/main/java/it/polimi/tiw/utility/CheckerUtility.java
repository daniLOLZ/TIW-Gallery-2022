package it.polimi.tiw.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckerUtility {

	/**
	 * Checks whether this string is not null, empty or blank
	 * @param value the string to check
	 * @return true if it's well formatted
	 */
	public static boolean checkAvailability(String value) {
		return (value != null && !value.isBlank() && !value.isEmpty());
	}

	public static boolean checkValidImage(String imageString) {
		ArrayList<String> validFormats = new ArrayList<String>() {
			{
				add("jpg");
				add("png");
				add("webp");
			}
		};
				
		return validFormats.contains(getImageExtension(imageString));
	}

	public static String getImageExtension(String imageString){
		List<String> splitString = Arrays.asList(imageString.split("\\."));
		if(splitString.size() == 0) return "";
		String extension = splitString.get(splitString.size()-1); //Last token is the extension
		return extension;
	}

	public static boolean checkValidCss(String cssString) {
		return getImageExtension(cssString).equals("css");
	}
	
	/**
	 * Tests to see if the value provided represents a positive integer
	 * @return true if the value is a positive integer 
	 */
	public static boolean isPositiveInt(String value) {
		int temp;
		try {
			temp = Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		
		return (temp > 0);
	}
	
	/**
	 * Tests to see if the value provided represents an integer
	 * @return true if the value is an integer 
	 */
	public static boolean isInt(String value) {
		int temp;
		try {
			temp = Integer.parseInt(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}

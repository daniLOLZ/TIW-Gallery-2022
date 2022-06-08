package it.polimi.tiw.utility;

public class CheckerUtility {

	/**
	 * Checks whether this string is not null, empty or blank
	 * @param value the string to check
	 * @return true if it's well formatted
	 */
	public static boolean checkAvailability(String value) {
		return (value != null && !value.isBlank() && !value.isEmpty());
	}

}

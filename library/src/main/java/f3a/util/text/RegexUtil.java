package f3a.util.text;

import java.util.regex.Pattern;

@SuppressWarnings({ "unused" })
public class RegexUtil {
	
	public static boolean isOnlyAlphabetAndNumber(String target) {
        if (target == null) {
            return false;
        }
		return Pattern.matches("[\\dA-Za-z]*", target);
	}
	
	public static boolean isEmail(String target) {
        if (target == null) {
            return false;
        }
		return Pattern.matches("^[a-z0-9]([-_.]?[a-z0-9]+)*@([-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2})?$", target);
	}
    
    public static boolean isCellphone(String target) {
        if (target == null) {
            return false;
        }
        return Pattern.matches("^1[0-9]{10}$", target);
    }
    
    public static boolean isPhoneno(String target) {
        if (target == null) {
            return false;
        }
        return Pattern.matches("^(\\d{3}-\\d{8})|(\\d{11})|(\\d{8})$", target);
    }
	
	public static boolean isIdentity(String target) {
		if (target == null) {
			return false;
		}
		return Pattern.matches("^[1-6][0-7]\\d{4}(19|20)\\d{2}((0[1-9])|(1[0-2]))((0[1-9])|([1-2]\\d)|(3[0-1]))\\d{3}(\\d|x|X)$", target);
	}
	
	public static boolean isIp(String target) {
        if (target == null) {
            return false;
        }
		return Pattern.matches("^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}$", target);
	}
    
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255)
            return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }
}

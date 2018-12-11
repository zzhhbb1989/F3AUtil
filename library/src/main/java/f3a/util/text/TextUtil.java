package f3a.util.text;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * String工具类
 * @author Bob Ackles
 *
 */
@SuppressWarnings({ "unused" })
public class TextUtil {

	/**
	 * 半角转换为全角
	 */
	public static String toDBC(String input) {
		if (input == null) {
			return null;
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if ('\u3000' == c[i]) {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}
	
	public static float getTextWidth(Paint paint, String text) {
		float[] widths = new float[text.length()];
		paint.getTextWidths(text, widths);
		float sum = 0;
		for (float width : widths) {
			sum += width;
		}
		return sum;
	}
	
	public static float getTextHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return (float) Math.ceil(fm.descent - fm.ascent);
	}
	
	public static List<int[]> indexOfASetOfSignsInString(String src, String[] signs) {
		List<int[]> positions = new ArrayList<>();
		int index = 0;
		do {
			index = src.indexOf(signs[0], index);
			if (index == -1) {
				break;
			}
			int[] currPositions = new int[signs.length];
			for (int i = 0; i < signs.length; i++) {
				currPositions[i] = src.indexOf(signs[i], index);
			}
			positions.add(currPositions);
			index = currPositions[currPositions.length - 1] + signs[signs.length - 1].length();
		} while (index < src.length());
		return positions;
	}
	
	public static boolean isEmpty(Object src) {
		return src == null || "".equals(src);
	}
	
	public static int getIntByString(Object src, int defaultValue) {
		return isEmpty(src) ? defaultValue : Integer.parseInt((String)src);
	}
	
	public static String getStringByObject(Object src) {
		return isEmpty(src) ? null : (String)src;
	}
	
	public static String wrapNone(String text) {
		return TextUtils.isEmpty(text) ? "暂无" : text;
	}
	
	public static boolean isNone(String text) {
		return "暂无".equals(text);
	}
    
    public static enum CharType {
        en,
        zh,
        other
    }
    
    public static long getWordCount(String src) {
        char ch;
        CharType type;
        // 单词的临时变量
        boolean wordBegin = false;
        // 汉字个数
        int numZh = 0;
        // 单词个数
        int numEn = 0;
        // 其他符号
        int numOther = 0;
        for (int i = 0; i < src.length(); i++) {
            ch = src.charAt(i);
            type = getCharType(ch);
            if (wordBegin) {
                if (type != CharType.en && ch != '-') {
                    numEn ++;
                    wordBegin = false;
                    if (type == CharType.zh) {
                        numZh ++;
                    }
                }
            } else {
                switch (type) {
                    case en:
                        wordBegin = true;
                        break;
                    case zh:
                        numZh ++;
                        break;
                    default:
                        numOther ++;
                        break;
                }
            }
        }
        if (wordBegin) {
            numEn++;
        }
        return numZh + numEn;
    }
    
    public static CharType getCharType(char ch) {
        if (ch >= 19968 && ch <= 64041) {
            return CharType.zh;
        }
        if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {
            return CharType.en;
        }
        
        return CharType.other;
    }
}

package f3a.util.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class SpannableStringUtil {
	
	////////////////////////////// Color //////////////////////////////

	public static void foregroundColor(SpannableString ss, String part, int color) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		if (start >= 0) {
			foregroundColor(ss, start, start + part.length(), color);
		}
	}

	public static void foregroundColor(SpannableString ss, int start, int end, int color) {
		ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
	}

	public static void backgroundColor(SpannableString ss, String part, int color) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new BackgroundColorSpan(color), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	////////////////////////////// Text Size //////////////////////////////

	public static void textSize(SpannableString ss, String part, int sizeInPx) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new AbsoluteSizeSpan(sizeInPx), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public static void textSize(SpannableString ss, String part, int size, boolean dip) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new AbsoluteSizeSpan(size, dip), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	////////////////////////////// Text Style //////////////////////////////

	public static void textStyle(TextView textView, SpannableString ss, String part, int typefaceStyle) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new TextAppearanceSpan(null, typefaceStyle,
				(int)textView.getTextSize(), textView.getTextColors(),
				textView.getLinkTextColors()), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	////////////////////////////// Subscript Superscript //////////////////////////////

	public static void subscript(SpannableString ss, String part) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new SubscriptSpan(), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public static void superscript(SpannableString ss, String part) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new SuperscriptSpan(), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	////////////////////////////// Line //////////////////////////////
	
	public static void underline(SpannableString ss, String part) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new UnderlineSpan(), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public static void strikeThrough(SpannableString ss, String part) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new StrikethroughSpan(), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	////////////////////////////// Image //////////////////////////////
	
	public static void image(SpannableString ss, String part, Drawable d) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		image(ss, part, d, ImageSpan.ALIGN_BASELINE);
	}
	
	public static void image(SpannableString ss, String part, Drawable d, int align) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new MyImageSpan(d, align), start, start + part.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	}
	
	public static class MyImageSpan extends ImageSpan {
		
		public static final int ALIGN_CENTER = 2;
		
		public MyImageSpan(Drawable d, int verticalAlignment) {
			super(d, verticalAlignment);
		}
		
		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
			Drawable b = getDrawable();
			canvas.save();
			
			Paint.FontMetrics fm = paint.getFontMetrics();
			int transY = bottom - b.getBounds().bottom;
			if (mVerticalAlignment == ALIGN_BASELINE) {
				transY -= paint.getFontMetricsInt().descent;
			}
			else if (mVerticalAlignment == ALIGN_CENTER) {
				transY = (bottom + top)/2 - b.getBounds().height()/2;
			}
			
			canvas.translate(x, transY);
			b.draw(canvas);
			canvas.restore();
		}
	}
	
	////////////////////////////// Click //////////////////////////////
	
	public static void clickable(SpannableString ss, String part, ClickableSpan click) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		clickable(ss, start, start + part.length(), click);
	}
	
	public static void clickable(SpannableString ss, int start, int end, ClickableSpan click) {
		String text = ss.toString();
		ss.setSpan(click, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public static void clickableFinish(TextView textView) {
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	////////////////////////////// URL //////////////////////////////
	
	public static void url(SpannableString ss, String part, String url) {
		if (TextUtils.isEmpty(part)) {
			return;
		}
		String text = ss.toString();
		int start = text.indexOf(part);
		ss.setSpan(new URLSpan(url), start, start + part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	public static void urlFinish(TextView textView) {
		clickableFinish(textView);
	}
	
	////////////////////////////// LinkMovementMethod //////////////////////////////
	
	class MyLinkMovementMethod extends LinkMovementMethod {

	    @Override
	    public boolean onTouchEvent(TextView widget, Spannable buffer,
	                                MotionEvent event) {
	        int action = event.getAction();

	        if (action == MotionEvent.ACTION_UP ||
	            action == MotionEvent.ACTION_DOWN) {
	            int x = (int) event.getX();
	            int y = (int) event.getY();

	            x -= widget.getTotalPaddingLeft();
	            y -= widget.getTotalPaddingTop();

	            x += widget.getScrollX();
	            y += widget.getScrollY();

	            Layout layout = widget.getLayout();
	            int line = layout.getLineForVertical(y);
	            int off = layout.getOffsetForHorizontal(line, x);

	            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
	            
	            ClickableSpan[] all = buffer.getSpans(0, buffer.length(), ClickableSpan.class);
	            if (link.length != 0) {
	                if (action == MotionEvent.ACTION_UP) {
	                    link[0].onClick(widget);
	                } else if (action == MotionEvent.ACTION_DOWN) {
	                    Selection.setSelection(buffer,
	                                           buffer.getSpanStart(link[0]),
	                                           buffer.getSpanEnd(link[0]));
	                } else if (action == MotionEvent.ACTION_MOVE) {
						
					}

	                return true;
	            } else {
	                Selection.removeSelection(buffer);
	            }
	        }

	        return super.onTouchEvent(widget, buffer, event);
	    }
	}
}

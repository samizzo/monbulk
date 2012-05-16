package monbulk.shared.widgets;

import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.regexp.shared.RegExp;

public class TextBoxEx extends TextBox implements KeyPressHandler, KeyUpHandler, FocusHandler, BlurHandler
{
	private boolean m_onlyNumbers = false;
	private boolean m_allowNegative = false;
	private boolean m_allowDecimalPoint = false;
	private String m_hintText = "";
	private String m_hintTextStyle = "";
	private boolean m_hasText = false;
	private String m_defaultValue = "";
	private RegExp m_regExp = null;
	
	public TextBoxEx()
	{
		addKeyPressHandler(this);
		addKeyUpHandler(this);
		addFocusHandler(this);
		addBlurHandler(this);
	}
	
	/**
	 * Sets the default value.  The default value is shown when there is no
	 * hint text and the text box loses focus.
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue)
	{
		m_defaultValue = defaultValue;
	}
	
	/**
	 * If true, only numbers can be entered into this text box.
	 * @param onlyNumbers
	 */
	public void setOnlyNumbers(boolean onlyNumbers)
	{
		m_onlyNumbers = onlyNumbers;
	}
	
	/**
	 * If true and this text box is set to only allow numbers, then the
	 * negative symbol will be allowed.
	 * @param allowNegative
	 */
	public void setAllowNegative(boolean allowNegative)
	{
		m_allowNegative = allowNegative;
	}
	
	/**
	 * If true and this text box is set to only allow numbers, then the
	 * decimal point will be allowed.
	 * @param allowDecimalPoint
	 */
	public void setAllowDecimalPoint(boolean allowDecimalPoint)
	{
		m_allowDecimalPoint = allowDecimalPoint;
	}
	
	/**
	 * Sets the hint text, to be used when there is nothing in the text box
	 * and it doesn't have focus.
	 * @param hintText
	 */
	public void setHintText(String hintText)
	{
		m_hintText = hintText;
	}

	/**
	 * Sets the style to be used to render the hint text.
	 * @param style
	 */
	public void setHintTextStyle(String style)
	{
		m_hintTextStyle = style;
	}
	
	/**
	 * Returns the text from this text box, or an empty string if there
	 * is no text in the text box.
	 * @return 
	 */
	public String getText()
	{
		if (m_hintText.length() == 0 || m_hasText)
		{
			// No hint text or there is hint text but there is also real text.
			return super.getText();
		}
		
		return "";
	}

	/**
	 * Sets a regex to be used as validation.  Only characters
	 * that pass the regex will be allowed.  This is only used
	 * if the text box is not set to only allow numbers.
	 * @param regex
	 */
	public void setValidCharRegex(String regex)
	{
		if (regex != null && regex.length() > 0)
		{
			m_regExp = RegExp.compile(regex);
		}
		else
		{
			m_regExp = null;
		}
	}
	
	/**
	 * Sets the text in this text box.  This will show the hint text if the
	 * specified text is an empty string.
	 * @param text
	 */
	public void setText(String text)
	{
		super.setText(text);
		setHasText(text);
		updateHintText(!m_hasText);
	}
	
	private void setHasText(String text)
	{
		// We have text if there is something in the text box and this isn't
		// a numbers-only textbox, or if it is there is more than just a - in
		// the text box.
		m_hasText = (text.length() > 0 && (!m_onlyNumbers || (!text.equals("-") && !text.equals("."))));
	}
	
	private boolean isDigit(int keyCode)
	{
		return keyCode >= '0' && keyCode <= '9';
	}

	// NOTE: We provide our own version here because we don't want
	// to call our getText.	
	private String getRealSelectedText()
	{
		int start = getCursorPos();
		if (start < 0)
		{
			return "";
		}

		int length = getSelectionLength();
		return super.getText().substring(start, start + length);
	}
	
	public void onKeyPress(KeyPressEvent event)
	{
		int charCode = event.getCharCode();
		int keyCode = 0;
		if (charCode == 0)
		{
			keyCode = event.getNativeEvent().getKeyCode();
		}

		if (m_onlyNumbers)
		{
			int selectionLength = getSelectionLength();
			String text = super.getText();
			boolean selectedIncludesMinus = selectionLength > 0 && getRealSelectedText().indexOf('-') >= 0;
			boolean textStartsWithMinus = text.length() > 0 && text.charAt(0) == '-';
			int cursorPos = getCursorPos();

			if (m_allowNegative && charCode == '-' &&
				!selectedIncludesMinus &&
				(cursorPos > 0 || (cursorPos == 0 && textStartsWithMinus)))
			{
				// Only allow a minus sign at the start of the text box, and
				// only if there isn't already one there.
				cancelKey();
			}
			
			boolean selectedIncludesDot = selectionLength > 0 && getRealSelectedText().indexOf('.') >= 0;
			boolean textIncludesDot = text.length() > 0 && text.indexOf('.') >= 0;

			if (m_allowDecimalPoint && charCode == '.' &&
				!selectedIncludesDot &&
				textIncludesDot) 
			{
				// Only allow one decimal point.
				cancelKey();
			}

			if (!isDigit(charCode) &&
				(!m_allowNegative || charCode != '-') &&
				(!m_allowDecimalPoint || charCode != '.') &&
				(keyCode != KeyCodes.KEY_TAB) &&
				(keyCode != KeyCodes.KEY_BACKSPACE) &&
				(keyCode != KeyCodes.KEY_DELETE) &&
				(keyCode != KeyCodes.KEY_ENTER) &&
				(keyCode != KeyCodes.KEY_HOME) &&
				(keyCode != KeyCodes.KEY_END) &&
				(keyCode != KeyCodes.KEY_LEFT) &&
				(keyCode != KeyCodes.KEY_UP) &&
				(keyCode != KeyCodes.KEY_RIGHT) &&
				(keyCode != KeyCodes.KEY_DOWN) &&
				(!event.isAltKeyDown() && !event.isControlKeyDown() && !event.isMetaKeyDown()))
			{
          		cancelKey();
        	}
		}
		else if (m_regExp != null && charCode != 0)
		{
			String str = (char)charCode + "";
			if (!m_regExp.test(str))
			{
				// New character doesn't match against regex so cancel.
				cancelKey();
			}
		}
	}
	
	public void onKeyUp(KeyUpEvent event)
	{
		setHasText(super.getText());
	}
	
	public void onBlur(BlurEvent event)
	{
		// Text box has lost focus.
		if (m_hasText && m_onlyNumbers && m_allowDecimalPoint)
		{
			// There is text and it should only have numbers,
			// so reformat the number in case it's a bit weird
			// e.g. 0. -> 0.0, .1 -> 0.1
			String text = super.getText();
			double value = Double.parseDouble(text);
			setText("" + value);
		}

		updateHintText(true);
	}
	
	public void onFocus(FocusEvent event)
	{
		updateHintText(false);
	}
	
	private void updateHintText(boolean visible)
	{
		if (m_hintText.length() > 0)
		{
			// If there is hint text and no real text, then set
			// the real text to the hint text or empty string,
			// and add or remove the style.
			if (!m_hasText)
			{
				super.setText(visible ? m_hintText : "");
			}

			if (m_hintTextStyle.length() > 0)
			{
				if (visible && !m_hasText)
				{
					addStyleName(m_hintTextStyle);
				}
				else
				{
					removeStyleName(m_hintTextStyle);
				}
			}
		}
		else
		{
			//  No hint text so set default value if there is one
			// and we are losing focus.
			if (visible && !m_hasText && m_defaultValue.length() > 0)
			{
				super.setText(m_defaultValue);
			}
		}
	}
}

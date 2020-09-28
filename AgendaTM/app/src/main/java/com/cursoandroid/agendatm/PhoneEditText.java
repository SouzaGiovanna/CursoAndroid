package com.cursoandroid.agendatm;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;

public class PhoneEditText extends androidx.appcompat.widget.AppCompatEditText {
    private boolean isUpdating;

    /*
     * Maps the cursor position from phone number to masked number... 1234567890
     * => (12) 3456-7890
     */
    private int positioning[] = { 1, 2, 3, 6, 7, 8, 9, 11, 12, 13, 14, 15 };

    public PhoneEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();

    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }

    public PhoneEditText(Context context) {
        super(context);
        initialize();

    }

    public String getCleanText() {
        String text = PhoneEditText.this.getText().toString();

        text.replaceAll("[^0-9]*", "");
        return text;

    }

    private void initialize() {

        final int maxNumberLength = 11;
        this.setKeyListener(keylistenerNumber);

        this.setText("(  )     -    ");
        this.setSelection(1);

        this.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String current = s.toString();

                /*
                 * Ok, here is the trick... calling setText below will recurse
                 * to this function, so we set a flag that we are actually
                 * updating the text, so we don't need to reprocess it...
                 */
                if (isUpdating) {
                    isUpdating = false;
                    return;

                }

                /* Strip any non numeric digit from the String... */
                String number = current.replaceAll("[^0-9]*", "");
                if (number.length() > 11)
                    number = number.substring(0, 11);

                int length = number.length();

                /* Pad the number to 10 characters... */
                String paddedNumber = padNumber(number, maxNumberLength);

                /* Split phone number into parts... */
                String ddd = paddedNumber.substring(0, 2);
                String part1 = paddedNumber.substring(2, 7);
                String part2 = paddedNumber.substring(7, 11).trim();

                /* build the masked phone number... */
                String phone = "(" + ddd + ") " + part1 + "-" + part2;

                /*
                 * Set the update flag, so the recurring call to
                 * afterTextChanged won't do nothing...
                 */
                isUpdating = true;
                PhoneEditText.this.setText(phone);

                PhoneEditText.this.setSelection(positioning[length]);

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
    }

    protected String padNumber(String number, int maxLength) {
        String padded = new String(number);
        for (int i = 0; i < maxLength - number.length(); i++)
            padded += " ";
        return padded;

    }

    private final KeylistenerNumber keylistenerNumber = new KeylistenerNumber();

    private class KeylistenerNumber extends NumberKeyListener {

        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

        }

        @Override
        protected char[] getAcceptedChars() {
            return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9' };

        }
    }
}
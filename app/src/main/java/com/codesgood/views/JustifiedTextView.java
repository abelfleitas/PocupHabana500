package com.codesgood.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class JustifiedTextView extends TextView {

    String justifiedText = "";
    Paint mPaint;
    String mThinSpace = " ";
    private float mThinSpaceWidth;
    private int mViewWidth;
    private float mWhiteSpaceWidth;
    float sentenceWidth = 0.0f;
    ArrayList<String> temporalLine = new ArrayList();
    int whiteSpacesNeeded = 0;
    int wordsInThisSentence = 0;
    int randomPosition;


    public JustifiedTextView(Context context) {
        super(context);
    }

    public JustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LayoutParams params = getLayoutParams();
        String[] words = getText().toString().split(" ");
        this.mPaint = getPaint();
        this.mViewWidth = getMeasuredWidth() - (getPaddingLeft() + getPaddingRight());
        if (params.width != -2 && this.mViewWidth > 0 && words.length > 0 && this.justifiedText.isEmpty()) {
            this.mThinSpaceWidth = this.mPaint.measureText(this.mThinSpace);
            this.mWhiteSpaceWidth = this.mPaint.measureText(" ");
            for (String word : words) {
                boolean containsNewLine = word.contains("\n") || word.contains("\r");
                if (containsNewLine) {
                    for (String splitWord : word.split("(?<=\\n)")) {
                        processWord(splitWord, splitWord.contains("\n"));
                    }
                } else {
                    processWord(word, false);
                }
            }
            this.justifiedText += joinWords(this.temporalLine);
        }
        if (!this.justifiedText.isEmpty()) {
            setText(this.justifiedText);
        }
    }

    private void processWord(String word, boolean containsNewLine) {
        if (this.sentenceWidth + this.mPaint.measureText(word) < ((float) this.mViewWidth)) {
            this.temporalLine.add(word);
            this.wordsInThisSentence++;
            this.temporalLine.add(containsNewLine ? "" : " ");
            this.sentenceWidth += this.mPaint.measureText(word) + this.mWhiteSpaceWidth;
            if (containsNewLine) {
                this.justifiedText += joinWords(this.temporalLine);
                resetLineValues();
                return;
            }
            return;
        }
        while (this.sentenceWidth < ((float) this.mViewWidth)) {
            this.sentenceWidth += this.mThinSpaceWidth;
            if (this.sentenceWidth < ((float) this.mViewWidth)) {
                this.whiteSpacesNeeded++;
            }
        }
        if (this.wordsInThisSentence > 1) {
            insertWhiteSpaces(this.whiteSpacesNeeded, this.wordsInThisSentence, this.temporalLine);
        }
        this.justifiedText += joinWords(this.temporalLine);
        resetLineValues();
        if (containsNewLine) {
            this.justifiedText += word;
            this.wordsInThisSentence = 0;
            return;
        }
        this.temporalLine.add(word);
        this.wordsInThisSentence = 1;
        this.temporalLine.add(" ");
        this.sentenceWidth += this.mPaint.measureText(word) + this.mWhiteSpaceWidth;
    }

    private void resetLineValues() {
        this.temporalLine.clear();
        this.sentenceWidth = 0.0f;
        this.whiteSpacesNeeded = 0;
        this.wordsInThisSentence = 0;
    }

    private String joinWords(ArrayList<String> words) {
        String sentence = "";
        Iterator it = words.iterator();
        while (it.hasNext()) {
            sentence = sentence + ((String) it.next());
        }
        return sentence;
    }

    private void insertWhiteSpaces(int whiteSpacesNeeded, int wordsInThisSentence, ArrayList<String> sentence) {
        if (whiteSpacesNeeded != 0) {
            int i;
            if (whiteSpacesNeeded == wordsInThisSentence) {
                for (i = 1; i < sentence.size(); i += 2) {
                    sentence.set(i, ((String) sentence.get(i)) + this.mThinSpace);
                }
            } else if (whiteSpacesNeeded < wordsInThisSentence) {
                for (i = 0; i < whiteSpacesNeeded; i++) {
                    randomPosition = getRandomEvenNumber(sentence.size() - 1);
                    sentence.set(randomPosition, ((String) sentence.get(randomPosition)) + this.mThinSpace);
                }
            } else if (whiteSpacesNeeded > wordsInThisSentence) {
                while (whiteSpacesNeeded > wordsInThisSentence) {
                    for (i = 1; i < sentence.size() - 1; i += 2) {
                        sentence.set(i, ((String) sentence.get(i)) + this.mThinSpace);
                    }
                    whiteSpacesNeeded -= wordsInThisSentence - 1;
                }
                if (whiteSpacesNeeded == 0) {
                    return;
                }
                if (whiteSpacesNeeded == wordsInThisSentence) {
                    for (i = 1; i < sentence.size(); i += 2) {
                        sentence.set(i, ((String) sentence.get(i)) + this.mThinSpace);
                    }
                } else if (whiteSpacesNeeded < wordsInThisSentence) {
                    for (i = 0; i < whiteSpacesNeeded; i++) {
                        randomPosition = getRandomEvenNumber(sentence.size() - 1);
                        sentence.set(randomPosition, ((String) sentence.get(randomPosition)) + this.mThinSpace);
                    }
                }
            }
        }
    }

    private int getRandomEvenNumber(int max) {
        return new Random().nextInt(max) & -2;
    }
}

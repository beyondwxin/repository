package com.newgen.UI;

import android.text.Editable;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;


// MongolEditText Input Connection
// Allows the MongolEditText to receive input from system keyboards

class MetInputConnection extends BaseInputConnection {

    private MongolEditText mMongolEditText;

    MetInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
        if (!(targetView instanceof MongolEditText)) {
            throw new RuntimeException("MetInputConnection is only set up to work with a MongolEditText, " +
                    "not with " + targetView);
        }
        mMongolEditText = (MongolEditText) targetView;
    }

    @Override
    public Editable getEditable() {
        return mMongolEditText.getText();
    }

    @Override
    public boolean beginBatchEdit() {
        if (mMongolEditText == null) return false;
        return mMongolEditText.beginBatchEdit();
    }

    @Override
    public boolean endBatchEdit() {
        if (mMongolEditText == null) return false;
        return mMongolEditText.endBatchEdit();
    }

//    @Override
//    public void closeConnection() {
//        // trying to apply docs here. Is this right?
//        // see https://developer.android.com/reference/android/view/inputmethod/InputConnection.html#closeConnection()
//        super.closeConnection();
//        mMongolEditText.ensureEndedBatchEdit();
//    }

    // TODO commitCompletion: after adding completion choices
    // https://developer.android.com/reference/android/view/inputmethod/InputConnection.html#commitCompletion(android.view.inputmethod.CompletionInfo)
    // TODO commitCorrection: add spell correction at some future date
    // TODO? performContextMenuAction
    // TODO? performEditorAction

}

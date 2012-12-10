package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.thoughtworks.orteroid.Callback;
import com.thoughtworks.orteroid.R;
import com.thoughtworks.orteroid.constants.Constants;
import com.thoughtworks.orteroid.repositories.BoardRepository;
import org.json.JSONException;

public class AddIdeaActivity extends Activity {

    private int sectionId;
    private String idea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String sectionId = intent.getStringExtra(Constants.SECTION_ID);
        if (sectionId == null) {
            this.sectionId = 0;
        } else {
            this.sectionId = Integer.parseInt(sectionId);
        }
        setContentView(R.layout.add_idea);
    }

    public void addAnIdea(View view) {
        final EditText ideaText = (EditText) findViewById(R.id.ideaMessage);
        idea = ideaText.getText().toString();
        postIdea();
        ideaText.setText("");
    }

    private void postIdea() {
        Callback callback = addIdeaCallback();
        BoardRepository.getInstance().addIdea(idea, sectionId, callback);
    }

    private Callback addIdeaCallback() {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean result) throws JSONException {
                if(result) generateSuccessToast();
                else generateFailureNotification();
            }
        };
    }

    private void generateFailureNotification() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The following idea failed :\n "+idea);
        builder.setNegativeButton("Try Resending", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                postIdea();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generateSuccessToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Idea posted successfully");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}

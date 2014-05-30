package com.ideaboardz.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ideaboardz.android.Callback;
import com.ideaboardz.android.R;
import com.ideaboardz.android.constants.Constants;
import com.ideaboardz.android.models.Point;
import com.ideaboardz.android.repositories.BoardRepository;
import com.ideaboardz.android.utilities.ColorSticky;
import com.ideaboardz.android.utilities.Font;

public class EditIdeaActivity extends Activity {

    private Point point;
    private String message;
    private String oldmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_idea);
        Intent intent = getIntent();
        this.point = intent.getParcelableExtra(Constants.SELECTED_POINT);
        setupText();
        setBackgroundLayout();
        EditText editText = (EditText) findViewById(R.id.editIdea);
        oldmessage = editText.getText().toString();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void editIdea(View view) {
        EditText editText = (EditText) findViewById(R.id.editIdea);
        message = editText.getText().toString();
        sendEditedIdea();
    }

    public void deleteCurrentIdea(View view) {
        generateConfirmationMessage();
    }

    private void generateConfirmationMessage() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Are you sure you want to delete this Idea?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                deleteIdea();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteIdea() {
        Callback<Boolean> callback = deleteIdeaCallback();
        BoardRepository.getInstance().deletePoint(point, callback);
    }

    private void sendEditedIdea() {
        Callback<Boolean> callback = editIdeaCallback();
        message = convertLineBreakToSpace(message);
        oldmessage = convertLineBreakToSpace(oldmessage);
        BoardRepository.getInstance().editIdea(message, point.id(), oldmessage, callback);
    }

    private String convertLineBreakToSpace(String rawIdea) {
        return rawIdea.replaceAll("\n", " ");
    }

    private Callback<Boolean> editIdeaCallback() {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean result) {
                if (result != null) {
                    generateSuccessToast();
                } else generateFailureNotification();
            }
        };
    }

    private Callback<Boolean> deleteIdeaCallback() {
        return new Callback<Boolean>() {
            @Override
            public void execute(Boolean response) {
                if (response != null) {
                    migrateToViewBoardActivity();
                } else {
                    connectionIssueNotification();
                }
            }
        };
    }

    private void generateFailureNotification() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("The following idea failed :\n " + oldmessage)
                        .setNegativeButton("Try Resending", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendEditedIdea();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generateSuccessToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Idea posted successfully");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        migrateToViewBoardActivity();
    }

    private void migrateToViewBoardActivity() {
        finish();
    }


    private void setupText() {
        EditText editText = (EditText) findViewById(R.id.editIdea);
        editText.setText(point.message());
        editText.setTypeface(Font.setFontForIdea(this));
    }

    private void setBackgroundLayout() {
        EditText editText = (EditText) findViewById(R.id.editIdea);
        editText.setBackgroundResource(R.drawable.sticky);
        GradientDrawable drawable = (GradientDrawable) editText.getBackground();
        drawable.setColor(Color.parseColor(ColorSticky.getColorCode(point.sectionId())));
        editText.invalidate();
    }

    private void connectionIssueNotification() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this)
                        .setTitle("Failed to connect to the board")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(EditIdeaActivity.this, MainActivity.class);
                                dialog.dismiss();
                                startActivity(intent);
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

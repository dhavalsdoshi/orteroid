package com.thoughtworks.orteroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.thoughtworks.orteroid.R;

public class AddIdeaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.add_idea);
    }
    public void addAnIdea(){
    }

}

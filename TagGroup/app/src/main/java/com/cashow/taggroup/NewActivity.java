package com.cashow.taggroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewActivity extends AppCompatActivity implements OnTagClickListener {
    private NewTagGroup tagGroup1;
    private NewTagGroup tagGroup2;
    private NewTagGroup tagGroup3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        tagGroup1 = (NewTagGroup) findViewById(R.id.tag_group_1);
        tagGroup2 = (NewTagGroup) findViewById(R.id.tag_group_2);
        tagGroup3 = (NewTagGroup) findViewById(R.id.tag_group_3);

        setTagList1();
        setTagList2();
        setTagList3();
    }

    private void setTagList1() {
        List<MyTag> tagList = new ArrayList<>();
        tagList.add(new MyTag("tag1"));
        tagList.add(new MyTag("tag111"));
        tagList.add(new MyTag("tag111111"));
        tagList.add(new MyTag("tag111111111"));
        tagList.add(new MyTag("tag1111"));
        tagList.add(new MyTag("tag11"));
        tagList.add(new MyTag("tag111111111111"));
        tagList.add(new MyTag("tag111111111"));
        tagList.add(new MyTag("tag1111111"));
        tagList.add(new MyTag("tag1111"));
        tagList.add(new MyTag("tag11"));
        tagList.add(new MyTag("tag1111"));
        tagList.add(new MyTag("tag11"));
        tagList.add(new MyTag("tag1"));
        tagGroup1.setTagList(tagList);

        tagGroup1.setOnTagClickListener(this);
    }

    private void setTagList2() {
        List<MyTag> tagList = new ArrayList<>();
        tagList.add(new MyTag("tag2"));
        tagList.add(new MyTag("tag222"));
        tagList.add(new MyTag("tag222222"));
        tagList.add(new MyTag("tag222222222"));
        tagList.add(new MyTag("tag2222"));
        tagList.add(new MyTag("tag22"));
        tagList.add(new MyTag("tag222222222222"));
        tagList.add(new MyTag("tag222222222"));
        tagList.add(new MyTag("tag2222222"));
        tagList.add(new MyTag("tag2222"));
        tagList.add(new MyTag("tag22"));
        tagList.add(new MyTag("tag2222"));
        tagList.add(new MyTag("tag22"));
        tagList.add(new MyTag("tag2"));
        tagGroup2.setTagList(tagList);

        tagGroup2.setOnTagClickListener(this);

        tagGroup2.setMaxLineNumber(1);
    }

    private void setTagList3() {
        List<MyTag> tagList = new ArrayList<>();
        tagList.add(new MyTag("tag3"));
        tagList.add(new MyTag("tag333"));
        tagList.add(new MyTag("tag333333"));
        tagList.add(new MyTag("tag333333333"));
        tagList.add(new MyTag("tag3333"));
        tagList.add(new MyTag("tag33"));
        tagList.add(new MyTag("tag333333333333"));
        tagList.add(new MyTag("tag333333333"));
        tagList.add(new MyTag("tag3333333"));
        tagList.add(new MyTag("tag3333"));
        tagList.add(new MyTag("tag33"));
        tagList.add(new MyTag("tag3333"));
        tagList.add(new MyTag("tag33"));
        tagList.add(new MyTag("tag3"));
        tagGroup3.setTagList(tagList);

        tagGroup3.setOnTagClickListener(this);

        tagGroup3.setMaxLineNumber(2);
    }

    @Override
    public void onTagClick(MyTag tag) {
        Toast.makeText(getApplicationContext(), "tag click : " + tag.name, Toast.LENGTH_SHORT).show();
    }
}

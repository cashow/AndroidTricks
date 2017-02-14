package com.cashow.deletedeteck;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    /*
     * android官方没有提供监听删除事件的接口，所以需要自己实现。
     * 一个最简单的实现方法就是监听edittext的文本变化（addTextChangedListener），
     * 如果edittext内容变少了，就说明进行了删除操作。
     * 但这个方法有个缺陷，在edittext没有内容的时候，监听不到删除事件。
     * 解决办法就是，在edittext的内容最前面加一个看不见且不占空间的字符，
     * 如果检测到这个字符被删掉，就说明进行了删除操作，这个时候再在edittext前面手动加上那个特殊字符。
     * 这个特殊字符就是"零宽空格"，wiki链接：https://en.wikipedia.org/wiki/Zero-width_space
     */

    private EditText edittext;
    private TextView textview;

    // 记录edittext在状态改变之前的字数
    // 假如edittext在afterTextChanged之后，字数比preLength少，说明进行了删除操作
    private int preLength;

    // 删除按钮的点击次数
    private int deleteCount;

    // 零宽空格，不会引起换行
    // 需要注意的是，"\u200b"也是零宽空格，但是这个字符会导致换行
    // 也就是说，如果"\u200b"字符后面的单词超出了该行的剩余长度，会导致这个单词换行，在下一行显示
    // 使用"\uFEFF"就不会出现这种情况
    private static final String ZERO_WIDTH_SPACE = "\uFEFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        preLength = 0;
        deleteCount = 0;
    }

    private void initView() {
        edittext = (EditText) findViewById(R.id.edittext);
        textview = (TextView) findViewById(R.id.textview);

        // edittext初始化时，加上 ZERO_WIDTH_SPACE，这样的话在没有输入任何内容的情况下，
        // 在软键盘按下删除键，还是能补获到删除事件
        edittext.setText(ZERO_WIDTH_SPACE);
        preLength += 1;

        // 系统不能监听删除事件，所以在edittext前面加了没有宽度的符号 ZERO_WIDTH_SPACE
        // 如果edittext删除了ZERO_WIDTH_SPACE，那说明这个edittext已经没有内容，
        // 并且用户按下了删除按钮。这时需要再往edittext里添加进一个ZERO_WIDTH_SPACE
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // edittext 现在的内容
                String text = editable.toString();

                // edittext 现在的字数
                int length = text.length();

                if (length < preLength) {
                    // edittext 的字数比之前记录的字数要少，说明进行了删除操作
                    deleteCount += 1;
                    textview.setText(String.format("delete count: %s", deleteCount));
                }
                preLength = length;

                // 【注意】为了保证每次删除事件都能检测到，ZERO_WIDTH_SPACE必须要在所有输入内容的前面，并且要在光标前面
                // 有2种情况需要特别处理：
                // 1.用户把 ZERO_WIDTH_SPACE 删除掉了：
                // 这个时候需要在 edittext 最前面手动加上 ZERO_WIDTH_SPACE。
                // 2.edittext 的光标出现在了 ZERO_WIDTH_SPACE 之前：
                // 这个时候用户所有输入的内容都会出现在 ZERO_WIDTH_SPACE 之前。
                // 为了保证 ZERO_WIDTH_SPACE 出现在所有内容之前，
                // 需要把 edittext 里位置不对的 ZERO_WIDTH_SPACE 移除掉，
                // 并在 edittext 最前面加上 ZERO_WIDTH_SPACE
                if (!text.startsWith(ZERO_WIDTH_SPACE)) {
                    text = text.replace(ZERO_WIDTH_SPACE, "");
                    text = ZERO_WIDTH_SPACE + text;
                    edittext.setText(text);
                }

                // 如果现在 edittext 里只有 ZERO_WIDTH_SPACE，将光标移到 ZERO_WIDTH_SPACE 之后
                if (text.equals(ZERO_WIDTH_SPACE)) {
                    moveSelectionToLast(edittext);
                }
            }
        });
    }

    // 将光标移到末尾
    private void moveSelectionToLast(EditText edittext) {
        int length = edittext.getText().length();
        edittext.setSelection(length);
    }
}
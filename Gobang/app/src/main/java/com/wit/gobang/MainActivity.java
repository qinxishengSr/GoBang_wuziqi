package com.wit.gobang;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private ChessBoardView chessBoardView;
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chessBoardView = (ChessBoardView) findViewById(R.id.boardView);
        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener((v) -> {
            chessBoardView.retractAFalseMove();
        });
    }


//    这是 Android 应用程序的一个菜单事件监听方法，当用户点击菜单项时，会调用该方法。在该方法中，可以根据不同的菜单项 ID 来执行不同的操作。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 再来一局
        if (id == R.id.play_again) {
            chessBoardView.playAgain();
            return true;
        } else if (id == R.id.exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);//return 语句的作用是告诉 Android 系统是否应该继续执行父类的默认实现。如果返回 true，则表示已经处理了这个事件，不需要再执行其他操作；如果返回 false，则表示还需要继续处理这个事件，由父类负责执行
    }

    //onCreateOptionsMenu() 用于创建选项菜单，将菜单项添加到菜单中。它的返回值表示菜单是否显示出来，如果返回 true 则表示显示，否则不显示。
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//调用 getMenuInflater().inflate() 方法可以根据 XML 文件加载并构建菜单对象。第一个参数 R.menu.menu_main 表示要加载的菜单资源文件 ID，第二个参数 menu 是要填充菜单项的目标菜单对象。
        return true;
    }
}
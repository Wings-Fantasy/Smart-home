package com.hxshijie.smarthome;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hxshijie.Thread.ClientCtrlThread;
import com.hxshijie.Thread.ClientThread;
import com.hxshijie.util.JSON;

import org.json.JSONException;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean exitFlag = false;
    private Timer timer = new Timer();
    private ClientThread clientThread;
    private ClientCtrlThread clientCtrlThread;
    private TextView linkStatus;
    private ImageView linkStatusColor;
    private Socket socket;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint({"ResourceAsColor", "SetTextI18n"})
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Spinner aimsList = findViewById(R.id.aimsList);
            Button on = findViewById(R.id.on);
            Button off = findViewById(R.id.off);
            switch (msg.what) {
                case 0:
                    linkStatus.setText(""+bundle.get("text"));
                    linkStatusColor.setColorFilter(Color.argb(255,0,200,255));
                    linkStatus.setTextColor(Color.WHITE);
                    aimsList.setEnabled(true);
                    on.setEnabled(true);
                    off.setEnabled(true);
                    break;
                case 1:
                    linkStatus.setText(""+bundle.get("text"));
                    linkStatusColor.setColorFilter(Color.RED);
                    linkStatus.setTextColor(Color.WHITE);
                    aimsList.setEnabled(false);
                    on.setEnabled(false);
                    off.setEnabled(false);
                    break;
                case 100:
                    Toast.makeText(getBaseContext(), ""+bundle.get("text"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkStatus = findViewById(R.id.linkStatus);
        linkStatusColor = findViewById(R.id.linkStatusColor);

        clientThread = new ClientThread(this);
        clientThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!exitFlag) {
                exitFlag = true;
                Toast.makeText(getBaseContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        exitFlag = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
                if (clientThread != null) {
                    if (clientThread.getClientAgentThread() != null) {
                        clientThread.getClientAgentThread().interrupt();
                    }
                    clientThread.interrupt();
                }
                System.exit(0);
            }
        }
        return true;
    }

    public void On_Click(View view) {
        Spinner aimsList = findViewById(R.id.aimsList);
        String aims = switchAims(aimsList.getSelectedItem().toString());
        JSON json = new JSON();
        try {
            json.setValue("msg","CTRL");
            json.setValue("aims",aims);
            json.setValue("status","on");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        clientCtrlThread.setJson(json);
    }

    public void Off_Click(View view) {
        Spinner aimsList = findViewById(R.id.aimsList);
        String aims = switchAims(aimsList.getSelectedItem().toString());
        JSON json = new JSON();
        try {
            json.setValue("msg","CTRL");
            json.setValue("aims",aims);
            json.setValue("status","off");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        clientCtrlThread.setJson(json);
    }

    private String switchAims(String aims) {
        switch (aims) {
            case "灯1":
                aims = "L1";
                break;
            case "灯2":
                aims = "L2";
                break;
            case "灯3":
                aims = "L3";
                break;
            case "灯4":
                aims = "L4";
                break;
            case "灯5":
                aims = "L5";
                break;
        }
        return aims;
    }

    public ClientCtrlThread getClientCtrlThread() {
        return clientCtrlThread;
    }

    public void setClientCtrlThread(ClientCtrlThread clientCtrlThread) {
        this.clientCtrlThread = clientCtrlThread;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Handler getHandler() {
        return handler;
    }
}

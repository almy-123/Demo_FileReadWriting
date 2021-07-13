package sg.edu.rp.id19037610.demofilereadwriting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    String folderLocation;
    String extFolderLocation;
    Button btnRead, btnWrite;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI handlers to be defined
        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);
        tv = findViewById(R.id.tv);

        folderLocation = getFilesDir().getAbsolutePath() + "/MyFolder";
        extFolderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyExtFolder";

        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "folder created");
            }
        }

        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(MainActivity.this, permission, 0);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    File targetFile = new File(folderLocation, "data.txt");
                    // true for appending to existing data
                    // false for overwriting existing data
                    FileWriter writer = new FileWriter(targetFile, true);
                    writer.write("test data" + "\n");
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                try {
                    File extTargetFile = new File(extFolderLocation, "data.txt");
                    FileWriter extWriter = new FileWriter(extTargetFile, true);
                    extWriter.write("Hello world" + "\n");
                    extWriter.flush();
                    extWriter.close();
                } catch (Exception e) {
                    String msg = "Failed to write to external storage";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For internal storage
                File targetFile = new File(folderLocation, "data.txt");

                if (targetFile.exists() == true) {
                    String data = "";

                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);

                        String line = br.readLine();
                        while (line != null) {
                            data += line + "\n";
                            line = br.readLine();
                        }

                        tv.setText(data);

                        br.close();
                        reader.close();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.d("Content", data);

                    // For external Storage
                    File extTargetFile = new File(extFolderLocation, "data.txt");

                    if (extTargetFile.exists() == true) {
                        String extData = "";
                        try {
                            FileReader extReader = new FileReader(extTargetFile);
                            BufferedReader extBr = new BufferedReader(extReader);

                            String line = extBr.readLine();
                            while (line != null) {
                                extData += line + "\n";
                                line = extBr.readLine();
                            }
                            extBr.close();
                            extReader.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read external", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        Log.d("Ext Content", extData);
                    }
                }
            }
        });
    }

    public void createExternalFolder() {
        // Folder creation
        File extFolder = new File(extFolderLocation);
        if (extFolder.exists() == false) {
            boolean result = extFolder.mkdir();
            if (result == true) {
                Log.d("Ext File Read/Write", "folder created");
            }
        }

    }

    private boolean checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int checkRead = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED &&
                checkRead == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (checkPermission()) {
            createExternalFolder();
        }
    }
}
package com.example.nutriseeon;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    public int camRequestTime = 1;
    private String device_name = "";
    private String device_address = "";
    AndroidResponse androidResponse;

    public enum NetworkState {
        NONE, REQUESTED, RECEIVED
    }

    public NetworkState netState;

    public enum ServiceState {
        DETECT_HAND, ROTATE, FLIP, DONE
    }

    public ServiceState stage;
    public JSONObject retVal = null;
    Handler camHandler;

    private String fire = "-1";

    public String mType = "Joystick";

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    public String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    public BluetoothGattCharacteristic bluetoothGattCharacteristicHM_10;

    private int INNER_MAX_TIME = 6000;

    private TextView actionString;
    private TextView timeHolding;
    private TextView Inner_text;

    private boolean shooted;
    private boolean retry;

    private String superString = "0;0;-1";

    final Handler handler1 = new Handler();

    private TextView ltxt, rtxt, rrtxt, rltxt;
    private double rr = 1.0d, rl = 1.0d;
    private int desviationl = 100, desviationr = 100;
    private double r = 0, l = 0;
    private Button r_btn, l_btn, u_btn, d_btn;
    private int rotateCount;
    private int flipCount;
    private int ROTATE_LIMIT = 10;
    private int FLIP_LIMIT = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (Button) findViewById(R.id.btn_takepicture);
        assert takePictureButton != null;

        netState = NetworkState.NONE;
        stage = ServiceState.DETECT_HAND;

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (netState == NetworkState.NONE) {
                    takePicture();
                }
            }
        });

        camHandler = new Handler();

        camHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TRIGGER", "ATTEMPT");
                if (netState == NetworkState.NONE ) {
                    Log.e("PICTURE", "TAKING");
                    takePicture();
                }
                else{
                    Log.e("NETWORK USING", "NOT TRIGGERED");
                }
                camHandler.postDelayed(this, camRequestTime * 1000);
            }
        }, camRequestTime * 1000);

        Intent intent = getIntent();
        device_name = intent.getStringExtra(MainActivity.EXTRAS_DEVICE_NAME);
        device_address = intent.getStringExtra(MainActivity.EXTRAS_DEVICE_ADDRESS);
        androidResponse = new AndroidResponse(getApplicationContext());

        //setContentView(R.layout.gatt_services_characteristics);

        Log.e("LOG", "Device Control");

        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        //FEEL FREE TO DELETE

        AudioManager volumeConfig = (AudioManager) getSystemService(AUDIO_SERVICE);
        volumeConfig.setStreamVolume(AudioManager.STREAM_MUSIC, 6, 0);

//        actionString = (TextView) findViewById(R.id.stateShoot);
//        timeHolding = (TextView) findViewById(R.id.timeHolding);
//        Inner_text = (TextView) findViewById(R.id.Inner_max_time);
//        Inner_text.setText(""+(INNER_MAX_TIME/1000)+"seconds");
//        timeHolding.setText("0");

        //Button configuration
//        r_btn = (Button) findViewById(R.id.right_button);
//        assert r_btn != null;
//        r_btn.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                superString = "r";
//                sendMove();
//            }
//        });
//
//        l_btn = (Button) findViewById(R.id.left_button);
//        assert l_btn != null;
//        l_btn.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                superString = "l";
//                sendMove();
//            }
//        });
//
//        u_btn = (Button) findViewById(R.id.up_button);
//        assert u_btn != null;
//        u_btn.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                superString = "u";
//                sendMove();
//            }
//        });
//
//        d_btn = (Button) findViewById(R.id.down_button);
//        assert d_btn != null;
//        d_btn.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                superString = "d";
//                sendMove();
//            }
//        });

        final int delay = 25;
        rotateCount = 0;
        flipCount = 0;
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                transmit();
                mConnected = true;
                invalidateOptionsMenu();

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            }
        }

    };

    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };



    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    // 네트워크 통신 보내는 모듈. request 보내고 response 받아서 처리 하면 될 구간
    public void send2Server(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();

        String BASE_URL = "http://27.96.134.241:8080/";
        String RequestURL = "";

        if (stage == ServiceState.ROTATE && ++rotateCount > ROTATE_LIMIT){
            stage = ServiceState.FLIP;
            rotateCount = 0;
        };

        if (stage == ServiceState.FLIP && ++flipCount > FLIP_LIMIT){
            stage = ServiceState.DETECT_HAND;
            flipCount = 0;
        }


        switch (stage) {
            case DETECT_HAND:
                RequestURL = BASE_URL + "detHand";
                break;
            case LOCATE_HAND:
                RequestURL = BASE_URL + "locHand";
                break;
            case ROTATE:
                RequestURL = BASE_URL + "rotate";
                break;
            case FLIP:
                RequestURL = BASE_URL + "flip";
                break;
        }

        Request request = new Request.Builder().url(RequestURL)  // Flask server URL
                .post(requestBody)
                .build();

//        Log.e("requestbody", bodyToString(request));

        OkHttpClient client = new OkHttpClient();

        netState = NetworkState.REQUESTED;
        Log.d("NETSTATE REQUESTED : ", String.valueOf(netState));


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                netState = NetworkState.RECEIVED;
                Log.d("NETSTATE RECEIVED : ", String.valueOf(netState));


                String retString = response.body().string();
                try {
                    retVal = new JSONObject(retString);
                    Log.e("retFeedback", (String) retVal.get("feedback"));
                    Log.e("retStage", (String) retVal.get("stage"));

                    stage = ServiceState.valueOf((String) retVal.get("stage"));
                    Log.e("changedStage", String.valueOf(stage));

                    switch (stage) {
                        case DETECT_HAND:
                            Log.e("STAGE?", "DETECTHAND");
                            String fb_str = (String) retVal.get("feedback");
                            switch (fb_str) {
                                case "CLOSE":
                                    androidResponse.Close();
                                    break;
                                case "FAR":
                                    androidResponse.Far();
                                    break;
                                case "LEFT":
                                    superString = "l";
                                    sendMove();
                                    break;
                                case "RIGHT":
                                    superString = "r";
                                    sendMove();
                                    break;
                                case "DOWN":
                                    superString = "d";
                                    sendMove();
                                    break;
                                case "UP":
                                    superString = "u";
                                    sendMove();
                                    break;
                                case "location clear":
                                    Log.e("Wrong", "location clear");
                                    break;
                            }

                            break;
                        case ROTATE:
                            Log.e("STAGE?", "ROTATE");
                            androidResponse.Rotate();
                            break;
                        case FLIP:
                            Log.e("STAGE?", "FLIP");
                            androidResponse.Flip();
                            break;
                        case DONE:
                            Log.e("STAGE?", "DONE");
                            
                            JSONObject nutriObj = new JSONObject((String) retVal.get("feedback"));
                            camHandler.removeCallbacksAndMessages(null);
                            String[] nutriVal = androidResponse.Done(SettingActivity.nutriSet, nutriObj);
                            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                            intent.putExtra("nutriVal", nutriVal);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        // result
                    }

                    netState = NetworkState.NONE;
                    Log.d("NETSTATE RESETED : ", String.valueOf(netState));


                } catch (JSONException e) {
                    e.printStackTrace();
                    netState = NetworkState.NONE;
                }

            }
        });


    }


    protected void takePicture() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireNextImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                        Log.e("save", String.valueOf(file));
                        send2Server(file);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);

        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        camHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        String LIST_NAME = "NAME";
        String LIST_UUID = "UUID";
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);

                bluetoothGattCharacteristicHM_10 = gattService.getCharacteristic(BluetoothLeService.UUID_HM_10);


            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            event.startTracking();
            actionString.setText(R.string.shooting);
            fire = "1";
            sendMove();
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            event.startTracking();
            actionString.setText(R.string.recovery);
            fire = "0";
            sendMove();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            setNormal();
            shooted = true;
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            shooted = false;
            fire = "-1";
            retry = true;
            maxHolder();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setNormal() {
        final Handler handler = new Handler();
        int delay = 2000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeHolding.setText("0");
                fire = "-1";
                sendMove();
                actionString.setText(R.string.no_action);
            }
        }, delay);
    }

    private void maxHolder(){
        final int[] t = {0};
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!shooted) {
                    t[0]++;
                    timeHolding.setText(getString(
                            R.string.timeHolding, t[0]));
                    if ((t[0] == (INNER_MAX_TIME / 1000)) && !retry) {
                        actionString.setText(R.string.shooting);
                        fire = "1";
                        shooted = true;
                        sendMove();
                        setNormal();
                    } else if (t[0] < INNER_MAX_TIME / 1000 && !retry) {
                        handler1.postDelayed(this, 1000);
                    }else if(retry){
                        t[0] = 0;
                        retry = false;
                        maxHolder();
                    }
                }
            }
        }, 1000);
    }

    //UNTIL HERE

    //Feel Free to MODIFY

    private void sendMove() {

        int ll = (int) (l * 2.55 * ((double) desviationl / 100));
        int rr = (int) (r * 2.55 * ((double) desviationr / 100));

        //superString = "25" ;//rr + ";" + ll + ";" + fire + "\n";
        Log.d("BtConnet", superString);
    }

    private void transmit(){
        final Handler mHandler = new Handler();

        Toast.makeText(this, "Wait for the connection to stablish", Toast.LENGTH_LONG).show();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //Log.d("BtSending", "run: Sending..");


                mBluetoothLeService.writeCharacteristic(superString,bluetoothGattCharacteristicHM_10);

                mHandler.postDelayed(this,250);

            }
        },1000);

    }

};


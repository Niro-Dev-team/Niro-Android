package com.niro.niroapp.pubnub_chat.groupchat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.Attachments;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.ChatAppMsgAdapter;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.ChatAppMsgDTO;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.files.PNFileUploadResult;
import com.pubnub.api.models.consumer.files.PNFileUrlResult;
import com.pubnub.api.models.consumer.files.PNListFilesResult;
import com.pubnub.api.models.consumer.files.PNUploadedFile;
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult;
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;


public class GroupChatActivity extends AppCompatActivity
{
    public static Context context;
    private static final String TAG = "Permissions";
    final private String clientUUID = java.util.UUID.randomUUID().toString();
    private EditText entryUpdateText;
    private Button send,gotogroup;
    private PubNub pubnub;
    String sendTime;
    String url;

    ProgressDialog proDialog;
    private String theChannel = "NewTesting3";
    private String senderName;
    String checker ="";
    Integer imgid=  R.mipmap.ic_launcher;
    ListView list;
    androidx.appcompat.widget.Toolbar toolbar;
    ChatAppMsgDTO msgDto;
    Attachments attachments;
    String msgContent;
    LinearLayoutManager linearLayoutManager;
    EditText msgInputText;
    final List<ChatAppMsgDTO> msgDtoList = new ArrayList<ChatAppMsgDTO>();
    final List<Attachments> attachmentsList= new ArrayList<Attachments>();
    ChatAppMsgAdapter chatAppMsgAdapter;
    RecyclerView msgRecyclerView;
    ScrollView scrollview;
    private String sendTime2;
    public static final int PICK_IMAGE = 1;
    private ImageView sentImage;
    public static final int PICK_VIDEO=2;
    public static final int PICK_FILES=3;
    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat_screen_layout);
        context = getApplicationContext();
        Bundle bn = getIntent().getExtras();
        assert bn != null;
        senderName =bn.getString("name");
        sentImage = findViewById(R.id.sentImage);
        msgInputText = (EditText)findViewById(R.id.chat_input_msg);
        msgInputText.setFocusableInTouchMode(true);
        msgInputText.requestFocus();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        scrollview = ((ScrollView) findViewById(R.id.scr));

        msgInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollview.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 200);
            }
        });

        msgInputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                CheckPermissions();
                isStoragePermissionGranted();
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (msgInputText.getRight() - msgInputText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        CharSequence[] options = new CharSequence[]
                                {
                                  "Images",
                                        "Videos",
                                        "File"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupChatActivity.this);
                        builder.setTitle("Select the file");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0)
                                {
                                    checker ="image";
                                    Intent intent = new Intent();
                                      intent.setType("image/*");
                                      intent.setAction(Intent.ACTION_GET_CONTENT);
                                     startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                                }
                                if(i==1)
                                { checker ="videos";
                                Intent intent = new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
                                }
                                if(i==2)
                                {
                                    checker ="files";
                                        Intent intent = new Intent();
                                        intent.setType("application/pdf");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILES);
                                }

                            }
                        });
                        builder.show();

                        // your action here
                       // ContentUriUtils.INSTANCE.getFilePath(getApplicationContext(), uri);
//                        Intent intent = new Intent();
//                        intent.setType("*/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//                        Toast.makeText(GroupChatActivity.this,"suc",Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });

        PNConfiguration pnconfig = new PNConfiguration();
        pnconfig.setPublishKey("pub-c-f678753e-5d43-4f11-807b-d25346592606");
        pnconfig.setSubscribeKey("sub-c-69aa9ba8-025e-11eb-8930-9a105f766811");
        pnconfig.setUuid(senderName);

        pubnub = new PubNub(pnconfig);
         msgRecyclerView = (RecyclerView)findViewById(R.id.chat_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        chatAppMsgAdapter  = new ChatAppMsgAdapter(msgDtoList,this);
        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.channelName);
        TextView msubtitle = (TextView) toolbar.findViewById(R.id.channelsub);
        CircleImageView groupIcon = toolbar.findViewById(R.id.groupDp);
        setSupportActionBar(toolbar);
        mTitle.setText(theChannel);
        msubtitle.setText("1000 members");


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Button msgSendButton = (Button)findViewById(R.id.chat_send_msg);


                pubnub.addListener(new SubscribeCallback() {
                    // PubNub status
                    @Override
                    public void status(PubNub pubnub, PNStatus status) {
                        switch (status.getOperation()) {
                            // combine unsubscribe and subscribe handling for ease of use
                            case PNSubscribeOperation:
                            case PNUnsubscribeOperation:
                                // Note: subscribe statuses never have traditional errors,
                                // just categories to represent different issues or successes
                                // that occur as part of subscribe
                                switch (status.getCategory()) {
                                    case PNConnectedCategory:
                                        // No error or issue whatsoever.
                                    case PNReconnectedCategory:
                                        // Subscribe temporarily failed but reconnected.
                                        // There is no longer any issue.
                                    case PNDisconnectedCategory:
                                        // No error in unsubscribing from everything.
                                    case PNUnexpectedDisconnectCategory:
                                        // Usually an issue with the internet connection.
                                        // This is an error: handle appropriately.
                                    case PNAccessDeniedCategory:
                                        // PAM does not allow this client to subscribe to this
                                        // channel and channel group configuration. This is
                                        // another explicit error.
                                    default:
                                        // You can directly specify more errors by creating
                                        // explicit cases for other error categories of
                                        // `PNStatusCategory` such as `PNTimeoutCategory` or
                                        // `PNMalformedFilterExpressionCategory` or
                                        // `PNDecryptionErrorCategory`.
                                }

                            case PNHeartbeatOperation:
                                // Heartbeat operations can in fact have errors,
                                // so it's important to check first for an error.
                                // For more information on how to configure heartbeat notifications
                                // through the status PNObjectEventListener callback, refer to
                                // /docs/android-java/api-reference-configuration#configuration_basic_usage
                                if (status.isError()) {
                                    // There was an error with the heartbeat operation, handle here
                                } else {
                                    // heartbeat operation was successful
                                }
                            default: {
                                // Encountered unknown status type
                            }
                        }
                    }

                    // Messages
                    @Override
                    public void message(PubNub pubnub, PNMessageResult message) {
                        Log.d("messgage", String.valueOf(message));
                        Log.d("timestamp", String.valueOf(message.getTimetoken()));
                        JsonObject recived = message.getMessage().getAsJsonObject();
                        final String senderNameStatus = recived.get("senderName").getAsString();
                        final String messageValue = recived.get("message").getAsString();
                        final String timeToken = message.getTimetoken().toString();
                        String time = pubNubTimeToLOcal(timeToken);
                        Log.d("time", time);
                        Log.d("RecivedMSg", senderNameStatus + " " + messageValue);


                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, messageValue, senderNameStatus, time);

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (!senderNameStatus.equals(senderName)) {
                                    msgDtoList.add(msgDto);
                                    linearLayoutManager.setStackFromEnd(true);
                                    msgRecyclerView.setAdapter(chatAppMsgAdapter);

                                    msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());

                                }
                            }
                        });


                    }

                    // Presence
                    @Override
                    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                        Log.d("Presence Event: ", presence.getEvent());
                        // Can be join, leave, state-change or timeout

                        Log.d("Presence Channel: ", presence.getChannel());
                        // The channel to which the message was published

                        Log.d("Presence Occupancy: ", String.valueOf(presence.getOccupancy()));
                        // Number of users subscribed to the channel

                        Log.d("Presence State: ", String.valueOf(presence.getState()));
                        // User state

                        Log.d("Presence UUID: ", presence.getUuid());
                        // UUID to which this event is related

                        presence.getJoin();
                        // List of users that have joined the channel (if event is 'interval')

                        presence.getLeave();
                        // List of users that have left the channel (if event is 'interval')

                        presence.getTimeout();
                        // List of users that have timed-out off the channel (if event is 'interval')

                        presence.getHereNowRefresh();
                        // Indicates to the client that it should call 'hereNow()' to get the
                        // complete list of users present in the channel.
                    }

                    // Signals
                    @Override
                    public void signal(PubNub pubnub, PNSignalResult signal) {
                        Log.d("Signal publisher: ", signal.getPublisher());
                        Log.d("Signal payload: ", String.valueOf(signal.getMessage()));
                        Log.d("Signal subscription: ", signal.getSubscription());
                        Log.d("Signal channel: ", signal.getChannel());
                        Log.d("Signal timetoken: ", String.valueOf(signal.getTimetoken()));
                    }

                    @Override
                    public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) {

                    }

                    @Override
                    public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) {

                    }

                    @Override
                    public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) {

                    }

                    // Message actions
                    @Override
                    public void messageAction(PubNub pubnub, PNMessageActionResult pnActionResult) {
                        PNMessageAction pnMessageAction = pnActionResult.getMessageAction();
                        Log.d("Message action type: ", pnMessageAction.getType());
                        Log.d("Message action value: ", pnMessageAction.getValue());
                        Log.d("Message action uuid: ", pnMessageAction.getUuid());
                        Log.d(" actionTimetoken: ", String.valueOf(pnMessageAction.getActionTimetoken()));
                        Log.d("messageTimetoken: ", String.valueOf(pnMessageAction.getMessageTimetoken()));
                        Log.d(" subscription: ", pnActionResult.getSubscription());
                        Log.d("action channel: ", pnActionResult.getChannel());
                        Log.d("action timetoken: ", String.valueOf(pnActionResult.getTimetoken()));
                    }

                    // Files
                    @Override
                    public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) {
                        Log.d("File channel: ", pnFileEventResult.getChannel());
                        Log.d("File publisher: ", pnFileEventResult.getPublisher());
                        Log.d("File message: ", String.valueOf(pnFileEventResult.getMessage()));
                        Log.d("File timetoken: ", String.valueOf(pnFileEventResult.getTimetoken()));
                        Log.d("File file.id: ", pnFileEventResult.getFile().getId());
                        Log.d("File file.name: ", pnFileEventResult.getFile().getName());
                        Log.d("File file.url: ", pnFileEventResult.getFile().getUrl());
                        String time = pubNubTimeToLOcal(String.valueOf(pnFileEventResult.getTimetoken()));
                        String publisherName = pnFileEventResult.getPublisher();
                        String Fileurl =pnFileEventResult.getFile().getUrl();

                        if (!pnFileEventResult.getPublisher().equals(senderName)) {
                            msgDto = new ChatAppMsgDTO(Fileurl,publisherName , ChatAppMsgDTO.MSG_TYPE_RECEIVED, "media", time);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    msgDtoList.add(msgDto);
                                    msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                    chatAppMsgAdapter.notifyDataSetChanged();
                                    msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
                                }
                            });
                        }
                        else {
                            msgDto = new ChatAppMsgDTO(pnFileEventResult.getFile().getUrl(), pnFileEventResult.getPublisher(), ChatAppMsgDTO.MSG_TYPE_SENT, "media", time);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    msgDtoList.add(msgDto);
                                    msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                    chatAppMsgAdapter.notifyDataSetChanged();
                                    msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
                                    proDialog.dismiss();
                                }
                            });
                        }




                    }
                });
        pubnub.subscribe()
                .channels(Arrays.asList(theChannel))
                .withPresence()
                .execute();
        try {
            fetchMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }


        pubnub.listFiles()
                .channel(theChannel)
                .async(new PNCallback<PNListFilesResult>() {
                    @Override
                    public void onResponse(PNListFilesResult result, PNStatus status) {
                        if (!status.isError()) {
                            Log.d("files status: " , String.valueOf(result.getStatus()));
                            Log.d("files status: " , String.valueOf(result.getNext()));
                            Log.d("files status: " , String.valueOf(result.getCount()));
                            Log.d("files status: " , String.valueOf(result.getCount()));
                            for (PNUploadedFile file : result.getData()) {
                                Log.d("files fileId: " , file.getId());
                                Log.d("files fileName: " , file.getName());
                                Log.d("files fileSize: " , String.valueOf(file.getSize()));
                                Log.d("files fileCreated: " , file.getCreated());



                            }
                        }
                        Log.d("files status code: " , String.valueOf(status.getStatusCode()));
                    }
                });

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgContent= msgInputText.getText().toString();
                if(!TextUtils.isEmpty(msgContent))
                {
                    // Add a new sent message to the list.
                    submitMessage(senderName,msgInputText.getText().toString(),"text");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            sentMessgae(msgContent,sendTime2);
                            msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
                        }
                    });

                }
            }
        });


        //linearLayoutManager.smoothScrollToPosition(msgRecyclerView, null, msgDtoList.size() - 1);


    }
    public void onResume() {
        super.onResume();

        msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("results",requestCode+" "+resultCode);
        //ArrayList<Uri> arrayList = new ArrayList<>();

        if (resultCode == RESULT_OK) {
            Uri content_describer = null;
            switch (requestCode) {
                case PICK_IMAGE: {
                    if (data != null) {
                        content_describer = data.getData();
                        String[] projection = {MediaStore.Images.Media.DATA};


                        try {
                            InputStream in = getContentResolver().openInputStream(content_describer);
                            Bitmap bmp = BitmapFactory.decodeStream(in);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            try {
                                stream.close();
                                stream = null;
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                            InputStream is = new ByteArrayInputStream(byteArray);
                            String fileName = queryName(getContentResolver(), content_describer);
                            Log.e("FileName", fileName);
                             proDialog = ProgressDialog.show(this, "Uploading Your File ", "Please wait");
                            proDialog.show();
                           sendMediaMsg(fileName,is);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                }
                case PICK_VIDEO:
                    if (data!=null)
                    {
                        content_describer = data.getData();
                        String Fname = queryName(getContentResolver(),content_describer);
                        String[] projection = {MediaStore.Images.Media.DATA};
                        String videoPath = getRealPathFromURI_API19(getApplicationContext(),content_describer);
                        String folder_main = "Niro";
                        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "compressedVideos");
                        if (!f1.exists()) {
                            f1.mkdirs();
                        }

//                        String Vname= f1.getAbsolutePath()+"/"+Fname;
//                        Log.e("paths",videoPath+"-"+Vname);
//                        try {
//                            String filePath = FileUtils.getPath(this, content_describer);
//                            String newPath = filePath.replace(".mp4", "_Compressed.mp4");
//
//                        } catch (CompressionException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            assert videoPath != null;
//                            SiliCompressor.with(getApplicationContext(), true).compressVideo(new VideoConversionProgressListener() {
//                                @Override
//                                public void videoConversionProgressed(float progressPercentage, @Nullable Long estimatedNumberOfMillisecondsLeft) {
//
//                                }
//
//
//                            }, data.getDataString(),Vname , 0.01F);
//                        } catch (CompressionException e) {
//                            Log.d("error","something Happend"+e);
//                            e.printStackTrace();
//                        }
                        double size =  getFileSizeFromUriInMegaByte(getApplicationContext(),content_describer);
                      Log.d("size", String.valueOf(size));
//                        File mydir = getApplicationContext().getDir("Niro", Context.MODE_PRIVATE); //Creating an internal dir;
//                        File fileWithinMyDir = new File(mydir, "compressed"+vName); //Getting a file within the dir.
//                        try {
//                            FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }

                    }

            }


            }
        }
protected String GetFileUrl(String channel, String fileName, String fileID)
{

    pubnub.getFileUrl()
            .channel(channel)
            .fileName(fileName)
            .fileId(fileID)
            .async(new PNCallback<PNFileUrlResult>() {
                @Override
                public void onResponse(PNFileUrlResult result, PNStatus status) {
                    if (!status.isError()) {
                        System.out.println("getUrl fileUrl: " + result.getUrl());
                      url = result.getUrl();
                        Log.d("extension",getMimeType(result.getUrl()));
                    }
                    System.out.println("getUrl status code: " + status.getStatusCode());
                }
            });

    return url;
}
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
    public static double getFileSizeFromUriInMegaByte(Context context, Uri uri) {
        String scheme = uri.getScheme();
        double dataSize = 0;
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try {
                InputStream fileInputStream = context.getContentResolver().openInputStream(uri);
                if (fileInputStream != null) {
                    dataSize = fileInputStream.available();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            String path = uri.getPath();
            File file = null;
            try {
                file = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (file != null) {
                dataSize = file.length();
            }
        }
        return dataSize / (1024 * 1024);
    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    protected void fetchMessages()
    {

        pubnub.fetchMessages()
                .channels(Arrays.asList(theChannel))
                .maximumPerChannel(40)
                .end(15343325004275466L)
                .includeMeta(true)
                .includeMessageType(true)
                .includeUUID(true)
                .async(new PNCallback<PNFetchMessagesResult>() {
                    @Override
                    public void onResponse(PNFetchMessagesResult result, PNStatus status) {
                        if (!status.isError()) {
                            Map<String, List<PNFetchMessageItem>> channels = result.getChannels();
                            List<PNHistoryItemResult> hs ;

                            for (PNFetchMessageItem messageItem : channels.get(theChannel)) {

                                Log.d("fetch", String.valueOf(messageItem.getMessage()));
                                Log.d("fetch", String.valueOf(messageItem.getMeta()));
                                Log.d("fetch", String.valueOf(messageItem.getUuid()));
                                Log.d("fetch", String.valueOf(messageItem.getTimetoken()));

                                JsonObject recived = messageItem.getMessage().getAsJsonObject();

                                Log.d("Message",messageItem.toString());
                                try {
                                    Method m = PNFetchMessageItem.class.getDeclaredMethod("getMessageType");
                                    m.setAccessible(true);
                                    int messageType = (int) m.invoke(messageItem);
                                    Log.e("tag", String.valueOf(messageType));
                                    final String timeToken = messageItem.getTimetoken().toString();
                                    String time=pubNubTimeToLOcal(timeToken);
                                    if(messageType ==0)
                                    {
                                final String entryVal  = recived.get("senderName").getAsString();
                                final String updateVal =recived.get("message").getAsString();


                                Log.d("time",time);
                                        if(!senderName.equals(entryVal) )
                                        {
                                            msgDto= new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED,updateVal,entryVal,time);
                                            msgDtoList.add(msgDto);
                                            msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                            msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
                                        }
                                        else {


                                            sentMessgae(updateVal,time);
                                            msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
                                        }
                                    }
                                    else if(messageType==4)
                                    {
                                        final String UUID = messageItem.getUuid();
                                         JsonObject file =recived.get("file").getAsJsonObject();
                                        final String fileID =file.get("id").getAsString();
                                        final String fileName= file.get("name").getAsString();
                                        final String url= GetFileUrl(theChannel,fileName,fileID);
                                        if (!senderName.equals(UUID))
                                        {
                                            msgDto= new ChatAppMsgDTO(url,UUID,ChatAppMsgDTO.MSG_TYPE_RECEIVED,"media",time);
                                            msgDtoList.add(msgDto);
                                            msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                            msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());

                                        }
                                        else {
                                            msgDto= new ChatAppMsgDTO(url,UUID,ChatAppMsgDTO.MSG_TYPE_SENT,"media",time);
                                            msgDtoList.add(msgDto);
                                            msgRecyclerView.setAdapter(chatAppMsgAdapter);
                                            msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());

                                        }
                                    }
                                    // Using invoke() method

                                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        else {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                    }
                });
    }


    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }
//    private void sendFiles()
    public String getPath(Uri uri) {

    String path = null;
    String[] projection = { MediaStore.Files.FileColumns.DATA };
    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

    if(cursor == null){
        path = uri.getPath();
    }
    else{
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(projection[0]);
        path = cursor.getString(column_index);
        cursor.close();
    }

    return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
}

    private String pubNubTimeToLOcal(String timeToken) {

        Long pubnubTime = Long.parseLong(timeToken);
        Long unix_seconds = pubnubTime/10000000 ;
        Date date = new Date(unix_seconds*1000L);
        // format of the date 1654565484564551
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String java_date = formatter.format(date);
        //12-10-2020 13:35:08
        String msgdate = java_date.substring(0,5);
        String hours = java_date.substring(11, 16);
       return msgdate +" "+ hours;


    }
    private void CheckPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
           // Toast.makeText(this, "Opening Gallery", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                    123, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    protected  void sentMessgae(String msgContent , String time)
    {
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent,"You",time);
        msgDtoList.add(msgDto);

        int newMsgPosition = msgDtoList.size() - 1;

        // Notify recycler view insert one new data.
        chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

        // Scroll RecyclerView to the last message.
        msgRecyclerView.scrollToPosition(newMsgPosition);
        msgRecyclerView.setAdapter(chatAppMsgAdapter);
        // Empty the input edit text box.
        msgInputText.setText("");

        msgInputText.requestFocus();
        scrollview.post(new Runnable() {
            @Override
            public void run() {


                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                linearLayoutManager.smoothScrollToPosition(msgRecyclerView, null, msgDtoList.size() - 1);
                msgRecyclerView.smoothScrollToPosition(chatAppMsgAdapter.getItemCount());
            }
        });
    }
    protected void deleteMessagesAll()
    {
        pubnub.deleteMessages()
                .channels(Arrays.asList(theChannel))
                .async(new PNCallback<PNDeleteMessagesResult>() {
                    @Override
                    public void onResponse(PNDeleteMessagesResult result, PNStatus status) {
                        // handle success or failure of the delete
                    }
                });    }
    protected void submitMessage(String anEntry, String anUpdate, String typeMsg) {
       // final String[] result2 = new String[1];
        JsonObject entryUpdate = new JsonObject();
        entryUpdate.addProperty("senderName", anEntry);
        entryUpdate.addProperty("message", anUpdate);
        entryUpdate.addProperty("type",typeMsg);

        pubnub.publish().channel(theChannel).message(entryUpdate).async(
                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            status.getErrorData().getThrowable().printStackTrace();
                        }
                        else {

                             sendTime = String.valueOf(result.getTimetoken());
                             sendTime2 = pubNubTimeToLOcal(sendTime);
                            Log.d("[PUBLISH: sent]", "timetoken: " + result.getTimetoken());

                        }
                    }
                });
    }

    protected void sendMediaMsg(String fileName, InputStream is)
    {
        pubnub.sendFile().channel(theChannel)
                .fileName(fileName)
                .inputStream(is)
                .async(new PNCallback<PNFileUploadResult>() {
                    @Override
                    public void onResponse(PNFileUploadResult result, PNStatus status) {
                        if (!status.isError()) {
                            Log.d("send timetoken: ", String.valueOf(result.getTimetoken()));
                            Log.d("send status: ", String.valueOf(result.getStatus()));
                            Log.d("send fileId: ", result.getFile().getId());
                            Log.d("send fileName: ", result.getFile().getName());
                            Log.d("Details", String.valueOf(result));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("send status code: ", String.valueOf(status.getStatusCode()));
                    }
                });
    }

}
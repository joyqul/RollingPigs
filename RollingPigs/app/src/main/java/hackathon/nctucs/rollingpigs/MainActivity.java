package hackathon.nctucs.rollingpigs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hackathon.nctucs.rollingpigs.elements.Circle;
import hackathon.nctucs.rollingpigs.elements.Node;
import hackathon.nctucs.rollingpigs.elements.Slot;


public class MainActivity extends Activity {

    // 0, 10, 15
    FrameLayout container;
    SharedPreferences sharedPreferences;
    private final int[] stories = new int[36];
    private final int[] stages = new int[]{ 0 , R.raw.lv01jsom, R.raw.lv01jsom, R.raw.lv02jsom,
    R.raw.lv03jsom, R.raw.lv04jsom, R.raw.lv05jsom, R.raw.lv06jsom, R.raw.lv07jsom, R.raw.lv08jsom, R.raw.lv09jsom
            , R.raw.lv11jsom, R.raw.lv11jsom, R.raw.lv12jsom, R.raw.lv13jsom, R.raw.lv14jsom, R.raw.lv16jsom
            , R.raw.lv16jsom, R.raw.lv17jsom, R.raw.lv18jsom, R.raw.lv19jsom, R.raw.lv20jsom, R.raw.lv21jsom
            , R.raw.lv22jsom, R.raw.lv23jsom, R.raw.lv24jsom, R.raw.lv25jsom, R.raw.lv26jsom, R.raw.lv27jsom
            , R.raw.lv28jsom, R.raw.lv29jsom};
    private final int[] circleSrc = new int[]{  R.drawable.circle_blue3 , R.drawable.pink_circle3 ,  R.drawable.circle_green
    , R.drawable.circle_gray3};
    private final int[] nodeSrc   = new int[]{ R.drawable.pig_blue , R.drawable.pig_blue ,
    R.drawable.pig_pink , R.drawable.pig_pink_blue , R.drawable.pig_green , 0 , 0 , 0 , R.drawable.pig_black};
    FrameLayout footer;
    Map< Integer , Circle > circles = new HashMap<>();
    Map< Integer , Node >   nodes = new HashMap<>();
    Map< Integer , Slot >   slots = new HashMap<>();
    MediaPlayer mediaPlayer;
    ImageView reset , new_lev;
    TextView step;
    TypeWriter top , bottom;
    AlertDialog alg;
    int cnt = 0 , stage = -1;
    boolean flag = false;

    private void reloadStage(){
        //Toast.makeText( getApplicationContext() , "stage" + stage , Toast.LENGTH_SHORT ).show();

        if ( !(stage == 1 || stage == 11 || stage == 16) ){
            step.setVisibility( View.VISIBLE );
            footer.setVisibility( View.VISIBLE );
            top.setVisibility( View.INVISIBLE );
            bottom.setVisibility( View.INVISIBLE );
        }
        else{
            step.setVisibility( View.INVISIBLE );
            footer.setVisibility( View.INVISIBLE );
            top.setVisibility( View.VISIBLE );
            bottom.setVisibility( View.VISIBLE );
            top.bringToFront();
            bottom.bringToFront();
            top.setTextColor(Color.rgb( 0x66 , 0x66 , 0x66 ) );
            bottom.setTextColor(Color.rgb(0x66, 0x66, 0x66));
        }

        circles.clear();
        nodes.clear();
        slots.clear();
        if ( !sharedPreferences.getBoolean( "str"+stage , false ) && !( stage ==1 || stage == 11 || stage==16 ) ) {
            sharedPreferences.edit().putBoolean("str" + stage, true).apply();

            ImageView img = (ImageView)findViewById( R.id.new_level );
            new_lev = img;
            img.setVisibility( View.VISIBLE );


            ObjectAnimator animator1 = ObjectAnimator.ofFloat(img, "translationX",  -25);
            animator1.setDuration(100);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX",  5);
            animator2.setDuration(100);
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(img, "translationX",  -5);
            animator3.setDuration(100);
            ObjectAnimator animator4 = ObjectAnimator.ofFloat(img, "translationX",  5);
            animator4.setDuration(100);
            ObjectAnimator animator5 = ObjectAnimator.ofFloat(img, "translationX",  -5);
            animator5.setDuration(100);
            ObjectAnimator animator6 = ObjectAnimator.ofFloat(img, "translationX",  5);
            animator6.setDuration(100);

            AnimatorSet animatorSet = new AnimatorSet();
            List<Animator> playList = new ArrayList<>();

            playList.add(animator1);
            playList.add(animator2);
            playList.add(animator3);
            playList.add(animator4);
            playList.add(animator5);
            playList.add(animator6);
            animatorSet.setStartDelay( 500 );
            animatorSet.playSequentially(playList);
            animatorSet.start();



            Thread t = new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage( msg );
                            }catch ( Exception e ){
                                e.printStackTrace();
                            }
                        }
                    }

            );
            t.start();

        }


        cnt = 0; flag = false;
        step.setText( "Step : "+cnt );
        try {
            initStage(stage);
            drawElements();
        }
        catch ( Exception e ){
            e.printStackTrace();
            this.finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText( getApplicationContext() , "started" , Toast.LENGTH_LONG ).show();
        sharedPreferences = getSharedPreferences( "db" , MODE_PRIVATE );
        stories[1] = R.drawable.story0;
        stories[11] = R.drawable.story1;
        stories[16] = R.drawable.story2;
        container = (FrameLayout)findViewById( R.id.containers );
        stage = getIntent().getIntExtra( "stage" , -1 );
        footer = (FrameLayout) findViewById( R.id.footer );

        step = (TextView)findViewById( R.id.score );
        top = (TypeWriter) findViewById( R.id.top );
        bottom = ( TypeWriter ) findViewById( R.id.bottom );

        reset = (ImageView)findViewById( R.id.reset );
        reset.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playClick();
                    reloadStage();
                }
            }

        );



        if ( stage == -1 ){ // load stage error
            Toast.makeText( getApplicationContext() , "load stage failed" , Toast.LENGTH_SHORT).show();
        }
        else{
           reloadStage();
        }

    }

    private void rotateCircle( int key ){
        circles.get( key ).rotate();

    }

    private void checkWin(){
        int cntw = 0;
        Set<Integer> nKey = nodes.keySet();

        for ( int key : nKey )
            nodes.get( key ).valid = false;

        Set<Integer> cKey = circles.keySet();

        for ( int key : cKey ){

            for ( int s_id : circles.get( key ).slots ){

                int n_id = slots.get( s_id ).getContent();

                if ( (nodes.get( n_id ).getColor() & circles.get( key ).getColor()) != 0 ){
                    if ( !nodes.get( n_id ).valid ){
                        nodes.get( n_id ).valid = true;
                        cntw++;
                    }
                }

            }


        }

        if ( cntw == slots.size() ){

            final int st = stage;
            final AlertDialog.Builder builder = new AlertDialog.Builder( this );

            View view = getLayoutInflater().inflate( R.layout.successdialog , null );
            ImageView img = (ImageView)view.findViewById( R.id.next_step );


            builder.setView( view );
            builder.setOnDismissListener(
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if ( !flag )
                            MainActivity.this.finish();
                    }
                }
            );
            alg = builder.create();

            img.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playClick();
                            stage += 1;
                            try {
                                reloadStage();
                                flag = true;
                                alg.dismiss();

                            }
                            catch ( Exception e ){
                                e.printStackTrace();
                            }
                        }
                    }


            );

            TextView yourScore = (TextView) view.findViewById(R.id.your_score);
            yourScore.setText( "Your score : " + cnt );

            int prev = sharedPreferences.getInt( ""+st , 1000 );
            if ( cnt < prev )
                sharedPreferences.edit().putInt( ""+st , cnt ).apply();
            prev = Math.min( prev , cnt );

            ((TextView) view.findViewById(R.id.high_score) ).setText( "High score : " + prev );
            Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        }
                        catch ( Exception e ){
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage( msg );

                    }
                }
            );
            t.start();


            //Toast.makeText( getApplicationContext() , "Win" , Toast.LENGTH_LONG ).show();
            sharedPreferences.edit().putInt( "stage" , stage+1 ).apply();
        }


    }

    private Handler handler = new Handler(  ){
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);

            if ( msg.what == 1 )
                alg.show();
            else if ( msg.what == 2 )
                new_lev.setVisibility( View.INVISIBLE );

        }
    };

    @Override
    protected void onResume() {
        super.onStart();

        if ( mediaPlayer != null ) {
            mediaPlayer.start();
            return;
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);

        try {
            mediaPlayer.start();

        }
        catch ( Exception e ){

        }
    }

    @Override
    protected void onPause() {
        super.onStop();

        if ( mediaPlayer != null && mediaPlayer.isPlaying() )
            mediaPlayer.pause();

    }


    private void updateCnt(){
        cnt += 1;
        step.setText( "Step : " + cnt );

        if ( cnt % 10 == 0 ) {

            Toast.makeText(this, "一陣風吹過來，豬豬覺得冷", Toast.LENGTH_SHORT).show();

            for (Node n : nodes.values()) {
                n.freezeEffect();
            }
        }
    }

    private void playClick(){
        MediaPlayer mp = MediaPlayer.create( this , R.raw.click );
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                }

        );
        try {
            mp.start();
        }
        catch ( Exception e ){
        }
    }

    private void playPig(){
        MediaPlayer mp = MediaPlayer.create( this , R.raw.pig );
        mp.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                }

        );
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.start();

        }
        catch ( Exception e ){
        }
    }

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void drawElements(){







        container.removeAllViews();

        if ( stage == 1 || stage == 11 || stage == 16 ){

            ImageView img = new ImageView(this);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT );
            img.setLayoutParams( lp );
            img.setScaleType(ImageView.ScaleType.FIT_XY );
            img.setImageResource( stories[stage] );

            int[] arr = new int[2];
            arr[0] = stage ; arr[1] = 1;
            img.setTag( arr );

            img.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                playClick();
                              int[] arr = (int[])v.getTag();
                                Log.e( "arr" , arr[0] + " " + arr[1] );

                              int st = arr[0];
                              int cnt = arr[1];
                              int id = getResourceId( "_"+st+"_"+cnt , "string" , getPackageName() );
                              if ( id == -1 ) {
                                    MainActivity.this.finish();
                                  sharedPreferences.edit().putInt( "stage" , stage+1 ).apply();
                              }
                                else {
                                  String val = "error";

                                  try {

                                      val = getString(id);
                                  }
                                  catch ( Exception e ){
                                      MainActivity.this.finish();
                                      sharedPreferences.edit().putInt( "stage" , stage+1 ).apply();
                                  }
                                  Log.e( val , val );
                                    if ( cnt % 2 == 1 ){
                                        top.animateText( val );

                                    }
                                     else{
                                        bottom.animateText( val );
                                    }
                                    arr[1] += 1;
                                    v.setTag( arr );

                                }
                        }
                    }


            );

            container.addView( img );

            return;
        }


        // draw the circles


        Set<Integer> cSet = circles.keySet();

        for ( int key : cSet ){

            Log.i( "added key" , key + "" );


            Circle circle = circles.get( key );

            int radius = (int)circle.getRadius();



            ImageView imageView = new ImageView( this );
            imageView.setScaleType( ImageView.ScaleType.FIT_XY );
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( radius*2 , radius*2 );
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.setMargins( circle.getX() - radius , circle.getY() - radius , 0 , 0 );
            imageView.setClickable(false);
            imageView.setImageResource(circle.getSrc());
            imageView.setLayoutParams(params);
            imageView.setClickable(true);
            imageView.setTag( key );
            imageView.setOnClickListener(
                    new ImageView.OnClickListener(){
                        @Override
                        public void onClick(View v) {

                            playPig();

                            rotateCircle( (int)v.getTag() );
                            updateCnt();
                            checkWin();
                        }
                    }

            );


            circle.img = imageView;

            container.addView( imageView );

            // this is the button to click



            //container.addView( imageView );


        }




        // draw the nodes


        Set<Integer> nSet = nodes.keySet();

        for ( int key : nSet ){
            Node node = nodes.get( key );

            int radius = (int)node.getRadius();

            ImageView imageView = new ImageView( this );
            imageView.setScaleType( ImageView.ScaleType.FIT_XY );
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( radius*2 , radius*2 );
            params.gravity = Gravity.TOP;
            params.setMargins( slots.get( node.getOnSId() ).getX() - radius , slots.get( node.getOnSId() ).getY() - radius , 0 , 0 );

            imageView.setImageResource(node.getSrc());
            imageView.setLayoutParams( params );


            node.img = imageView;

            container.addView( imageView );

        }








    }



    private void initStage( int stage ) throws IOException, JSONException{


        Log.e("stage", "stage"+stage);

        // requires read file



        InputStreamReader inputStreamReader = new InputStreamReader( getResources().openRawResource( stages[stage] ) );

        BufferedReader bufferedReader = new BufferedReader( inputStreamReader );



        String input = bufferedReader.readLine();

        JSONArray stageInfos = new JSONArray( input );

        Log.e( "stageInfos" , input );

        try {

            for ( int ii = 0 ; ii < stageInfos.length() ; ii++ ) {

                JSONObject stageInfo = stageInfos.getJSONObject( ii );

                if (stageInfo.getString("type").equals("circle")) {


                    Circle circle = new Circle(
                            stageInfo.getInt("id"),
                            stageInfo.getInt("color"),
                            stageInfo.getInt("x"),
                            stageInfo.getInt("y"),
                            stageInfo.getDouble("radius"),
                            circleSrc[ stageInfo.getInt( "src" ) ]
                    );



                    JSONArray slot = stageInfo.getJSONArray("slots");

                    for (int i = 0; i < slot.length(); i++)
                        circle.addSlot( slot.getInt(i) );

                    circle.setAttr( nodes , slots );

                    circles.put(stageInfo.getInt("id"), circle);


                } else if (stageInfo.getString("type").equals("node")) {

                    Node node = new Node(
                            stageInfo.getInt("id"),
                            stageInfo.getInt("color"),
                            stageInfo.getInt("onSId"),
                            stageInfo.getInt("radius"),
                            nodeSrc[ stageInfo.getInt( "color" ) ]
                    );

                    nodes.put(stageInfo.getInt("id"), node);

                } else if (stageInfo.getString("type").equals("slot")) {


                    Slot slot = new Slot(
                            stageInfo.getInt("id"),
                            stageInfo.getInt("x"),
                            stageInfo.getInt("y"),
                            stageInfo.getInt("content")
                    );

                    slots.put(stageInfo.getInt("id"), slot);

                }
            }
        }
        catch( Exception e ){
            e.printStackTrace();
        }



        for ( Circle c : circles.values())
               c.sortSlots();






    }




}

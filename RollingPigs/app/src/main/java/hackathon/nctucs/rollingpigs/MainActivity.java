package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hackathon.nctucs.rollingpigs.elements.Circle;
import hackathon.nctucs.rollingpigs.elements.Node;
import hackathon.nctucs.rollingpigs.elements.Slot;


public class MainActivity extends Activity {

    SharedPreferences sharedPreferences;
    private final int[] stages = new int[]{ R.raw.lv01jsom, R.raw.lv01jsom, R.raw.lv02jsom,
    R.raw.lv03jsom, R.raw.lv04jsom, R.raw.lv05jsom, R.raw.lv06jsom, R.raw.lv07jsom};
    private final int[] circleSrc = new int[]{ R.drawable.circle , R.drawable.circle , R.drawable.circle };
    private final int[] nodeSrc   = new int[]{ R.drawable.pig_black , R.drawable.pig_blue , R.drawable.pig_green
    , R.drawable.pig_org , R.drawable.pig_pink};

    Map< Integer , Circle > circles = new HashMap<>();
    Map< Integer , Node >   nodes = new HashMap<>();
    Map< Integer , Slot >   slots = new HashMap<>();
    MediaPlayer mediaPlayer;
    ImageView reset ;
    TextView step;
    AlertDialog alg;
    int cnt = 0 , stage = -1;
    boolean flag = false;

    private void reloadStage(){
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
        Toast.makeText( getApplicationContext() , "started" , Toast.LENGTH_LONG ).show();
        sharedPreferences = getSharedPreferences( "db" , MODE_PRIVATE );


        stage = getIntent().getIntExtra( "stage" , -1 );

        step = (TextView)findViewById( R.id.score );

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

            alg.show();

            Toast.makeText( getApplicationContext() , "Win" , Toast.LENGTH_LONG ).show();

        }


    }

    @Override
    protected void onResume() {
        super.onStart();
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
        super.onPause();

        if ( mediaPlayer.isPlaying() )
            mediaPlayer.stop();

    }


    private void updateCnt(){
        cnt += 1;
        step.setText( "Step : " + cnt );
        Toast.makeText(this, "一陣風吹過來，豬豬覺得冷", Toast.LENGTH_SHORT);
		for(Node n : nodes.values()){
			n.freezeEffect();
		}
    }

    private void playClick(){
        MediaPlayer mp = MediaPlayer.create( this , R.raw.click );
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.start();
        }
        catch ( Exception e ){
        }
    }

    private void playPig(){


        MediaPlayer mp = MediaPlayer.create( this , R.raw.pig );
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.start();
        }
        catch ( Exception e ){
        }
    }


    private void drawElements(){


        FrameLayout container = (FrameLayout)findViewById( R.id.container );

        container.removeAllViews();


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
            imageView = new ImageView( this );
            imageView.setScaleType( ImageView.ScaleType.FIT_XY );
            params = new FrameLayout.LayoutParams( radius/2 , radius/2 );
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.setMargins( circle.getX() -radius/4 , circle.getY()-radius/4 , 0 , 0 );

            imageView.setImageResource( circle.getSrc() );

            imageView.setLayoutParams( params );
            imageView.bringToFront();
            imageView.setTag(key);


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

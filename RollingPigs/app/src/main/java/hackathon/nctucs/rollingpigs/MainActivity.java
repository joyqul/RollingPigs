package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
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


    private final int[] stages = new int[]{ R.raw.stage1 , R.raw.stage1};
    private final int[] circleSrc = new int[]{ R.drawable.circle , R.drawable.circle };
    private final int[] nodeSrc   = new int[]{ R.drawable.bird_blue , R.drawable.bird_blue , R.drawable.bird_red , R.drawable.bird_yellow };

    Map< Integer , Circle > circles = new HashMap<>();
    Map< Integer , Node >   nodes = new HashMap<>();
    Map< Integer , Slot >   slots = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText( getApplicationContext() , "started" , Toast.LENGTH_LONG ).show();


        int stage = 0;

        stage = 1;
        if ( stage == -1 ){ // load stage error
            Toast.makeText( getApplicationContext() , "load stage failed" , Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                initStage(stage);
            }
            catch ( Exception e ){
                e.printStackTrace();
            }
            drawElements();
        }

    }

    private void drawElements(){


        FrameLayout container = (FrameLayout)findViewById( R.id.container );

        LayoutInflater inflater = getLayoutInflater();


        // draw the circles


        Set<Integer> cSet = circles.keySet();

        for ( int key : cSet ){
            Circle circle = circles.get( key );

            int radius = (int)circle.getRadius();


            ImageView imageView = new ImageView( this );
            imageView.setScaleType( ImageView.ScaleType.FIT_XY );
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams( radius*2 , radius*2 );
            params.gravity = Gravity.TOP;
            params.setMargins( circle.getX() + radius , circle.getY() + radius , 0 , 0 );

            imageView.setImageResource( circle.getSrc() );
            imageView.setLayoutParams( params );

            circle.img = imageView;

            container.addView( imageView );
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
            params.setMargins( slots.get( node.getOnSId() ).getX() + radius , slots.get( node.getOnSId() ).getY() + radius , 0 , 0 );

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


                    circles.put(stageInfo.getInt("id"), circle);


                } else if (stageInfo.getString("type").equals("node")) {

                    Node node = new Node(
                            stageInfo.getInt("id"),
                            stageInfo.getInt("color"),
                            stageInfo.getInt("onSId"),
                            stageInfo.getInt("radius"),
                            nodeSrc[ stageInfo.getInt( "src" ) ]
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





    }




}

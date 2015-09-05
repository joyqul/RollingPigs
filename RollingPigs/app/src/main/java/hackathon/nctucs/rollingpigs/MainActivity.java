package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hackathon.nctucs.rollingpigs.elements.Circle;
import hackathon.nctucs.rollingpigs.elements.Node;
import hackathon.nctucs.rollingpigs.elements.Slot;


public class MainActivity extends Activity {


    Map< Integer , Circle > circles = new HashMap<>();
    Map< Integer , Node >   nodes = new HashMap<>();
    Map< Integer , Slot >   slots = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int stage = getIntent().getIntExtra( "stage" , -1 );

        if ( stage == -1 ){ // load stage error
            Toast.makeText( getApplicationContext() , "load stage failed" , Toast.LENGTH_SHORT).show();
        }
        else{
            initStage( stage );

        }

    }


    private void initStage( int stage ) {


        // requires read file

        JSONObject stageInfo = new JSONObject();


        try {

            if (stageInfo.getString("type").equals("circle")) {



                Circle circle = new Circle(
                        stageInfo.getInt( "id" ),
                        stageInfo.getInt( "color" ),
                        stageInfo.getInt( "x" ),
                        stageInfo.getInt( "y" ),
                        stageInfo.getDouble("radius"),
                        stageInfo.getString("src")
                );

                circles.put( stageInfo.getInt( "id" ) , circle );


            }
            else if ( stageInfo.getString("type").equals("node") ){







            }
            else if ( stageInfo.getString("type").equals("slot") ){



                id: $id,
                        x:  $x,
                        y:  $y,
                        content:		$node_id



            }
        }
        catch( Exception e ){
            e.printStackTrace();
        }


    }




}

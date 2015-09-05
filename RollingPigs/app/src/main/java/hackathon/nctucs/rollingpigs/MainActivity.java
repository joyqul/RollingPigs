package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;

import hackathon.nctucs.rollingpigs.elements.Circle;


public class MainActivity extends Activity {


    Circle[] circles;
    Node[]   nodes;


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








    }




}

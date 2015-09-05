package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class MenuPage extends Activity {

    final int[] ids = new int[]{ 0 , R.id.stage1 , R.id.stage2 , R.id.stage3 };
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences = getSharedPreferences( "db" , MODE_PRIVATE );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.menupage );

        ImageView mute = (ImageView)findViewById( R.id.mute );
        sharedPreferences.edit().putBoolean( "mute" , false ).apply();

        mute.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedPreferences.edit().putBoolean( "mute" ,
                            !sharedPreferences.getBoolean( "mute" , false )

                    ).apply();
                }
            }

        );



        for ( int i = 1 ; i <= 3  ; i++ ){
            ImageView img = (ImageView)findViewById( ids[i] );
            img.setTag( i );
            img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuPage.this , MainActivity.class);
                        intent.putExtra( "stage" , (int)v.getTag() );
                        startActivity( intent );
                    }
                }

            );

        }



    }

    @Override
    protected void onResume() {
        super.onStart();

        if ( sharedPreferences.getBoolean( "mute" , false ) == true )
            return;

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping( true );

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
}

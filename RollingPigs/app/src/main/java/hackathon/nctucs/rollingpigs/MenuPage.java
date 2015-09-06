package hackathon.nctucs.rollingpigs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Shamrock on 2015/9/5.
 */
public class MenuPage extends Activity {
    // 1 11 16
    final int[] ids = new int[]{ 0 , R.id.stage1 , R.id.stage2 , R.id.stage3 , R.id.stage4 };
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;

    public int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.menupage );
        sharedPreferences = getSharedPreferences( "db" , MODE_PRIVATE );
        ImageView mute = (ImageView)findViewById( R.id.mute );
        sharedPreferences.edit().putBoolean( "mute" , false ).apply();


        if ( sharedPreferences.getInt( "stage" , -1 ) == -1 )
            sharedPreferences.edit().putInt( "stage" , 1 ).apply();


        mute.setOnClickListener(
            new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( sharedPreferences.getBoolean( "mute" , false ) ) {

                        sharedPreferences.edit().putBoolean("mute",  false   ).apply();
                        if ( !mediaPlayer.isPlaying() )
                            mediaPlayer.start();
                        ((ImageView)v).setImageResource( R.drawable.audio45 );
                    }
                    else{
                        sharedPreferences.edit().putBoolean("mute",  true   ).apply();
                        if ( mediaPlayer.isPlaying() )
                            mediaPlayer.pause();

                        ((ImageView)v).setImageResource( R.drawable.volume_control );
                    }
                }
            }

        );






    }


    private void reloadPage(){

        int lastStage = sharedPreferences.getInt( "stage" , 1 );
        lastStage = 30;
        for ( int i = 1 ; i <= 30  ; i++ ){
            ImageView img = (ImageView)findViewById( getResourceId( "stage"+i , "id" , getPackageName() ) );

            TextView txt = new TextView( this );
            txt.setTextSize( 50 );
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)img.getLayoutParams();

            lp.gravity = Gravity.CENTER;
            txt.setText( i+"" );
            txt.setLayoutParams( lp );

            if ( i <= lastStage ) {

                if ( i == 11 || i == 1 || i == 16 )
                    img.setImageResource( R.drawable.menu_story );

                else
                    img.setImageResource( R.drawable.menu_game );

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
            else{
                if ( i == 11 || i == 1 || i == 16 )
                    img.setImageResource( R.drawable.menu_story_not );

                else
                    img.setImageResource( R.drawable.menu_game_not );
            }


        }

    }

    @Override
    protected void onResume() {
        super.onStart();
        reloadPage();
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

package com.example.android.miwok;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
public class ColorsActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    //Handles Audio focus when playing Audio file
    private AudioManager mAudioManager ;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||focusChange ==  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback because your Audio Focus was
                        // temporarily stolen, but will be back soon.
                        // i.e. for a phone call

                        mMediaPlayer.pause();
                        //WHEN THE AUDIO FOCUS BACK, it'll be BETTER FOR THE USER TO HEAR
                        // THE WORD FROM THE BEGINNING OF THE AUDIO FILE
                        //INSTEAD OF JUST HEARING THE REMAINING OF THE WORD
                        mMediaPlayer.seekTo(0);
                    }
                    else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //AUDIOFOCUS_GAIN >>> It means we have regained Audio Focus and we can resume the playback

                        mMediaPlayer.start();
                    }



                    else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // AUDIOFOCUS_LOSS>>> It means we've lost the Audio Focus and
                        // STOP & cleanup resources
                        releaseMediaPlayer();

                    }
                }
            };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //create and setup AudioManager to request audio focus
        mAudioManager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("green","chokokki",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black","kululli",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white","kelelli",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        ListView listView = (ListView) findViewById(R.id.list);
        final WordAdapter myAdapter = new WordAdapter(this,words,R.color.category_colors);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = words.get(position);
                //request Audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //USE THE MUSIC STREAM
                        AudioManager.STREAM_MUSIC,
                        //REQUEST TEMPORARY AUDIO FOCUS
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //release the media player if it currently exists because we are about
                //to play a different sound file
                releaseMediaPlayer();

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //WE HAVE AUDIO FOCUS NOW
                    // create and setup {@link MediaPlayer} for the audio resource id associated with the current word
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getmAudioResourceId());
                    // start the audio file
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

            }
        });



    }
    @Override
    protected void onStop() {

        super.onStop();
        //when the activity is stopped,release the mediaplayer resources because we won't
        //be playing any more sounds.
        releaseMediaPlayer();
    }





    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            // Register whether or not we were granted Audio focus ,abandon it
            // and this is also unregistered OnAudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


package com.example.android.miwok;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
public class PhrasesActivity extends AppCompatActivity {
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
        words.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michәksәs?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.","kuchi achit",R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","әәnәs'aa?",R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.","hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.","әәnәm",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.","yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here.","әnni'nem",R.raw.phrase_come_here));

        ListView listView = (ListView) findViewById(R.id.list);
        WordAdapter myAdapter = new WordAdapter(this,words,R.color.category_phrases);
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
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //WE HAVE AUDIO FOCUS NOW
                    // create and setup {@link MediaPlayer} for the audio resource id associated with the current word
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getmAudioResourceId());
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

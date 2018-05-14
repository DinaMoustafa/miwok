package com.example.android.miwok;
/**
 * Created by DINA on 02/04/2018.
 */
public class Word {
    private  String mMiwokTranslation ;
    private  String mDefaultTranslation ;
    private int mImageResourceId=NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;
    private int mAudioResourceId;

    public Word(String defaultTranslation,String miwokTranslation,int audioResourceId){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }
    public Word(String defaultTranslation,String miwokTranslation,int imageResourceId,int audioResourceId ){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId =  imageResourceId;
        mAudioResourceId = audioResourceId;

    }

    public String getmMiwokTranslation(){

    return mMiwokTranslation;

    }
    public String getmDefaultTranslation(){

        return mDefaultTranslation ;

    }
    public int getmImageResourceId(){

        return mImageResourceId;

    }

    public boolean hasImage(){

        return mImageResourceId!= NO_IMAGE_PROVIDED;

    }
    public int getmAudioResourceId(){

        return mAudioResourceId ;


    }


    @Override
    public String toString() {

        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}

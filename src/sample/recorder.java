package sample;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Created by abedaigorou on 16/01/22.
 */
public class recorder
{
    public static final int bit=16;
    public static final int hz=8000;
    public static final int STEREO=2;

    private TargetDataLine target;
    private AudioInputStream stream;
    private byte[] voice=new byte[hz*bit/8*2];

    private boolean isRunning=true;
    recorder()
    {
        try {
            //オーディオフォーマットの指定
            AudioFormat af = new AudioFormat(hz,bit,STEREO,true,false);
            //ターゲットデータラインを取得
            DataLine.Info info =new DataLine.Info(TargetDataLine.class,af);
            target=(TargetDataLine)AudioSystem.getLine(info);
            //ターゲットデータラインを開く
            target.open(af);

            //音声入力スタート
            target.start();

            //入力ストリームを取得
            stream=new AudioInputStream(target);


        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while(true) {
                    if(!isRunning)
                        return;
                    try {
                        stream.read(voice, 0, voice.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public byte[] getVoice()
    {
        return voice;
    }

    public void stop()
    {
        isRunning=false;
        target.stop();
        target.close();
    }


}

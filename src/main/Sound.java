package main;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
    Clip musicClip;
    private static FloatControl musicVolumeControl;
    URL[] url = new URL[10];

    public Sound(){
        url[0] = getClass().getResource("/retro-arcade-game.wav");
        url[1] = getClass().getResource("/touch floor.wav");
        url[2] = getClass().getResource("/rotation.wav");
        url[3] = getClass().getResource("/game-over-arcade.wav");
        url[4] = getClass().getResource("/delete line.wav");
    }

    public void play(int i, boolean music){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            ais.close();

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (music) {
                gainControl.setValue(-15.0f); // música de fundo mais suave
            } else {
                gainControl.setValue(-5.0f);  // efeitos um pouco mais fortes
            }


            if(music){
                musicClip = clip;
                loop();
            } else {
                clip.start();
            }

            clip.addLineListener(event -> {
                if(event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch(Exception e){
            e.printStackTrace(); // coloque para ver o erro real
        }
    }

    public void loop(){
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(){
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
        }
    }
}

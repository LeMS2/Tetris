package main;
import java.net.URL;
import javax.sound.sampled.Clip;

public class Sound {
    Clip musicClip;
    URL url[] = new URL[10];

    public Sound(){
        url[0] = getClass().getResource("/res/white-labyrinth-active.mp3");
        url[1] = getClass().getResource("/touch floor.wav");
        url[2] = getClass().getResource("/rotation.wav");
        url[3] = getClass().getResource("/gameover.wav");
        url[4] = getClass().getResource("/delete line.wav");
    }
    public void play(int i, boolean music){

    }
}

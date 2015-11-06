package game.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import game.Game;

public class AudioClip {
	private Clip clip;
	private AudioInputStream ais;

	public AudioClip(String filepath) {
		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(Game.class.getResourceAsStream(filepath));
			clip.open(ais);
		} catch (Exception e) {
			clip = null;
		}
	}

	public void start() {
		if (clip != null) {
			clip.start();
		}
	}

	public void stop() {
		if (clip != null) {
			clip.stop();
		}
	}
}

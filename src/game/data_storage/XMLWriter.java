package game.data_storage;

import java.awt.geom.Point2D;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import game.character.Player;
import game.exception.FileFormatException;
import game.world.World;

/**
 * Writes a Game object to an xml as representation of the games current state
 * @author Nathan Cooper 300305932
 *
 */
public class XMLWriter {
	
	private BufferedOutputStream bufferStream;
	private FileOutputStream fileStream;
	private XMLEncoder encoder;
	
	/**
	 * Writes to a file from a given string
	 * @param fileName				appends .xml onto fileName if needed
	 * @throws FileNotFoundException
	 */
	public XMLWriter(String fileName) throws FileNotFoundException {
		if(!fileName.endsWith(".xml")){
			fileName = fileName.concat(".xml");
		}
		fileStream = new FileOutputStream(fileName);
		bufferStream = new BufferedOutputStream(fileStream);
		encoder = new XMLEncoder(bufferStream);
	}
	
	/**
	 * Writes to a given "xml" file
	 * @param file
	 * @throws FileNotFoundException
	 */
	public XMLWriter(File file) throws FileNotFoundException{
		if(file.toString().endsWith(".xml")){
			fileStream = new FileOutputStream(file);
			bufferStream = new BufferedOutputStream(fileStream);
			encoder = new XMLEncoder(bufferStream);
		}
		else{
			throw new FileFormatException("Invalid File type\nFile must be xml");
		}
	}
	
	/**
	 * Saves a copy of the game in its present state
	 * @param player	
	 * @param world
	 */
	public void writeGame(Player player, World world, Point2D.Float offSet){
		encoder.writeObject(player);
		encoder.writeObject(world);
		encoder.writeObject(offSet);
	}
	
	public void writeObject(Object obj){
		encoder.writeObject(obj);
	}
	
	/**
	 * Closes the encoder
	 */
	public void close(){
		encoder.close();
	}
}

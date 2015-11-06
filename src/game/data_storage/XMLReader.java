package game.data_storage;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import game.character.Player;
import game.exception.FileFormatException;
import game.world.World;

/**
 * Reads a Game object from an xml file
 * @author Nathan Cooper 300305932
 *
 */
public class XMLReader {

		private BufferedInputStream bufferStream;
		private FileInputStream fileStream;
		private XMLDecoder decoder;
		
		/**
		 * Creates an XMLReader from a string
		 * @param filename
		 * @throws FileNotFoundException
		 */
		public XMLReader(String fileName)throws FileNotFoundException {
			if(fileName.endsWith(".xml")){
				fileStream = new FileInputStream(fileName);
				bufferStream = new BufferedInputStream(fileStream);
				decoder = new XMLDecoder(bufferStream);
			}
			else{
				throw new FileFormatException("Invalid File type\nFile must be xml");
			}
		}
		
		/**
		 * Creates an XMLReader from a file
		 * @param file
		 * @throws FileNotFoundException
		 */
		public XMLReader(File file) throws FileNotFoundException{
			if(file.getName().endsWith(".xml")){	
				fileStream = new FileInputStream(file);
				bufferStream = new BufferedInputStream(fileStream);
				decoder = new XMLDecoder(bufferStream);
			}
			else{
				throw new FileFormatException("Invalid File type\nFile must be xml");
			}
		}
		
		/**
		 * Produces a "Player" from an xml file
		 * @return
		 */
		public Player readPlayer(){
			return (Player)decoder.readObject();
		}
		
		/**
		 * Produces a "World" aka game map from an xml file
		 * @return
		 */
		public World readWorld(){
			return (World)decoder.readObject();
		}
		
		/**
		 * Produces a 2dPoint from an xml file of the view offset
		 * @return
		 */
		public Point2D.Float readOffSet() {
			return (Point2D.Float)decoder.readObject();
		}
		
		public Object readObject(){
			return decoder.readObject();
		}
		
		/**
		 * Closes the decoder
		 */
		public void close(){
			decoder.close();
		}
}

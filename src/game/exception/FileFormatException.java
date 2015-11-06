package game.exception;

public class FileFormatException extends IllegalArgumentException{

	public FileFormatException(String string) {
		super(string);
		System.out.println(string);
	}
	
	public FileFormatException(){
		
	}
}

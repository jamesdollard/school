import java.util.ArrayList;

/**
 * Interface for command pattern
 * @author jamesdollard
 *
 */
public interface Command {

	/**
	 * Executes a command
	 * @return
	 */
	public ArrayList<Score> doCommand();
	
	/**
	 * Undoes the originally executed command
	 */
	public void undoCommand();

}

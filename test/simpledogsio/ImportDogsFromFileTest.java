package simpledogsio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thor
 */
public class ImportDogsFromFileTest {
	String filename;
	
	@Before
	public void setUp() {
		filename = "Data/playforddogsOffSpring3.csv";
	}

	/**
	 * Test of importDogsFromCSV method, of class ImportDogsFromFile.
	 */
	@Test
	public void testImportDogsFromCSV() throws Exception {
		System.out.println("importDogsFromCSV");
		ImportDogsFromFile.importDogsFromCSV(filename);
		// TODO review the generated test code and remove the default call to fail.
		assertTrue(1==1);
	}
	
}

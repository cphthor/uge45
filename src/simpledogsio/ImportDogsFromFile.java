package simpledogsio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author thor
 */
public class ImportDogsFromFile {
	
	
	private static final String[] breeds = {"Akita","Alaskan Malamute","American Bull Dog","American Bull Terrier","American Staffordshire Terrier Cross","American Staffordshire Terrier","Australian Cattle Dog","Australian Kelpie","Australian Shepherd","Australian Silky Terrier","Australian Terrier","Beagle","Bearded Collie","Belgian Shepherd Dog","Bichon Frise x","Bichon Frise","Border Collie","Boxer","British Bulldog","Bull Terrier","Bulldog","Bullmastiff","Cairn Terrier","Cavalier King Charles Spaniel","Chihuahua","Chinese Crested Dog","Cocker Spaniel","Collie (Rough Coat)","Collie (Rough)","Collie (Smooth)","Coolie","Corgi","Dachshund (Miniature)","Dachshund","Dalmation","Dobermann","Dogue De Bordeaux","English Springer Spaniel","Fox Terrier (Smooth) ","Fox Terrier (Smooth)","French Bulldog","German Shepherd Dog","German Shorthaired Pointer","Golden Retriever","Great Dane","Greyhound","Hungarian Vizsla","Irish Wolfhound","Italian Greyhound","Jack Russell Terrier","Japanese Spitz","Keeshond","King Charles Spaniel","Labrador Retriever","Lhasa Apso","Maltese","Maremma Sheepdog","Mastiff","Miniature Pinscher","Papillon","Pointer","Pomeranian","Poodle (Miniature)","Poodle (Toy)","Poodle","Pug","Rhodesian Ridgeback","Rottweiler","Samoyed","Schnauzer (Miniature)","Schnauzer","Scottish Terrier","Shar Pei","Shetland Sheepdog","Shih Tzu","Siberian Husky","Spaniel","St. Bernard","Staffordshire Bull Terrier","Tenterfield Terrier","Terrier","Tibetan Spaniel","Unknown","Weimaraner","West Highland White Terrier","Whippet"};
	// Dog(String name, String color, String breed, String name, String gender, String cityOrigin, int Mother, int Father)
	/*
	25702,Brown/Tan,Dobermann,RUFUS,Desexed Male,WATERLOO CORNER,0,0
	25703,Grey/Tan,Dobermann,E T,Desexed Male,WATERLOO CORNER,0,0
	26898,Blenheim,Spaniel,SAMO,Male,HILLIER,26901,26899
	26899,Blenheim,Spaniel,ANDY,Male,HILLIER,0,0
	26901,Blenheim,Spaniel,CORKY,Female,HILLIER,0,0
	26938,Black,Rottweiler,SPYDA,Desexed Female,WATERLOO CORNER,0,0
	*/
	public static void importDogsFromCSV(String filename) throws FileNotFoundException, IOException {
		
		
		int[] collectIDs = new int[3];
		int breedCounter = 0;
		int breedCounterMax = breeds.length  - 1;
		String prevF = "";
		String prevM = "";
		String prevP = "";
		String tmpBreed = breeds[breedCounter];
		int prevFid = 0;
		int prevMid = 0;
		int prevPid = 0;
		int tmpMid = 0;
		int tmpFid = 0;
		String filenameOut = "Data/output.sql";
		String sqlLine = "INSERT INTO DOGS (DogID,Color,Breed,DogName,Gender,CityOrigin,MotherID,FatherID) ";
		sqlLine += " VALUES(%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%d)";
		String line = "";
		String tmpString = "";
		File fh = new File(filename);
		File fhout = new File(filenameOut);
		FileWriter fw = new FileWriter(fhout);
		BufferedWriter bw = new BufferedWriter(fw);
		
		Scanner myScanner = new Scanner(fh);
		myScanner.nextLine();
		
		//26899,Blenheim,Spaniel,ANDY,Male,HILLIER,0,0
		while (myScanner.hasNextLine()) {
			breedCounter++;
			System.out.println(" ---- ");
			System.out.println(" ");
			tmpString = "";
			line = myScanner.nextLine();
			String[] myArr = line.split(",");
			try {
				tmpMid = Integer.parseInt(myArr[6]);
				tmpFid = Integer.parseInt(myArr[7]);
				
			} catch (Exception e) {
				tmpMid = 0;
				tmpFid = 0;
			}
			
			if (tmpMid == 0 || tmpFid == 0 ) {
				// then a potential parent
				if (tmpBreed.equals(myArr[2])) {
					switch (myArr[4]) {
						
						case "Male":
							// is there a female?
							if (!(prevF.length()>1)) {
								// then we have a match
								System.out.println("Match" + line);
								if (!(prevM.length()>1)){
									System.out.println("new mother");
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
									prevM = myArr[3];
									prevMid = Integer.parseInt(myArr[0]);
								} else {
									System.out.println("we have Male offfspring ");
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
									//we have an offspring
									prevFid = 0;
									prevMid = 0;
									prevF ="";
									prevM ="";
									tmpBreed = breeds[breedCounter%breedCounterMax];
									tmpBreed = myArr[2];
								}
								
							} else {
								System.out.println("Still looking for a not " + myArr[4]);
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
							}
							break;
						case "Female":
							// is there a male?
							if (!(prevM.length()>1)) {
								// then we have a match
								System.out.println("Match" + line);
								if (!(prevF.length()>1)) {
									System.out.println("new mother");
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
									prevF = myArr[3];
									prevFid = Integer.parseInt(myArr[0]);
								} else {
									//we have an offspring
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
									System.out.println("offspring");
									prevFid = 0;
									prevMid = 0;
									prevF ="";
									prevM ="";
									tmpBreed = breeds[breedCounter%breedCounterMax];
									tmpBreed = myArr[2];
								}
							} else {
								System.out.println("Still looking for a not " + myArr[4]);
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
							}
							break;
						default:
							System.out.println("Not a breed pot");
							tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
							break;
							
					}
				} else {
					//tmpBreed = breeds[breedCounter%breedCounterMax];
					tmpBreed = myArr[2];
					System.out.println("No breed avail .. just  print and new breed" + tmpBreed);
					tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],prevMid,prevFid);
				}
			} else if (tmpMid > 0 || tmpFid > 0 ) {
				tmpBreed = breeds[breedCounter%breedCounterMax];
				tmpBreed = myArr[2];
				System.out.println("is already a  puppet with  parents");
				tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],tmpMid,tmpFid);
			} else {
				System.out.println("is already a  puppet with  parents");
				tmpString = String.format(sqlLine, Integer.parseInt(myArr[0]),myArr[1],myArr[2],myArr[3],myArr[4],myArr[5],tmpMid,tmpFid);
			}
			
			tmpString += ";";
			bw.write(tmpString);
			bw.newLine();
			//System.out.println(tmpString);
			//System.out.println("Breed:" + tmpBreed);
		}
		
		bw.close();
	}
	
	
}

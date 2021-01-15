package ch.correvon.utils.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.FrameBodyTALB;
import org.farng.mp3.id3.FrameBodyTCON;
import org.farng.mp3.id3.FrameBodyTIT2;
import org.farng.mp3.id3.FrameBodyTPE1;
import org.farng.mp3.id3.FrameBodyTPE2;
import org.farng.mp3.id3.FrameBodyTRCK;
import org.farng.mp3.id3.FrameBodyTYER;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v1_1;
import org.farng.mp3.id3.ID3v2_4;
import org.farng.mp3.id3.ID3v2_4Frame;

public class TagHelper
{
	public class TagHelperResult
	{
		public TagHelperResult()
		{
			this.infos = new ArrayList<String>(500);
			this.errors = new ArrayList<String>(500);
		}
		
		public void append(TagHelperResult tagHelperResult)
		{
			this.infos.addAll(tagHelperResult.getInfos());
			this.errors.addAll(tagHelperResult.getErrors());
		}
		
		public void addInfo(String info)
		{
			this.infos.add(info);
		}
		
		public ArrayList<String> getInfos()
		{
			return this.infos;
		}
		
		public String getInfosString()
		{
			return StringHelper.convertListToString(this.infos, "\n");
		}
		
		public String getErrorsString()
		{
			return StringHelper.convertListToString(this.errors, "\n");
		}
		
		public void addError(String error)
		{
			this.errors.add(error);
		}
		
		public ArrayList<String> getErrors()
		{
			return this.errors;
		}
		
		public boolean isErrors()
		{
			return !this.errors.isEmpty();
		}
		
		private ArrayList<String> infos;
		private ArrayList<String> errors;
	}
	
	// TODO remplacer les syserr par des exception ou plutot des Resultat que renverrait la fonction
	// TODO écrire ailleurs que dans un JTextArea
	
	public TagHelper()
	{
		this(true, true, true, false, true, " - ", " - ");
	}
	
	public TagHelper(boolean id3v1, boolean id3v2, boolean deleteUnauthorized, boolean keepBackup, boolean debug, String albumSplit, String titleSplit)
	{
		this.id3v1 = id3v1;
		this.id3v2 = id3v2;
		this.deleteUnauthorized = deleteUnauthorized;
		this.keepBackup = keepBackup;
		this.debug = debug;
		this.albumSplit = albumSplit;
		this.titleSplit = titleSplit;
		
		this.taggedFiles = 0;
		this.deletedFiles = 0;
	}
	
	public TagHelperResult tagRoot(String path)
	{
		TagHelperResult result = new TagHelperResult();
		
		File musicDir = new File(path);
		if(!musicDir.isDirectory())
			return this.deleteFile(musicDir);
		
		for(String artist:musicDir.list())
			result.append(this.tagArtist(path, artist));
		return result;
	}
	
	public TagHelperResult tagArtist(String path, String artist)
	{
		TagHelperResult result = new TagHelperResult();

		File albumDir = new File(path + "\\" + artist);
		if(!albumDir.isDirectory())
			return this.deleteFile(albumDir);
		
		String album;
		String year;
		String[] splited;
		for(String dirName:albumDir.list())
		{
			splited = dirName.split(albumSplit, 2);
			if(splited.length < 2)
			{
				File file = new File(albumDir.getAbsolutePath()+"\\"+dirName);
				if(!file.isDirectory())
					result.append(this.deleteFile(file));
				else
				{
					String msg = "\"" + dirName + "\" dans " + "\"" + albumDir.getAbsolutePath() + "\"" + " n'est pas conforme à \"[ANNEE] - [NOM_ALBUM]\"";
					result.addError(msg);
					System.err.println(msg);
				}
				continue;
			}
			try
			{
				new Integer(splited[0]); // test uniquement si la date est num�rique, mais garde le string au cas o� elle commencerait par des '0'
			}
			catch(NumberFormatException e)
			{
				String msg = "year \"" + splited[0] + "\" dans " + "\"" + albumDir.getAbsolutePath() + "\" n'est pas un nombre";
				result.addError(msg);
				System.err.println(msg);
				continue;
			}
			year = splited[0];
			album = splited[1];
			
			result.append(this.tagAlbum(path, artist, year, album));
		}
		return result;
	}
	
	public TagHelperResult tagAlbum(String path, String artist, String year, String album)
	{
		TagHelperResult result = new TagHelperResult();

		File titleDir = new File(path + "\\" + artist + "\\" + year + albumSplit + album);
		
		String title;
		String trackNumber;
		String[] splited;
		File file;
		
		if(!titleDir.isDirectory())
			return this.deleteFile(titleDir);
		
		for(String fileName:titleDir.list())
		{
			file = new File(titleDir + "\\" + fileName);
			if(!file.getName().toLowerCase().trim().endsWith(".mp3"))
			{
				result.append(deleteFile(file));
				continue;
			}
			
			splited = fileName.split(titleSplit);
			if(splited.length < 2)
			{
				String msg = "\"" + fileName + "\" dans " + "\"" + titleDir.getAbsolutePath() + "\"" + " n'est pas conforme à \"[NUMERO_PISTE] - [TITRE]\"";
				result.addError(msg);
				System.err.println(msg);
				continue;
			}
			try
			{
				new Integer(splited[0]); // test uniquement si le num�ro de piste est num�rique, mais garde le string au cas o� il commencerait par des '0'
			}
			catch(NumberFormatException e)
			{
				String msg = "trackNumber \"" + splited[0] + "\" dans " + "\"" + titleDir.getAbsolutePath() + "\" n'est pas un nombre";
				result.addError(msg);
				System.err.println(msg);
				continue;
			}
			trackNumber = splited[0];
			title = StringHelper.removeExtension(splited[1]);

			result.append(this.tagFile(path, artist, year, album, trackNumber, title));
		}
		
		return result;
	}
	
	public TagHelperResult tagFile(String path, String artist, String year, String album, String trackNumber, String title)
	{
		TagHelperResult result = new TagHelperResult();

		String filePath = path + "\\" + artist + "\\" + year + albumSplit + album + "\\" + trackNumber + titleSplit + title + ".mp3" ;
		result.addInfo(filePath);
		if(this.debug)
		{
			result.addInfo("Artiste : "+artist);
			result.addInfo("Année : "+year);
			result.addInfo("Album : "+album);
			result.addInfo("N° : "+trackNumber);
			result.addInfo("Titre : "+title);
		}
		
	    if(!this.id3v1 && !this.id3v2) // Optimisation du temps : créer un mp3 file et le sauver inutilement prend des plombes
	    	return result;
	    
		year = new Integer(year).toString();
		trackNumber = new Integer(trackNumber).toString();
		try
		{
			MP3File mp3file = new MP3File(new File(filePath));

		    if(this.id3v1)
		    {
			    ID3v1 id3v1_1 = new ID3v1_1();
			    id3v1_1.setAlbum(album);
			    id3v1_1.setAlbumTitle(album);
			    id3v1_1.setArtist(artist);
			    id3v1_1.setComment("");
			    id3v1_1.setGenre((byte)17); // 17 = rock. see http://www.multimediasoft.com/amp3dj/help/index.html?amp3dj_00003e.htm
			    id3v1_1.setLeadArtist(artist);
			    id3v1_1.setSongComment("");
			    id3v1_1.setSongTitle(title);
			    id3v1_1.setTitle(title);
			    id3v1_1.setTrackNumberOnAlbum(trackNumber);
			    id3v1_1.setYear(year);
			    id3v1_1.setYearReleased(year);
			    mp3file.setID3v1Tag(id3v1_1);
		    }

		    if(this.id3v2)
		    {
			    ID3v2_4 id3v2_2 = new ID3v2_4(); // see http://www.id3.org/id3v2.3.0
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTIT1((byte)0, title)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTIT2((byte)0, title)));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTIT3((byte)0, title)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTRCK((byte)0, trackNumber)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTYER((byte)0, year)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTPE1((byte)0, artist)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTPE2((byte)0, artist)));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTPE3((byte)0, artist)));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTCOM((byte)0, artist)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTALB((byte)0, album)));
			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTCON((byte)0, "Rock")));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTDAT((byte)0, year)));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTPUB((byte)0, "")));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTCOP((byte)0, "")));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTENC((byte)0, "")));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyTEXT((byte)0, "")));
//			    id3v2_2.setFrame(new ID3v2_4Frame(new FrameBodyCOMM())); // plante
			    mp3file.setID3v2Tag(id3v2_2);
		    }
		    
		    mp3file.save(/*this.keepBackup*/); // TODO remettre keepBackup ? Je l'ai retiré car c'est incompatible quand on install la librairie jid3lib via maven
		    this.taggedFiles++;
		}
		catch(IOException ioe)
		{
			String msg = "IOException - Impossible de tagger "+filePath;
			System.err.println(msg);
			result.addError(msg);
		}
		catch(TagException te)
		{
			String msg = "TagException - Impossible de tagger "+filePath;
			System.err.println(msg);
			result.addError(msg);
		}
		catch(UnsupportedOperationException uoe)
		{
			String msg = "UnsupportedOperationException - Impossible de tagger "+filePath;
			System.err.println(msg);
			result.addError(msg);
		}
		
		return result;
	}
	
	private TagHelperResult deleteFile(File file)
	{
		TagHelperResult result = new TagHelperResult();
		if(this.deleteUnauthorized && file.exists() && file.isFile() && !file.getName().toLowerCase().trim().endsWith(".mp3"))
		{
			if(file.delete())
			{
				result.addInfo("Suppression de "+file.getAbsolutePath());
				this.deletedFiles++;
			}
			else
			{
				String msg = "Impossible de supprimer "+file.getAbsolutePath();
				result.addError(msg);
				System.err.println(msg);
			}
		}
		return result;
	}
	
	public boolean isId3v1()
	{
		return this.id3v1;
	}

	public void setId3v1(boolean id3v1)
	{
		this.id3v1 = id3v1;
	}

	public boolean isId3v2()
	{
		return this.id3v2;
	}

	public void setId3v2(boolean id3v2)
	{
		this.id3v2 = id3v2;
	}

	public boolean isDeleteUnauthorized()
	{
		return this.deleteUnauthorized;
	}

	public void setDeleteUnauthorized(boolean deleteUnauthorized)
	{
		this.deleteUnauthorized = deleteUnauthorized;
	}

	public String getAlbumSplit()
	{
		return this.albumSplit;
	}

	public void setAlbumSplit(String albumSplit)
	{
		this.albumSplit = albumSplit;
	}

	public String getTitleSplit()
	{
		return this.titleSplit;
	}

	public void setTitleSplit(String titleSplit)
	{
		this.titleSplit = titleSplit;
	}

	public boolean isKeepBackup()
	{
		return this.keepBackup;
	}

	public void setKeepBackup(boolean keepBackup)
	{
		this.keepBackup = keepBackup;
	}

	public boolean isDebug()
	{
		return this.debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public int getTaggedFiles()
	{
		return this.taggedFiles;
	}

	public void setTaggedFiles(int taggedFiles)
	{
		this.taggedFiles = taggedFiles;
	}

	public int getDeletedFiles()
	{
		return this.deletedFiles;
	}

	public void setDeletedFiles(int deletedFiles)
	{
		this.deletedFiles = deletedFiles;
	}
	
	private boolean id3v1;
	private boolean id3v2;
	private boolean deleteUnauthorized;
	private String albumSplit;
	private String titleSplit;
	private boolean keepBackup;
	private boolean debug;
	private int taggedFiles;
	private int deletedFiles;
}

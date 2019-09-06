package cloudfragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item2 {
    private File file;
    private String filename;
    private String filedate;

    public Item2(String filename,String filedate) {
        this.file=file;
       this.filename=filename;
       this.filedate=filedate;
    }

  public String getFilename()
  {
      return this.filename;
  }

  public String getFiledate()
  {
      return  this.filedate;
  }



}
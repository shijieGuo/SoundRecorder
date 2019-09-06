package saverecodingfragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item {
    private File file;
    private String filename;
    private String filetime;
    private String filedate;

    public Item(File file,String filetime){
        this.file=file;
        StringBuffer name=new StringBuffer(file.getName());
        name.delete(name.length()-4,name.length());
        this.filename=new String(name);
        this.filetime=filetime;
        this.filedate=new SimpleDateFormat("yyyy-MM-dd").format(new Date(file.lastModified()));
    }
    public String getFilename()
  {
      return this.filename;
  }
    public String getFiletime()
  {
      return  this.filetime;
  }
    public String getFiledate()
  {
      return  this.filedate;
  }
    public File getFile() { return  this.file; }
}
import com.sadakhata.spamsmsblocker.*;

public class SMSReader{
	public static void main(String args[])
	{
		SKSpamBlocker block = new SKSpamBlocker();
		block.init();
		System.out.println(block.isSpam("1", "আজ দুই দিনের প্যাক কিনলেই এমবি বোনাস। অ্যাকটিভ করতে ডায়াল করুন"));
		//System.out.println(block.isSpam("1", "jan"));
	}
}
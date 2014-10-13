import com.sadakhata.spamsmsblocker.*;

public class SMSReader{
	public static void main(String args[])
	{
		SKSpamBlocker block = new SKSpamBlocker();
		block.init();
		System.out.println(block.isSpam("1", "Your bKash Buy Airtime request of Tk 17.00 for 01820736109 was successful. "));
		//System.out.println(block.isSpam("1", "jan"));
	}
}
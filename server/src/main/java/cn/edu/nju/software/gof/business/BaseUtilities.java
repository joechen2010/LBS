package cn.edu.nju.software.gof.business;

import cn.edu.nju.software.manager.AccountManager;
import cn.edu.nju.software.manager.AuthAccessKeyManager;
import cn.edu.nju.software.manager.CheckInCounterManager;
import cn.edu.nju.software.manager.CheckInManager;
import cn.edu.nju.software.manager.FriendRequestManager;
import cn.edu.nju.software.manager.PersonManager;
import cn.edu.nju.software.manager.PersonalLocationManager;
import cn.edu.nju.software.manager.PlaceManager;
import cn.edu.nju.software.manager.ReplyManager;
import cn.edu.nju.software.manager.RichManManager;
import cn.edu.nju.software.manager.UserLocationManager;
import cn.edu.nju.software.util.SpringContextHolder;

public abstract class BaseUtilities {
	protected static final double RANGE = 0.05;
	protected AccountManager accountManager = SpringContextHolder.getBean("accountManager");
	protected PersonalLocationManager personalLocationManager = SpringContextHolder.getBean("personalLocationManager");
	protected PlaceManager placeManager = SpringContextHolder.getBean("placeManager");
	protected CheckInManager checkInManager = SpringContextHolder.getBean("checkInManager");
	protected CheckInCounterManager checkInCounterManager = SpringContextHolder.getBean("checkInCounterManager");
	protected PersonManager personManager = SpringContextHolder.getBean("personManager");
	protected RichManManager richManManager = SpringContextHolder.getBean("richManManager");
	protected ReplyManager replyManager = SpringContextHolder.getBean("replyManager");
	protected AuthAccessKeyManager authAccessKeyManager = SpringContextHolder.getBean("authAccessKeyManager");
	protected FriendRequestManager friendRequestManager = SpringContextHolder.getBean("friendRequestManager");	
	protected UserLocationManager userLocationManager = SpringContextHolder.getBean("userLocationManager");
	
	protected CommonUtilities commonUtilities = SpringContextHolder.getBean("commonUtilities");
	protected RichManUtilities richManUtilities = SpringContextHolder.getBean("richManUtilities");
	protected CheckInUtilities checkInUtilities = SpringContextHolder.getBean("checkInUtilities");
	protected FriendUtilities friendUtilities = SpringContextHolder.getBean("friendUtilities");
	protected PlaceUtilities placeUtilities = SpringContextHolder.getBean("placeUtilities");
	protected ProfileUtilities profileUtilities = SpringContextHolder.getBean("profileUtilities");
}

package cn.edu.nju.software.gof.processor;

import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.gof.servlet.ServletParam;

public class RequestProcessorFactory {

	private static Map<String, RequestProcessor> processors = new HashMap<String, RequestProcessor>();

	static {
		processors.put(ServletParam.CheckUser, new UserCheckExistedProcessor());
		processors.put(ServletParam.Register, new UserRegisterProcessor());
		processors.put(ServletParam.Login, new UserLoginProcessor());
		processors.put(ServletParam.ChangePassword, new UserChangePasswordProcessor());
		processors.put(ServletParam.ChangeState, new UserChangeStateProcessor());
		processors.put(ServletParam.GetPersonalProfile, new UserGetProfileProcessor());
		processors.put(ServletParam.SetPersonalProfile, new UserModifyProfileProcessor());
		processors.put(ServletParam.GetPersonalAvatar, new UserGetAvatarProcessor());
		processors.put(ServletParam.UploadAvatar, new UserModifyAvatarProcessor());
		processors.put(ServletParam.UpdateLocation, new UserUpdateLocationProcessor());
		processors.put(ServletParam.ShowNearbyFriend, new FriendFindNearbyProessor());
		processors.put(ServletParam.GetFriendList, new FriendListGetterProcessor());
		processors.put(ServletParam.GetFriendAvatar, new FriendAvatarGetterProcessor());
		processors.put(ServletParam.GetFriendProfile, new FriendProfileGetterProcessor());
		processors.put(ServletParam.RequestFriend, new FriendRequestProcessor());
		processors.put(ServletParam.GetFriendRequest, new FriendRequestGetterProcessor());
		processors.put(ServletParam.AgreeFriendRequest, new FriendRequestAgreeProcessor());
		processors.put(ServletParam.RejectFriendRequest, new FriendRequestRejectProcessor());
		processors.put(ServletParam.DeleteFriend, new FriendDeleteProcessor());
		processors.put(ServletParam.SearchFriend, new FriendSearchProcessor());
		processors.put(ServletParam.RecommandFriend, new FriendRecommandProcessor());
		processors.put(ServletParam.GetNearbyPlace, new PlaceGetNearbyProcessor());
		processors.put(ServletParam.GetPlaceInfo, new PlaceGetInfoProcessor());
		processors.put(ServletParam.CreatePlace, new PlaceCreationProcessor());
		processors.put(ServletParam.CommentPlace, new PlaceCommentProcessor());
		processors.put(ServletParam.CheckIn, new PlaceUserCheckInProcessor());
		processors.put(ServletParam.GetPersonlCheckIn, new UserGetCheckInProcessor());
		processors.put(ServletParam.GetFriendCheckIn, new FriendGetOnesCheckInProcessor());
		processors.put(ServletParam.SetSynchronization, new SynchronizationRequestProcessor());

		processors.put(ServletParam.GetRichmanInfo, new GetRichManInfoProcessor());
		processors.put(ServletParam.BuyExistedPlace, new BuyExistedPlaceProcessor());
		processors.put(ServletParam.GetPlaceGeneral, new GetPlaceGeneralProcessor());
		processors.put(ServletParam.GetPlaceReplies, new GetPlaceRepliesProcessor());
		processors.put(ServletParam.GetSubPlaces, new GetSubPlacesProcessor());
		processors.put(ServletParam.GetPersonlCheckIn, new GetPlaceCheckInProcessor());
		processors.put(ServletParam.GetPlaceImage, new GetPlaceImageProcessor());
		processors.put(ServletParam.USERLOCATION, new UserLocationProcessor());
	}

	public static RequestProcessor getProcessor(String processorName) {
		return processors.get(processorName);
	}
}

package cn.edu.nju.software.gof.requests;

public final class ServletParam {

	public static final String ProcessorName = "processor_name";

	public static final String CheckUser = "check_user";
	public static final String Register = "register";
	public static final String Login = "login";
	public static final String ChangePassword = "change_password";
	public static final String ChangeState = "change_state";
	public static final String GetPersonalProfile = "get_personal_profile";
	public static final String SetPersonalProfile = "set_personal_profile";
	public static final String GetPersonalAvatar = "get_personal_avatar";
	public static final String UploadAvatar = "upload_avatar";
	public static final String UpdateLocation = "update_location";
	public static final String ShowNearbyFriend = "show_nearby_friend";
	public static final String GetFriendList = "get_friend_list";
	public static final String GetFriendAvatar = "get_friend_avatar";
	public static final String GetFriendProfile = "get_friend_profile";
	public static final String RequestFriend = "request_friend";
	public static final String GetFriendRequest = "get_friend_request";
	public static final String AgreeFriendRequest = "agree_friend_request";
	public static final String RejectFriendRequest = "reject_friend_request";
	public static final String DeleteFriend = "delete_friend";
	public static final String SearchFriend = "search_friend";
	public static final String RecommandFriend = "recommand_friend";
	public static final String GetNearbyPlace = "get_nearby_place";
	public static final String GetPlaceInfo = "get_place_info";
	public static final String CreatePlace = "create_place";
	public static final String CommentPlace = "comment_place";
	public static final String CheckIn = "check_in";
	public static final String GetPersonlCheckIn = "get_personal_check_in";
	public static final String GetFriendCheckIn = "get_friend_check_in";
	public static final String SetSynchronization = "set_synchronization";
	public static final String GetRichmanInfo = "get_richman_info";
	public static final String BuyExistedPlace = "buy_existed_place";
	public static final String GetPlaceGeneral = "get_place_general";
	public static final String GetPlaceReplies = "get_place_replies";
	public static final String GetSubPlaces = "get_sub_places";
	public static final String GetPlaceCheckIn = "get_place_check_in";
	public static final String GetPlaceImage = "get_place_image";
	public static final String MODIFY_PLACE = "modify_place";
	public static final String GET_FRIEND_AVATAR_COUNTER = "get_friend_avatar_counter";
	public static final String GET_PLACE_IMAGE_COUNTER = "get_place_image_counter";
	public static final String GET_MY_TOP_CHECKINS = "get_my_top_checkins";

	public final class RequestParam {
		public static final String SessionID = "session_id";
		public static final String UserID = "user_id";
		public static final String FriendID = "friend_id";
		public static final String RequestID = "request_id";
		public static final String PlaceID = "place_id";
		public static final String ParentID = "parent_id";
		public static final String PLACE_DESCRIPTION = "place_description";

		public static final String UserName = "user_name";
		public static final String Password = "password";
		public static final String State = "state";

		public static final String RealName = "real_name";
		public static final String Place = "place";
		public static final String School = "school";
		public static final String Birthday = "birthday";

		public static final String OldPassword = "old_password";
		public static final String NewPassword = "new_password";

		public static final String PlaceName = "place_name";
		public static final String Latitude = "latitude";
		public static final String Longitude = "longitude";

		public static final String Image = "image";
		public static final String Content = "content";

		public static final String Provider = "provider";
	}

	public final class JsonParam {
		public static final String ListName = "results";
	}
}

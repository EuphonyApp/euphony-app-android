package euphony.com.euphony;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by pR0 on 28-05-2016.
 */
public interface UserApi {

    @GET("/id")
    Call<ResponseBody> getId(@Query("f_id") String f_id, @Query("g_id") String g_id);

    @POST("/artist")
    Call<ResponseBody> createArtist(@Body Artist artist);

    @GET("/artist")
    Call<Artist> getArtist(@Query("id") String id);

    @POST("/venue")
    Call<ResponseBody> createVenue(@Body Venue venue);

    @GET("/venue")
    Call<Venue> getVenue(@Query("id") String id);

    @POST("/band")
    Call<ResponseBody> createBand(@Body Band band);

    @GET("/band")
    Call<Band> getBand(@Query("id") String id);

    @POST("/artists")
    Call<ResponseBody> artists(@Body List<Artist> artists);

    @GET("/artists/name")
    Call<List<UserMin>> getArtistByName(@Query("name") String name, @Query("cur_id") String cur_id);

    @GET("/user/all")
    Call<List<User>> getAll(@Query("cur_id") String curId);

    @GET("/usersInPart")
    Call<List<User>> getUsersInPart(@Query("cur_id") String curId, @Query("total") int total, @Query("offset") int offset, @Query("type") String type);

    @GET("/user/ids")
    Call<List<UserMin>> getSomeUsers(@Query("ids") List<String> userIds, @Query("cur_id") String curId);

    @POST("/follow")
    Call<ResponseBody> follow(@Query("follow") String follow, @Query("cur_user") String curUser);

    @POST("/unfollow")
    Call<ResponseBody> unfollow(@Query("unfollow") String unfollow, @Query("cur_user") String curUser);

    @GET("/band/list")
    Call<List<Band>> getBandList(@Query("cur_id") String cur_id);

    @POST("/band/add/artist")
    Call<ResponseBody> addMember(@Query("id") String id, @Query("band_id") String band_id);

    @GET("artist/near")
    Call<List<Artist>> getNearArtist(@Query("latitude") Double latitude, @Query("longitude") Double longitude, @Query("cur_id") String id);

    @GET("/chat_details")
    Call<List<ChatDetails>> getConversation(@Query("id") String id);

    @GET("/conversation/id")
    Call<List<Message>> getMessages(@Query("cur_id") String cur_id, @Query("id") String id);

    @POST("/message")
    Call<ResponseBody> saveMessage(@Query("read") String read, @Body Message message);

    @GET("/bands/city")
    Call<List<Band>> getBands(@Query("city") String city);

    @GET("/followOrNot")
    Call<ResponseBody> getFollowOrNot(@Query("cur_id") String cur_id, @Query("id") String id);

    @GET("/bookedOrNot")
    Call<ResponseBody> getBookedOrNot(@Query("cur_id") String cur_id, @Query("id") String id);

    @GET("/jammingOrNot")
    Call<ResponseBody> getJammingOrNot(@Query("cur_id") String cur_id, @Query("id") String id);

    @GET("/all/artists")
    Call<List<Artist>> getAllArtists(@Query("id") String id);

    @POST("/update/artist")
    Call<ResponseBody> updateArtist(@Body Artist artist);

    @POST("/update/venue")
    Call<ResponseBody> updateVenue(@Body Venue venue);

    @POST("/update/band")
    Call<ResponseBody> updateBand(@Body Band band);

    @POST("/stop/tracking")
    Call<ResponseBody> stopTracking(@Query("id") String id);

    @POST("/update/location")
    Call<ResponseBody> updateLocation(@Query("id") String id, @Query("longitude") Float longitude, @Query("latitude") Float latitude);

    @GET("/followers")
    Call<List<UserMin>> getFollowers(@Query("cur_id") String cur_id);

    @GET("/following")
    Call<List<UserMin>> getFollowing(@Query("cur_id") String cur_id);

    @POST("/book/band")
    Call<ResponseBody> bookBand(@Query("band_id") String bandId, @Query("venue_id") String venueId, @Query("date") String date,
                                @Query("time") String time);

    @POST("/book/artist")
    Call<ResponseBody> bookArtist(@Query("artist_id") String artistId, @Query("venue_id") String venueId, @Query("date") String date,
                                @Query("time") String time);

    @GET("/booking/details/artist")
    Call<List<Booking>> getBookingsArtist(@Query("id") String id);

    @GET("/booking/details/venue")
    Call<List<Booking>> getBookingsVenue(@Query("id") String id);

    @POST("/jam/send")
    Call<ResponseBody> sendJamRequest(@Query("sender_id") String sender, @Query("sent_to_id") String sentTo, @Query("date") String date,
                                      @Query("time") String time);

    @POST("/jam/cancel")
    Call<ResponseBody> cancelJam(@Query("sender_id") String sender, @Query("sent_to_id") String sentTo);

    @POST("/jam/rejected")
    Call<ResponseBody> rejectJam(@Query("notify_id") String notification_id);

    @POST("/jam/accepted")
    Call<ResponseBody> acceptJam(@Query("notify_id") String notification_id);

    @POST("/booking/accepted")
    Call<ResponseBody> acceptBooking(@Query("notify_id") String notification_id);

    @POST("/booking/rejected")
    Call<ResponseBody> rejectBooking(@Query("notify_id") String notification_id);

    @POST("/booking/cancel")
    Call<ResponseBody> cancelBooking(@Query("booked_id") String booked, @Query("bookedBy_id") String bookedBy);

    @GET("/notifications")
    Call<List<Notifications>> getNotification(@Query("id") String id);

    @POST("/update/gcm")
    Call<ResponseBody> updateGcm(@Query("id") String id, @Query("gcm_token") String token);

    @POST("/band/members")
    Call<List<Artist>> getMembers(@Query("id") String id);

    @Multipart
    @POST("/upload_profile_picture")
    Call<ResponseBody> uploadProfilePicture(@Part("photo") MultipartBody.Part file);

    @POST("/unread")
    Call<Integer> getUnread(@Query("id") String id);

}
package com.google.firebase.quickstart.auth.kotlin


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import io.reactivex.Observable

interface  FunlootApi {
    /*    @GET("/posts/{id}")
    public Call<FunlootUser> getPostWithID(@Path("id") int id);

    @GET("/posts")
    public Call<List<FunlootUser>> getAllPosts();

    @GET("/posts")
    public Call<List<FunlootUser>> getPostOfUser(@Query("userId") int id);*/
//
    @GET("/api/objects")
    fun getTest(@Header("idtoken") idtoken: String): Observable<List<ObjectFunLoot>>

    @POST("/api/score")
    fun postScore(@Header("idtoken") idtoken: String, @Body body: ScoreFunLoot): Observable<List<ScoreFunLoot>>

    @GET("/api/news")
    fun getNews(@Header("idtoken") idtoken: String): Observable<List<NewsFunLoot>>

//
//    @GET("/api/user")
//    fun getUser(@Header("idtoken") idtoken: String): Call<FunlootUser>
//
//    @POST("/api/user")
//    fun postUser(@Header("idToken") idToken: String, @Body body: FunlootUser): Call<FunlootUser>
//
//    @POST("/api/train")
//    fun postTrain(@Header("idToken") idToken: String, @Body body: TrainBody): Observable<TrainBody>

}
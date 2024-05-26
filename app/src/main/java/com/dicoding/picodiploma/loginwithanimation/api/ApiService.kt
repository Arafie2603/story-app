package com.dicoding.picodiploma.loginwithanimation.api

//
//interface ApiService {
//    @GET("stories")
//    suspend fun getStories(): StoryResponse
//    @GET("stories/{id}")
//    suspend fun getDetailStory(@Path("id") id: String): DetailStoryResponse
//    @Multipart
//    @POST("stories")
//    suspend fun uploadImage(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") lat: Double ?= null,
//        @Part("lon") lon: Double ?=null
//    ) : FileUploadResponse
//    @GET("stories")
//    suspend fun getStoriesWithLocation(
//        @Query("location") location : Int = 1,
//    ): StoryResponse
//    @GET("stories")
//    suspend fun getStories(
//        @Query("page") page: Int = 1,
//        @Query("size") size: Int = 20
//    ): StoryResponse
//}
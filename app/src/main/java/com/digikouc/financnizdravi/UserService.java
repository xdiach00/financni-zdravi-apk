package com.digikouc.financnizdravi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface UserService {

    @POST("api/user/authenticate")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/user/create")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    @POST("api/revenue/get")
    Call<DataResponse> enterData(@Body DataRequest dataRequest);

    @POST("api/necessary-expenses/get")
    Call<DataResponse> enterData2(@Body DataRequest dataRequest);

    @POST("api/unnecessary-expenses/get")
    Call<DataResponse> enterData3(@Body DataRequest dataRequest);

    @POST("api/saving/get")
    Call<DataResponse> enterData4(@Body DataRequest dataRequest);

    @POST("api/revenue/set")
    Call<DataResponse> setData(@Body DataSetRequest dataSetRequest);

    @POST("api/necessary-expenses/set")
    Call<DataResponse> setData2(@Body DataSetRequest dataSetRequest);

    @POST("api/unnecessary-expenses/set")
    Call<DataResponse> setData3(@Body DataSetRequest dataSetRequest);

    @POST("api/saving/set")
    Call<DataResponse> setData4(@Body DataSetRequest dataSetRequest);

    @POST("api/user/password/change")
    Call<SettingsResponse> changePass(@Body PassChangeRequest passChangeRequest);

    @POST("api/user/settings/set")
    Call<DataResponse> changeSettings(@Body SettingsRequest settingsRequest);

    @POST("api/user/settings/get")
    Call<DataResponse> showSettings(@Body SettingsRequest settingsRequest);

    @POST("api/user/financial/data/get")
    Call<DataResponse> getData(@Body DataRequest dataRequest);

    @POST("api/user/forgotten-password")
    Call<DataResponse> resetPass(@Body PassResetRequest passResetRequest);

    @POST("api/user/activate")
    Call<DataResponse> activateUser(@Body ActivateUserRequest activateUserRequest);

    @POST("api/user/resend-activation")
    Call<DataResponse> resendActivation(@Body EmailResendRequest emailResendRequest);

    @POST("api/user/is-email-available")
    Call<EmailAvailableResponse> checkEmail(@Body PassResetRequest passResetRequest);

    @HTTP(method = "DELETE", path = "api/user/remove", hasBody = true)
    Call<SettingsResponse> deleteAccount(@Body DeleteAccountRequest deleteAccountRequest);


}

package com.hz.junxinbaoan.api;

import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.result.CodeResult;
import com.hz.junxinbaoan.result.UserInfoResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApi {
    @FormUrlEncoded
    @POST(Constants.VCODE)
    Call<CodeResult> getData(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<UserInfoResult> getUserInfo(@FieldMap Map<String, Object> map);
}

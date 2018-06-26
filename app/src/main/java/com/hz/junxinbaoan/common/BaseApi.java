package com.hz.junxinbaoan.common;

import com.hz.junxinbaoan.common.Constants;
import com.hz.junxinbaoan.result.CodeResult;
import com.hz.junxinbaoan.result.GetStatisticsResult;
import com.hz.junxinbaoan.result.MainPageResult;
import com.hz.junxinbaoan.result.RolePermissionResult;
import com.hz.junxinbaoan.result.UserInfoResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApi {
    @FormUrlEncoded
    @POST(Constants.VCODE)
    Call<CodeResult> getVCode(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<UserInfoResult> getUserInfo(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<RolePermissionResult> getRoles(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<GetStatisticsResult> getTop(@FieldMap Map<String, Object> map);

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<MainPageResult> getSquareData(@FieldMap Map<String, Object> map);
}

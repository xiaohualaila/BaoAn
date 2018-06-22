package com.hz.junxinbaoan.common;

import com.hz.junxinbaoan.result.CodeResult;
import com.hz.junxinbaoan.result.GetSignDetailResult;
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
    Call<CodeResult> getVCode(@FieldMap Map<String, Object> map);  //获取版本信息

    @FormUrlEncoded
    @POST(Constants.GETUSERINFO)
    Call<UserInfoResult> getUserInfo(@FieldMap Map<String, Object> map);  //获取个人信息接口

    @FormUrlEncoded
    @POST(Constants.MAINPAGEDATA)
    Call<MainPageResult> getSquareData(@FieldMap Map<String, Object> map); //获取四个方块的数据

    @FormUrlEncoded
    @POST(Constants.ATTENDACNE_STATISTICS)
    Call<GetStatisticsResult> getTop(@FieldMap Map<String, Object> map);//工作时间

    @FormUrlEncoded
    @POST(Constants.ROLE_PERMISSION)
    Call<RolePermissionResult> getRoles(@FieldMap Map<String, Object> map);//获取角色权限

    @FormUrlEncoded
    @POST(Constants.ATTENDACNE_DETAIL)
    Call<GetSignDetailResult> getDetail(@FieldMap Map<String, Object> map);
}

package com.codbking.calendar;


public class CalendarBean {

    public int year;
    public int moth;
    public int day;
    public int week;

    //-1,0,1
    public int mothFlag;

    //显示
    public String chinaMonth;
    public String chinaDay;

    public String empSchScheduleShortName;//班次简称
    public String empSchScheduleColor;//班次颜色

    public boolean isChooseDay;


    public CalendarBean(int year, int moth, int day) {
        this.year = year;
        this.moth = moth;
        this.day = day;
    }

    public String getDisplayWeek(){
        String s="";
         switch(week){
             case 1:
                 s="周日";
          break;
             case 2:
                 s="周一";
          break;
             case 3:
                 s="周二";
                 break;
             case 4:
                 s="周三";
                 break;
             case 5:
                 s="周四";
                 break;
             case 6:
                 s="周五";
                 break;
             case 7:
                 s="周六";
                 break;

         }
        return s ;
    }

    public String getParams(){
        StringBuffer stringBuffer = new StringBuffer(year+"-");
        if (moth < 10){
            stringBuffer.append("0");
        }
        stringBuffer.append(moth);
        stringBuffer.append("-");
        if (day < 10){
            stringBuffer.append("0");
        }
        stringBuffer.append(day);
        return stringBuffer.toString();
    }


    @Override
    public String toString() {
//        String s=year+"/"+moth+"/"+day+"\t"+getDisplayWeek()+"\t农历"+":"+chinaMonth+"/"+chinaDay;
        String s=year+"/"+moth+"/"+day;
        return s;
    }
}
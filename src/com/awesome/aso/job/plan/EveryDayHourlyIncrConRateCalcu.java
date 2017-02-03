package com.awesome.aso.job.plan;

public class EveryDayHourlyIncrConRateCalcu {

    // 一天中每小时刷单比例
    private final float[] DAY_HOURS_RATIO = {//
    0.01f, 0.01f, 0.01f, 0.01f, //
            0.01f, 0.01f, 0.02f, 0.03f, //
            0.04f, 0.04f, 0.05f, 0.05f, //
            0.05f, 0.04f, 0.04f, 0.04f, //
            0.04f, 0.07f, 0.09f, 0.08f, //
            0.09f, 0.08f, 0.06f, 0.03f //
    };
    // 每天每小时要刷的数量。
    private final int[][] mEveryDayHourlyNumber;
    // 增长率
    private final float mIncreasing;
    // 加上转换率，实际要下载的量。
    private final int mRealNumber;
    // 转换率
    private final float mConversionRate;
    // 执行天数
    private final int mDays;

    public EveryDayHourlyIncrConRateCalcu(int number, int days, float increasing, float conversionRate) {
        this.mIncreasing = increasing;
        this.mConversionRate = conversionRate;
        this.mDays = days;
        this.mRealNumber = (int) (number + Math.ceil(number * (1 - conversionRate)));
        this.mEveryDayHourlyNumber = new int[days][24];
        //
        calculateEveryDayHourlyNumber();
    }

    // 计算每天每小时要数量
    private void calculateEveryDayHourlyNumber() {
        for (int dayIndex = 0; dayIndex < mEveryDayHourlyNumber.length; dayIndex++) {
            for (int hourseIndex = 0; hourseIndex < mEveryDayHourlyNumber[dayIndex].length; hourseIndex++) {
                if (dayIndex == 0) {
                    mEveryDayHourlyNumber[dayIndex][hourseIndex] = (int) Math.ceil(mRealNumber * DAY_HOURS_RATIO[hourseIndex]);
                } else {
                    int perDayCurrHourCount = mEveryDayHourlyNumber[dayIndex - 1][hourseIndex];
                    int increasingCount = Math.round(perDayCurrHourCount * mIncreasing);
                    mEveryDayHourlyNumber[dayIndex][hourseIndex] = perDayCurrHourCount + increasingCount;
                }
            }
        }
    }

    /**
     * 第几天第几点的数量。
     * 
     * @param day
     *            第几天
     * @param hours
     *            几点钟， 24小时制。
     * @return
     */
    public int getNumberByDayHours(int day, int hours) {
        if (day >= mEveryDayHourlyNumber.length || hours > 24) {
            return -1;
        }
        return mEveryDayHourlyNumber[day][hours];
    }

    /**
     * 获取所有总的数量。
     * 
     * @return
     */
    public int getAllCount() {
        int allCount = 0;
        for (int i = 0; i < mEveryDayHourlyNumber.length; i++) {
            for (int j = 0; j < mEveryDayHourlyNumber[i].length; j++) {
                allCount += mEveryDayHourlyNumber[i][j];
            }
        }
        return allCount;
    }

    /**
     * 转化率
     * 
     * @return
     */
    public float getConversionRate() {
        return mConversionRate;
    }

    /**
     * 总的执行次数。（按小时）
     * 
     * @return
     */
    public int getRunCycle() {
        return mDays * 24;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int dayIndex = 0; dayIndex < mEveryDayHourlyNumber.length; dayIndex++) {
            sb.append("第" + (dayIndex + 1) + "天").append(":").append("\n");
            for (int hourseIndex = 0; hourseIndex < mEveryDayHourlyNumber[dayIndex].length; hourseIndex++) {
                sb.append(mEveryDayHourlyNumber[dayIndex][hourseIndex]);
                if (hourseIndex != mEveryDayHourlyNumber[dayIndex].length - 1) {
                    sb.append(",");
                } else {
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

}

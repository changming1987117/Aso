package com.awesome.aso.job.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils {

    public static int randomInterval(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static final long ONE_HOURS = 60 * 60 * 1000;

    // 当前整点时间 （小时）
    public static int getCurrHours() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    //
    public static Date getHoursPlus(int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, number);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date firstTime = calendar.getTime();
        return firstTime;
    }

    public static Date getSecondPlus(int number) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, number);
        Date firstTime = calendar.getTime();
        return firstTime;
    }

    public static String findFileName(String downloadUrl) {
        Pattern pattern = Pattern.compile("([^\\/]*)\\?");
        Matcher matcher = pattern.matcher(downloadUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            String[] path = downloadUrl.split("\\/");
            return path[path.length - 1];
        }
    }

    //
    public static <T> List<T> shuffleSublist(List<T> list, int number) {
        java.util.Collections.shuffle(list);
        return list.subList(0, number);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValueWithDefault(Map<?, ?> map, Object key, T def) {
        return map.get(key) == null ? def : (T) map.get(key);
    }

    /**
     * 随机指定范围内N个不重复的数 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换 然后从len-2里随机产生下一个随机数，如此类推
     * 
     * @param max
     *            指定范围最大值
     * @param min
     *            指定范围最小值
     * @param n
     *            随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;

        if (max < min || n > len) {
            return null;
        }

        // 初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            // 待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            // 将随机到的数放入结果集
            result[i] = source[index];
            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
}

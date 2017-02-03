package com.awesome.aso.job;

public class ASOManger {

    /**
     * 问题1：系统版本号有可能导致搜索不到应用；
     */

    public static void main(String[] args) throws Exception {
        String configPath = "tt.bsh";
        if (args.length == 0) {
            System.out.println("pls input beanshell file path");
            System.exit(1);
            return;
        } else if (args.length == 1) {
            configPath = args[0];
        }
//         初始化beanshell引擎
        ASOShellEngine.initEngine(configPath);
        //
        ASOShellEngine.getInstance().getTaskPlan().start();
    }
}

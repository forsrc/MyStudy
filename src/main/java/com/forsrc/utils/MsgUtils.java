package com.forsrc.utils;

import java.io.File;
import java.io.IOException;

public class MsgUtils {

    public static final String TYPE_DEBUG = "DEBUG";
    public static final String TYPE_INFO = "INFO";
    public static final String TYPE_IGNORE = "IGNORE";
    public static final String TYPE_FAILED = "FAILED";
    public static final String TYPE_SUCCESS = "SUCCESS";
    public static final String TYPE_WARNING = "WARNING";
    public static final String TYPE_ERROR = "ERROR";

    public static String getMsg(String type, String msg, int index) {
        return getMsg(type, msg, index, true);
    }

    public static String getMsg(String type, String msg, int index, boolean flag) {

        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        sb.append(DateTimeUtils.getDateTime()).append(" ");
        sb.append(type).append(" ");
        sb.append(msg);

        index = index >= ste.length ? ste.length - 1 : index;
        index = index < 0 ? ste.length + index : index;
        index = index < 0 ? 0 : index;

        if (flag) {
            sb.append("     @ ");
            sb.append(ste[index < ste.length ? index : (ste.length - 1)]
                    .toString());
            sb.append(" ")
                    .append(Thread.currentThread().toString())
                    .append((Thread.currentThread().isDaemon() ? "" : " Daemon"));
            // sb.append(msg);
        }
        return sb.toString();
    }

    public static void printDebugMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        String env = System.getenv("PRINT_MSG");
        if (env == null
                || (env.toUpperCase().indexOf("ALL") < 0 && env.toUpperCase()
                .indexOf(TYPE_DEBUG) < 0)) {
            return;
        }
        printMsg(String.format("%1$-7S", TYPE_DEBUG), msg, 4, true);
    }

    public static void printErrorMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);

        printMsg(String.format("%1$-7S", TYPE_ERROR), msg, 4, true);
    }

    public static void printErrorMsg(Exception exception) {
        // return getMsg("ERROR      ", msg, 4);
        StackTraceElement[] trace = exception.getStackTrace();
        printMsg(String.format("%1$-7S", TYPE_ERROR), exception.toString(), 4,
                true);
        for (int i = 0; i < trace.length; i++) {
            // ConsoleColorUtils.printRed("\t\t\t\tat " + trace[i] + "\n");
            System.err.print("\t\t\t\tat " + trace[i] + "\n");
        }
        Throwable ourCause = exception.getCause();
        if (ourCause != null) {
            printErrorMsgTraceAsCause(ourCause, trace);
        }
    }

    public static void printErrorMsgTraceAsCause(Throwable throwable,
                                                 StackTraceElement[] causedTrace) {
        // assert Thread.holdsLock(s);

        // Compute number of frames in common between this and caused
        StackTraceElement[] trace = throwable.getStackTrace();
        int m = trace.length - 1, n = causedTrace.length - 1;
        while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
            m--;
            n--;
        }
        int framesInCommon = trace.length - 1 - m;
        // ConsoleColorUtils.printRed(new
        // StringBuffer().append("\t\t\t\tCaused by: ")
        // .append(throwable).toString());
        System.err.println(new StringBuffer().append("\t\t\t\tCaused by: ")
                .append(throwable).toString());
        // System.out.println();

        for (int i = 0; i <= m; i++) {
            // ConsoleColorUtils.printRed(new
            // StringBuffer().append("\n\t\t\t\tat " + trace[i])
            // .toString());
            System.err.println(new StringBuffer().append(
                    "\n\t\t\t\tat " + trace[i]).toString());
            // System.out.println();
        }
        if (framesInCommon != 0)
            // ConsoleColorUtils.printRed(new
            // StringBuffer().append("\n\t\t\t\t... ")
            // .append(framesInCommon).append(" more\n").toString());
            System.err.println(new StringBuffer().append("\n\t\t\t\t... ")
                    .append(framesInCommon).append(" more\n").toString());
        // System.out.println();
        // Recurse if we have a cause
        Throwable ourCause = throwable.getCause();
        if (ourCause != null)
            printErrorMsgTraceAsCause(ourCause, trace);
    }

    public static void printIgnoreMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        printMsg(String.format("%1$-7S", TYPE_IGNORE), msg, 4, true);
    }

    public static void printInfo(String msg) {
        printMsg(String.format("%1$-7S", TYPE_INFO), msg, 4, false, false);
    }

    public static void printInfoMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        printMsg(String.format("%1$-7S", TYPE_INFO), msg, 4, true);
    }

    public static void printFailedMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        printMsg(String.format("%1$-7S", TYPE_FAILED), msg, 4, true);
    }

    public static void printMsg(String msg) {
        printMsg(TYPE_INFO, msg, 4, false, false);
    }

    public static void printMsg(String type, String msg) {
        printMsg(String.format("%1$-7S", type), msg, 4, true);
    }

    public static void printMsg(String type, String msg, int index,
                                boolean printTime, boolean printSrc) {
        String env = System.getenv("PRINT_MSG");
        if (env == null) {
            env = ".*";
        }
        if (env.toUpperCase().indexOf("ALL") < 0 && !type.trim().matches(env)) {
            return;
        }
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        if (printTime) {
            // System.out.print(DateTimeUtils.getDateTime());
            // System.out.print(" ");
            sb.append(DateTimeUtils.getDateTime()).append(" ");
            sb.append(type).append(" ");
            // ConsoleColorUtils.printGreen(type);
        }
        try {
            // Method method = ConsoleColorUtils.class
            // .getMethod(
            // TYPE_DEBUG.equals(type.trim()) ? "printDarkGary" : TYPE_INFO
            // .equals(type.trim()) ? "printDef" : TYPE_IGNORE.equals(type
            // .trim()) ? "printMagenta"
            // : TYPE_SUCCESS.equals(type.trim()) ? "printGreen" : TYPE_ERROR
            // .equals(type.trim()) ? "printRed" : TYPE_WARNING
            // .equals(type.trim()) ? "printYellow" : "printLightCyan",
            // String.class);
            // method.invoke(new ConsoleColorUtils(), new
            // StringBuffer().append(type).append(" ")
            // .append(msg).toString());
            // System.out.print(new StringBuffer().append(type).append(" ")
            // .append(msg).toString());
            sb.append(msg);
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.print(type);
            // sb.append(type);
        }
        // System.out.print(" ");
        // System.out.print(msg);

        if (printSrc) {
            index = index >= ste.length ? ste.length - 1 : index;
            index = index < 0 ? ste.length + index : index;
            index = index < 0 ? 0 : index;
            sb.append("     @ ");
            sb.append(ste[index < ste.length ? index : (ste.length - 1)]
                    .toString());
            // sb.append(msg);

            // ConsoleColorUtils.printDarkGary("     @ "
            // + ste[index < ste.length ? index : (ste.length - 1)].toString());
            sb.append(Thread.currentThread().toString()).append(
                    Thread.currentThread().isDaemon() ? " Daemon" : "");
            sb.append("\n");

            // ConsoleColorUtils.printDarkGary(" " +
            // Thread.currentThread().toString()
            // + (Thread.currentThread().isDaemon() ? " Daemon" : ""));
            // System.out.print(" " + Thread.currentThread().toString()
            // + (Thread.currentThread().isDaemon() ? " Daemon" : ""));
        }
        System.out.print(sb.toString());
        // System.out.println();
        String saveLogEnv = System.getenv("SAVE_MSG_PATH");
        if (saveLogEnv != null) {
            if (saveLogEnv.indexOf("%LOG_YYYYMMDD%") >= 0) {
                saveLogEnv = saveLogEnv.replace("%LOG_YYYYMMDD%",
                        DateTimeUtils.getDateTime("yyyyMMdd"));
            }
            File file = new File(saveLogEnv);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    return;
                }
            }
            try {
                FileUtils.setFileTxt(file, sb.toString(), true);
            } catch (IOException e) {
            }
        }
    }

    public static void printMsg(String type, String msg, int index, boolean flag) {
        printMsg(type, msg, index, true, flag);
    }

    public static void printSuccessMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        printMsg(String.format("%1$-7S", TYPE_SUCCESS), msg, 4, true);
    }

    public static void printWarningMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        printMsg(String.format("%1$-7S", TYPE_WARNING), msg, 4, true);
    }

    public static String toDebugMsg(String msg) {
        // return getMsg("IGNORE    ", msg, 4);
        return toMsg(String.format("%1$-7S", TYPE_DEBUG), msg, 4);
    }

    public static String toErrorMsg(String msg) {
        // return getMsg("ERROR      ", msg, 4);
        return toMsg(TYPE_ERROR, msg, -2);
    }

    public static String toIgnoreMsg(String msg) {
        // return getMsg("IGNORE    ", msg, 4);
        return toMsg(TYPE_IGNORE, msg, -2);
    }

    public static String toInfo(String msg) {
        return toMsg(TYPE_INFO, msg, -2, false);
    }

    public static String toInfoMsg(String msg) {
        // return getMsg("INFO        ", msg, 4);
        return toMsg(TYPE_INFO, msg, -2);
    }

    public static String toInfoMsg(String msg, boolean flag) {
        // return getMsg("INFO        ", msg, 4);
        return toMsg(TYPE_INFO, msg, -2, flag);
    }

    public static String toMsg(String type, String msg) {
        return getMsg(String.format("%1$-7S", type), msg, -2);
    }

    public static String toMsg(String type, String msg, int index) {
        return getMsg(String.format("%1$-7S", type), msg, index);
    }

    public static String toMsg(String type, String msg, int index, boolean flag) {
        return getMsg(String.format("%1$-7S", type), msg, index, flag);
    }

    public static String toSuccessMsg(String msg) {
        // return getMsg("SUCCESS  ", msg, 4);
        return toMsg(TYPE_SUCCESS, msg, -2);
    }

    public static String toWarningMsg(String msg) {
        // return getMsg("WARNING", msg, 4);
        return toMsg(TYPE_WARNING, msg, -2);
    }

}
